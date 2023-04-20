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
import com.example.myshop.models.Order
import com.example.myshop.ui.activities.MyOrderDetailsActivity
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader


open class MyOrdersListAdapter (
    private val context: Context,
    private var list: ArrayList<Order>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){

            GlideLoader(context).loadproductPicture(
                model.image,
                holder.itemView.findViewById(R.id.iv_item_image)
            )

            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text = model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text = "$${model.total_amount}"

            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).visibility = View.GONE

            holder.itemView.setOnClickListener{
                val intent = Intent(context, MyOrderDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_MY_ORDER_DETAILS, model)
                context.startActivity(intent)
            }

        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)



}