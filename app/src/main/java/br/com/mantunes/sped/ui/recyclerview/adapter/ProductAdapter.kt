package br.com.mantunes.sped.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import br.com.mantunes.sped.databinding.ItemCorouselBinding
import br.com.mantunes.sped.model.ProductCarousel
import com.bumptech.glide.Glide

class ProductAdapter(private var productList  : List<ProductCarousel>): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class ProductViewHolder(val binding: ItemCorouselBinding ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemCorouselBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ProductViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.apply {
            Glide.with(productImage).load(product.productImage).into(productImage)
            productName.text = product.productName

            productImage.setOnClickListener{
                Toast.makeText(holder.binding.root.context, product.productName,
                Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun getItemCount(): Int {
        return productList.size
    }

//    fun updateData(list: ArrayList<ProductModel>) {
//        this.productList = list
//        notifyDataSetChanged()
//    }
//
//    //Use the method for item changed
//    fun itemChanged() {
//        // remove last item for test purposes
//        this.productList[0] = (ProductModel(R.drawable.londonlove, "Thi is cool"))
//        notifyItemChanged(0)
//    //Use the method for checking the itemRemoved
//    fun removeData() {
//        // remove last item for test purposes
//        val orgListSize = productList.size
//        this.productList = this.productList.subList(0, orgListSize - 1).toList() as ArrayList<ProductModel>
//        notifyItemRemoved(orgListSize - 1)
//    }
}