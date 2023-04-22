package com.example.myshop.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.SoldProduct
import com.example.myshop.ui.adapter.SoldProductsListAdapter

class SoldProductsFragment : BaseFragment() {

    var rv_sold_product_items : RecyclerView? = null
    var tv_no_sold_products_found : TextView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_products, container, false)
    }

    override fun onResume() {
        super.onResume()
        getSoldProductsList()
    }

    private fun getSoldProductsList(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getSoldProductsList(this)
    }

    fun successSoldProductList(soldProductsList: ArrayList<SoldProduct>){

        rv_sold_product_items = view?.findViewById(R.id.rv_sold_product_items)
        tv_no_sold_products_found = view?.findViewById(R.id.tv_no_sold_products_found)
        hideProgressDialog()
        if(soldProductsList.size > 0){
            rv_sold_product_items?.visibility = View.VISIBLE
            tv_no_sold_products_found?.visibility = View.GONE

            rv_sold_product_items?.layoutManager = LinearLayoutManager(activity)
            rv_sold_product_items?.setHasFixedSize(true)
            val adapterProducts = SoldProductsListAdapter(requireActivity(), soldProductsList)
            rv_sold_product_items?.adapter = adapterProducts
        }else{
            rv_sold_product_items?.visibility = View.GONE
            tv_no_sold_products_found?.visibility = View.VISIBLE
        }
    }
}