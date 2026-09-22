package com.example.datatuku

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatuku.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: ProductDatabaseHelper
    private lateinit var adapter: ProductAdapter
    private val rupiahFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    private var selectedProductId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = ProductDatabaseHelper(this)
        setupProductList()
        setupForm()
        loadProducts()
    }

    override fun onDestroy() {
        databaseHelper.close()
        super.onDestroy()
    }

    private fun setupProductList() {
        adapter = ProductAdapter(
            onEditClick = { product -> fillFormForEdit(product) },
            onDeleteClick = { product -> confirmDelete(product) }
        )

        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerProducts.adapter = adapter
    }

    private fun setupForm() {
        binding.buttonSave.setOnClickListener { saveProduct() }
        binding.buttonCancel.setOnClickListener { resetForm() }

        binding.inputQuantity.doAfterTextChanged { updatePreviewTotal() }
        binding.inputUnitPrice.doAfterTextChanged { updatePreviewTotal() }
    }

    private fun saveProduct() {
        val name = binding.inputName.text.toString().trim()
        val quantity = binding.inputQuantity.text.toString().toIntOrNull()
        val unitPrice = binding.inputUnitPrice.text.toString().toDoubleOrNull()

        when {
            name.isEmpty() -> {
                binding.inputName.error = getString(R.string.error_required)
                return
            }
            quantity == null || quantity <= 0 -> {
                binding.inputQuantity.error = getString(R.string.error_positive_number)
                return
            }
            unitPrice == null || unitPrice <= 0.0 -> {
                binding.inputUnitPrice.error = getString(R.string.error_positive_number)
                return
            }
        }

        val selectedId = selectedProductId
        if (selectedId == null) {
            databaseHelper.insertProduct(name, quantity, unitPrice)
            showToast(R.string.message_saved)
        } else {
            databaseHelper.updateProduct(selectedId, name, quantity, unitPrice)
            showToast(R.string.message_updated)
        }

        resetForm()
        loadProducts()
    }

    private fun fillFormForEdit(product: Product) {
        selectedProductId = product.id
        binding.inputName.setText(product.name)
        binding.inputQuantity.setText(product.quantity.toString())
        binding.inputUnitPrice.setText(product.unitPrice.toString())
        binding.buttonSave.setText(R.string.action_update)
        binding.buttonCancel.visibility = View.VISIBLE
        updatePreviewTotal()
    }

    private fun confirmDelete(product: Product) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_title)
            .setMessage(getString(R.string.delete_message, product.name))
            .setNegativeButton(R.string.action_cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ ->
                databaseHelper.deleteProduct(product.id)
                showToast(R.string.message_deleted)
                if (selectedProductId == product.id) {
                    resetForm()
                }
                loadProducts()
            }
            .show()
    }

    private fun loadProducts() {
        val products = databaseHelper.getAllProducts()
        adapter.submitList(products)

        val total = products.sumOf { it.subtotal }
        binding.textTotalPayment.text = getString(
            R.string.total_payment,
            rupiahFormat.format(total)
        )
        binding.textEmpty.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updatePreviewTotal() {
        val quantity = binding.inputQuantity.text.toString().toIntOrNull() ?: 0
        val unitPrice = binding.inputUnitPrice.text.toString().toDoubleOrNull() ?: 0.0
        binding.textPreviewTotal.text = getString(
            R.string.preview_total,
            rupiahFormat.format(quantity * unitPrice)
        )
    }

    private fun resetForm() {
        selectedProductId = null
        binding.inputName.text?.clear()
        binding.inputQuantity.text?.clear()
        binding.inputUnitPrice.text?.clear()
        binding.buttonSave.setText(R.string.action_save)
        binding.buttonCancel.visibility = View.GONE
        updatePreviewTotal()
    }

    private fun showToast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }
}
