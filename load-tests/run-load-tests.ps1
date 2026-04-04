param(
    [string]$BaseUrl = "http://localhost:8080"
)

$ErrorActionPreference = "Stop"

$repoRoot = Split-Path -Parent $PSScriptRoot
$scriptPath = Join-Path $repoRoot "load-tests/k6/dispatcher-load.js"
$resultsDir = Join-Path $repoRoot "load-tests/results"
$reportPath = Join-Path $repoRoot "docs/performance/RESULTS.md"
$profiles = @("p50", "p100", "p200", "p500")

New-Item -ItemType Directory -Force -Path $resultsDir | Out-Null

$hasK6 = $null -ne (Get-Command k6 -ErrorAction SilentlyContinue)
$hasDocker = $null -ne (Get-Command docker -ErrorAction SilentlyContinue)

if (-not $hasK6 -and -not $hasDocker) {
    throw "Neither k6 nor docker is installed. Install at least one of them to run load tests."
}

$effectiveBaseUrl = $BaseUrl
if (-not $hasK6 -and $hasDocker -and $BaseUrl -match "localhost|127\.0\.0\.1") {
    $effectiveBaseUrl = $BaseUrl -replace "localhost|127\.0\.0\.1", "host.docker.internal"
}

foreach ($profile in $profiles) {
    $summaryFile = Join-Path $resultsDir "$profile-summary.json"
    if (Test-Path $summaryFile) {
        Remove-Item $summaryFile -Force
    }

    Write-Host "Running profile: $profile"

    if ($hasK6) {
        & k6 run `
            -e "BASE_URL=$effectiveBaseUrl" `
            -e "PROFILE=$profile" `
            --summary-export "$summaryFile" `
            "$scriptPath"
    }
    else {
        $workDir = $repoRoot.Replace('\\', '/')
        & docker run --rm -i `
            -v "${workDir}:/work" `
            -w /work `
            grafana/k6 run `
            -e "BASE_URL=$effectiveBaseUrl" `
            -e "PROFILE=$profile" `
            --summary-export "/work/load-tests/results/$profile-summary.json" `
            "/work/load-tests/k6/dispatcher-load.js"
    }
}

$rows = @()
foreach ($profile in $profiles) {
    $file = Join-Path $resultsDir "$profile-summary.json"
    if (-not (Test-Path $file)) {
        continue
    }

    $json = Get-Content $file -Raw | ConvertFrom-Json

    $avg = [math]::Round($json.metrics.http_req_duration.values.avg, 2)
    $p95 = [math]::Round($json.metrics.http_req_duration.values.'p(95)', 2)
    $p99 = [math]::Round($json.metrics.http_req_duration.values.'p(99)', 2)
    $err = [math]::Round(($json.metrics.http_req_failed.values.rate * 100), 2)
    $rps = [math]::Round($json.metrics.http_reqs.values.rate, 2)

    $rows += "| $profile | $avg | $p95 | $p99 | $err | $rps |"
}

$now = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
$report = @(
    "# Performance Results",
    "",
    "- Generated at: $now",
    "- Base URL: $effectiveBaseUrl",
    "",
    "| Profil | Ortalama (ms) | p95 (ms) | p99 (ms) | Hata % | Req/s |",
    "|---|---:|---:|---:|---:|---:|"
)

$report += $rows

$report += @(
    "",
    "## Evidence",
    "- JSON summaries: load-tests/results",
    "- Grafana screenshots: docs/performance"
)

Set-Content -Path $reportPath -Value ($report -join "`r`n") -Encoding UTF8
Write-Host "Report generated: $reportPath"
