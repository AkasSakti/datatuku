package com.example.datatuku

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_PRODUCTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_QUANTITY INTEGER NOT NULL,
                $COLUMN_UNIT_PRICE REAL NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    fun insertProduct(name: String, quantity: Int, unitPrice: Double): Long {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_QUANTITY, quantity)
            put(COLUMN_UNIT_PRICE, unitPrice)
        }

        return writableDatabase.insert(TABLE_PRODUCTS, null, values)
    }

    fun updateProduct(id: Long, name: String, quantity: Int, unitPrice: Double): Int {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_QUANTITY, quantity)
            put(COLUMN_UNIT_PRICE, unitPrice)
        }

        return writableDatabase.update(
            TABLE_PRODUCTS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun deleteProduct(id: Long): Int {
        return writableDatabase.delete(
            TABLE_PRODUCTS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getAllProducts(): List<Product> {
        val products = mutableListOf<Product>()
        val cursor = readableDatabase.query(
            TABLE_PRODUCTS,
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_QUANTITY, COLUMN_UNIT_PRICE),
            null,
            null,
            null,
            null,
            "$COLUMN_ID DESC"
        )

        cursor.use {
            val idIndex = it.getColumnIndexOrThrow(COLUMN_ID)
            val nameIndex = it.getColumnIndexOrThrow(COLUMN_NAME)
            val quantityIndex = it.getColumnIndexOrThrow(COLUMN_QUANTITY)
            val unitPriceIndex = it.getColumnIndexOrThrow(COLUMN_UNIT_PRICE)

            while (it.moveToNext()) {
                products.add(
                    Product(
                        id = it.getLong(idIndex),
                        name = it.getString(nameIndex),
                        quantity = it.getInt(quantityIndex),
                        unitPrice = it.getDouble(unitPriceIndex)
                    )
                )
            }
        }

        return products
    }

    companion object {
        private const val DATABASE_NAME = "datatuku.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_PRODUCTS = "products"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "nama_produk"
        const val COLUMN_QUANTITY = "jumlah"
        const val COLUMN_UNIT_PRICE = "harga_satuan"
    }
}
