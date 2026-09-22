# DataTuku

DataTuku adalah aplikasi Android native Kotlin untuk toko kelontong kecil. Aplikasi ini menyimpan data produk belanja, yaitu nama produk, jumlah, dan harga satuan, lalu menghitung subtotal setiap produk serta total biaya yang harus dibayar.

## Fitur

- Tambah produk belanja.
- Tampilkan daftar produk dari SQLite.
- Edit data produk.
- Hapus data produk.
- Hitung subtotal otomatis dari `jumlah x harga satuan`.
- Hitung total bayar dari seluruh produk di daftar.
- Menggunakan satu tabel SQLite bernama `products`.
- Mendukung Android 10 sampai Android 14 melalui `minSdk 29` dan `targetSdk 34`.

## Teknologi

- Kotlin
- Android native XML layout
- SQLite dengan `SQLiteOpenHelper`
- RecyclerView
- Material Components

## Struktur Database

Database dibuat otomatis saat aplikasi pertama kali dijalankan.

Nama database:

```text
datatuku.db
```

Nama tabel:

```text
products
```

Struktur tabel:

```sql
CREATE TABLE products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nama_produk TEXT NOT NULL,
    jumlah INTEGER NOT NULL,
    harga_satuan REAL NOT NULL
);
```

Penjelasan kolom:

- `id`: primary key otomatis.
- `nama_produk`: nama barang atau produk.
- `jumlah`: jumlah barang yang dibeli.
- `harga_satuan`: harga satu barang.

Subtotal tidak disimpan di database karena dapat dihitung dari `jumlah * harga_satuan`. Total bayar juga tidak disimpan karena dihitung dari seluruh subtotal produk.

## Cara Membuat Aplikasi

1. Buat project Android baru di Android Studio.
2. Pilih template `Empty Views Activity`.
3. Gunakan bahasa `Kotlin`.
4. Atur minimum SDK ke API 29 agar kompatibel dengan Android 10.
5. Tambahkan dependensi AndroidX, AppCompat, Material Components, dan RecyclerView di `app/build.gradle`.
6. Buat model data `Product.kt`.
7. Buat class `ProductDatabaseHelper.kt` yang menurunkan `SQLiteOpenHelper`.
8. Di method `onCreate`, jalankan SQL `CREATE TABLE products`.
9. Buat fungsi CRUD:
   - `insertProduct`
   - `getAllProducts`
   - `updateProduct`
   - `deleteProduct`

   Fungsi-fungsi tersebut dibuat di file:

   ```text
   app/src/main/java/com/example/datatuku/ProductDatabaseHelper.kt
   ```

   File `ProductDatabaseHelper.kt` berisi class `ProductDatabaseHelper` yang menurunkan `SQLiteOpenHelper`. Di file ini database SQLite dibuat, tabel `products` didefinisikan, dan seluruh operasi tambah, baca, ubah, serta hapus data produk dijalankan.
10. Buat layout `activity_main.xml` untuk form input, total bayar, dan daftar produk.
11. Buat layout `item_product.xml` untuk tampilan tiap produk.
12. Buat `ProductAdapter.kt` untuk menampilkan data SQLite di RecyclerView.
13. Di `MainActivity.kt`, hubungkan form, database, adapter, dan perhitungan total.

## Cara Menjalankan

1. Buka folder project ini di Android Studio.
2. Tunggu proses Gradle Sync selesai.
3. Jalankan aplikasi pada emulator atau perangkat Android.
4. Gunakan emulator/perangkat Android 10, 11, 12, 13, atau 14.

Alternatif dari terminal:

```bash
./gradlew assembleDebug
```

Untuk Windows PowerShell:

```powershell
.\gradlew.bat assembleDebug
```

Jika belum ada Gradle Wrapper, buka project melalui Android Studio terlebih dahulu atau jalankan perintah `gradle wrapper` jika Gradle sudah terpasang di komputer.

## Alur Penggunaan

1. Masukkan nama produk, jumlah, dan harga satuan.
2. Aplikasi menampilkan subtotal input secara otomatis.
3. Tekan `Simpan`.
4. Produk muncul pada daftar produk.
5. Total bayar diperbarui otomatis.
6. Tekan `Edit` untuk mengubah data produk.
7. Tekan `Hapus` untuk menghapus produk.

## File Penting

- `app/src/main/java/com/example/datatuku/MainActivity.kt`: logika utama aplikasi.
- `app/src/main/java/com/example/datatuku/ProductDatabaseHelper.kt`: pembuatan database dan operasi CRUD.
- `app/src/main/java/com/example/datatuku/ProductAdapter.kt`: adapter daftar produk.
- `app/src/main/res/layout/activity_main.xml`: layout halaman utama.
- `app/src/main/res/layout/item_product.xml`: layout item produk.
