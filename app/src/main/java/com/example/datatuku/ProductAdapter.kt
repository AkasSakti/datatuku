package com.example.datatuku

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.datatuku.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onEditClick: (Product) -> Unit,
    private val onDeleteClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val products = mutableListOf<Product>()
    private val rupiahFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

    fun submitList(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.textProductName.text = product.name
            binding.textProductDetail.text = itemView.context.getString(
                R.string.product_detail,
                product.quantity,
                rupiahFormat.format(product.unitPrice)
            )
            binding.textProductSubtotal.text = itemView.context.getString(
                R.string.product_subtotal,
                rupiahFormat.format(product.subtotal)
            )
            binding.buttonEdit.setOnClickListener { onEditClick(product) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(product) }
        }
    }
}
