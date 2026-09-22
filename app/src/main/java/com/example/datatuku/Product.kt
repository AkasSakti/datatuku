package com.example.datatuku

data class Product(
    val id: Long,
    val name: String,
    val quantity: Int,
    val unitPrice: Double
) {
    val subtotal: Double
        get() = quantity * unitPrice
}
