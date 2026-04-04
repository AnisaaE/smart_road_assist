# Performance Results

## Durum
Bu ortamda testler çalıştırılamadı.

Sebep:
- `docker` kurulu/değil veya PATH'te değil
- `k6` kurulu/değil veya PATH'te değil

## Hazır altyapı
- Test scripti: `load-tests/k6/dispatcher-load.js`
- Otomasyon scripti: `load-tests/run-load-tests.ps1`
- Sonuç klasörü: `load-tests/results`

## Çalıştırma
`powershell -ExecutionPolicy Bypass -File .\load-tests\run-load-tests.ps1 -BaseUrl http://localhost:8080`

Bu komut sonrası tablo otomatik doldurulacaktır.

## Sonuç Tablosu
| Profil | Ortalama (ms) | p95 (ms) | p99 (ms) | Hata % | Req/s |
|---|---:|---:|---:|---:|---:|
| p50 | - | - | - | - | - |
| p100 | - | - | - | - | - |
| p200 | - | - | - | - | - |
| p500 | - | - | - | - | - |
