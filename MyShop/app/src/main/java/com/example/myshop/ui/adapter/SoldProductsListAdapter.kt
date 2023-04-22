package com.example.myshop.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.SoldProduct
import com.example.myshop.ui.activities.SoldProductDetailsActivity
import com.example.myshop.ui.adapters.MyProductsListAdapter
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader

open class SoldProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<SoldProduct>
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyProductsListAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model = list[position]

        if(holder is MyProductsListAdapter.MyViewHolder){
            GlideLoader(context).loadproductPicture(
                model.image,
                holder.itemView.findViewById(R.id.iv_item_image)
            )

            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = "$${model.price}"

            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).visibility = View.GONE

            holder.itemView.setOnClickListener{
                val intent = Intent(context, SoldProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_SOLD_ORDER_PRODUCT_DETAILS, model)
                context.startActivity(intent)
            }
        }
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

}