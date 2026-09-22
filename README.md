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

## Alternatif Solusi Kendala SQLite

SQLite pada Android tidak perlu diinstal terpisah karena sudah tersedia di dalam sistem Android. Yang perlu disiapkan adalah Android Studio, SDK Android, dan kode helper database di aplikasi.

Jika mengalami kesulitan dari instalasi sampai pemanfaatan SQLite, gunakan pengecekan berikut.

### 1. Saat Instalasi atau Setup Project

Pastikan Android Studio sudah terpasang dengan komponen berikut:

- Android SDK Platform API 34 untuk Android 14.
- Android SDK Build-Tools.
- Android Emulator jika ingin mencoba tanpa perangkat fisik.
- JDK 17, biasanya sudah tersedia otomatis di Android Studio versi baru.

Jika Gradle Sync gagal:

- Pastikan internet aktif saat pertama kali membuka project.
- Buka `File > Settings > Build, Execution, Deployment > Build Tools > Gradle`.
- Gunakan Gradle JDK bawaan Android Studio, biasanya bernama `Embedded JDK`.
- Klik `Sync Project with Gradle Files`.

Jika muncul error dependency, cek file:

```text
app/build.gradle
```

Pastikan dependency utama berikut ada:

```gradle
implementation("androidx.core:core-ktx:1.13.1")
implementation("androidx.appcompat:appcompat:1.7.0")
implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
implementation("com.google.android.material:material:1.12.0")
implementation("androidx.recyclerview:recyclerview:1.3.2")
```

### 2. Saat Database Tidak Terbuat

Database SQLite dibuat otomatis oleh class:

```text
app/src/main/java/com/example/datatuku/ProductDatabaseHelper.kt
```

Database baru dibuat ketika aplikasi pertama kali memanggil `ProductDatabaseHelper`, yaitu di `MainActivity`.

Jika database belum muncul:

- Jalankan aplikasi minimal satu kali.
- Tambahkan satu data produk melalui form.
- Pastikan tidak ada error pada method `onCreate` di `ProductDatabaseHelper.kt`.
- Pastikan nama tabel dan kolom sama antara query insert, update, delete, dan read.

Query pembuatan tabel yang dipakai:

```sql
CREATE TABLE products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nama_produk TEXT NOT NULL,
    jumlah INTEGER NOT NULL,
    harga_satuan REAL NOT NULL
);
```

### 3. Saat Data Tidak Tersimpan

Jika tombol `Simpan` ditekan tetapi data tidak muncul:

- Pastikan `nama produk` tidak kosong.
- Pastikan `jumlah` berupa angka lebih dari 0.
- Pastikan `harga satuan` berupa angka lebih dari 0.
- Cek fungsi `insertProduct` di `ProductDatabaseHelper.kt`.
- Cek fungsi `loadProducts` di `MainActivity.kt`, karena fungsi ini membaca ulang data setelah simpan.

Pada aplikasi ini, alurnya adalah:

```text
Form input -> saveProduct() -> insertProduct() -> loadProducts() -> RecyclerView
```

### 4. Saat Data Tidak Berubah Setelah Edit

Jika edit tidak berhasil:

- Pastikan menekan tombol `Edit` pada item produk terlebih dahulu.
- Setelah form terisi data lama, ubah datanya lalu tekan `Update`.
- Cek apakah `selectedProductId` di `MainActivity.kt` berisi `id` produk yang benar.
- Cek fungsi `updateProduct` di `ProductDatabaseHelper.kt`.

Alur edit:

```text
Klik Edit -> fillFormForEdit() -> Update -> updateProduct() -> loadProducts()
```

### 5. Saat Data Tidak Terhapus

Jika produk tidak terhapus:

- Pastikan memilih tombol `Hapus`.
- Pastikan menekan konfirmasi hapus pada dialog.
- Cek fungsi `deleteProduct` di `ProductDatabaseHelper.kt`.
- Setelah hapus, aplikasi memanggil `loadProducts()` agar daftar diperbarui.

Alur hapus:

```text
Klik Hapus -> confirmDelete() -> deleteProduct() -> loadProducts()
```

### 6. Saat Struktur Tabel Berubah

Jika nama kolom atau struktur tabel diubah saat aplikasi sudah pernah dijalankan, database lama di perangkat masih memakai struktur lama.

Solusinya:

- Hapus aplikasi dari emulator/perangkat lalu install ulang.
- Atau naikkan nilai `DATABASE_VERSION` di `ProductDatabaseHelper.kt`.
- Atau bersihkan data aplikasi melalui pengaturan perangkat.

Contoh menaikkan versi database:

```kotlin
private const val DATABASE_VERSION = 2
```

Pada project ini, method `onUpgrade` akan menghapus tabel lama dan membuat ulang tabel. Cara ini cukup untuk latihan CRUD sederhana, tetapi data lama akan hilang saat upgrade.

### 7. Cara Mengecek Isi Database

Gunakan fitur bawaan Android Studio:

1. Jalankan aplikasi di emulator atau perangkat.
2. Buka `View > Tool Windows > App Inspection`.
3. Pilih proses aplikasi `com.example.datatuku`.
4. Buka tab `Database Inspector`.
5. Pilih database `datatuku.db`.
6. Buka tabel `products`.

Di sana data hasil input bisa dilihat langsung.

### 8. Alternatif Jika SQLite Manual Terasa Sulit

Untuk tugas sederhana, `SQLiteOpenHelper` sudah cukup. Namun jika aplikasi semakin besar, alternatif yang lebih rapi adalah memakai Room Database.

Perbandingan singkat:

- `SQLiteOpenHelper`: cocok untuk belajar dasar SQL dan CRUD manual.
- `Room`: cocok untuk project lebih besar karena query lebih terstruktur dan lebih mudah dites.

Untuk project ini tetap digunakan `SQLiteOpenHelper` karena syaratnya adalah DBMS SQLite dengan satu tabel saja.

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
