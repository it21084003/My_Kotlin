package com.example.myshop.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.Product
import com.example.myshop.ui.fragments.ProductsFragment
import com.example.myshop.utils.GlideLoader


open class MyProductsListAdapter (
    private val context: Context, private var list: ArrayList<Product>,private val fragment: ProductsFragment):
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,parent,false)
        )

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){

            GlideLoader(context).loadproductPicture(model.image, holder.itemView.findViewById(R.id.iv_item_image))
            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = "${model.price}"


            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).setOnClickListener{
                //TODO delete product
                fragment.deleteProduct(model.product_id)
            }
        }
    }
    override fun getItemCount(): Int {
        return  list.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}




