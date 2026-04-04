# Load Tests (k6)

Bu klasör Dispatcher üzerinden geçen yük testlerini içerir.

## Dosyalar
- `k6/dispatcher-load.js`: 50 / 100 / 200 / 500 eşzamanlı kullanıcı profilleri.
- `results/`: test çıktıları (json/csv/ekran görüntüsü).

## Profiller
- `PROFILE=p50`  -> 50 eşzamanlı
- `PROFILE=p100` -> 100 eşzamanlı
- `PROFILE=p200` -> 200 eşzamanlı
- `PROFILE=p500` -> 500 eşzamanlı
- `PROFILE=full` -> hepsini sıralı çalıştırır

## Örnek çalıştırma
- `k6 run -e BASE_URL=http://localhost:8080 -e PROFILE=p50 load-tests/k6/dispatcher-load.js`
- `k6 run -e BASE_URL=http://localhost:8080 -e PROFILE=full load-tests/k6/dispatcher-load.js`

## Tek komutla tüm profiller + rapor
- `powershell -ExecutionPolicy Bypass -File .\\load-tests\\run-load-tests.ps1 -BaseUrl http://localhost:8080`

Bu script:
- p50/p100/p200/p500 profillerini çalıştırır,
- JSON summary dosyalarını `load-tests/results` altına yazar,
- otomatik raporu `docs/performance/RESULTS.md` dosyasına üretir.

## Örnek sonuç kaydetme
- `k6 run -e BASE_URL=http://localhost:8080 -e PROFILE=p200 --summary-export=load-tests/results/p200-summary.json load-tests/k6/dispatcher-load.js`

## Ölçülen metrikler
- `http_req_duration` (avg, p95, p99)
- `http_req_failed` (error rate)
- `checks` (assertion başarı oranı)
