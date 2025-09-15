# Patika Store (CLI)

Java 21 + Maven + PostgreSQL ile terminal tabanlı e-ticaret iskeleti.

## Özellikler
- Kayıt (e-posta + şifre) / Giriş (SHA-256 hash)
- Ürün listeleme & arama
- Sepete ekle / Sepeti gör / Siparişi tamamla
- Stoktan atomik düşüm (yetersiz stokta rollback)

## Kurulum
1. Java 21 + Maven kurulu olmalı.
2. PostgreSQL’de `eticaret` DB’sini oluştur.
3. `ConnectionFactory.java` içinde `URL/USER/PASS` değerlerini düzenle.
4. Şemayı yükle ve örnek ürünleri ekle (README’deki SQL’ler).

## Çalıştırma
IDE’den `PatikaStoreMain`’i çalıştırın.

## Kullanım
- Misafir: `1) Kayıt`, `2) Giriş`, `0) Çıkış`
- Girişli: `3) Listele`, `4) Ara`, `5) Sepete Ekle`, `6) Sepeti Göster`,
  `7) Sipariş`, `9) Oturumu Kapat`, `0) Çıkış`
