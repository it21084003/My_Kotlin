package com.example.myshop.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.CartItem
import com.example.myshop.models.Product
import com.example.myshop.ui.adapter.CartItemsListAdapter


class CartListActivity : BaseActivity() {

    var rv_cart_items_list : RecyclerView? = null
    var ll_checkout : LinearLayout? = null
    var tv_no_cart_item_found : TextView? = null
    var tv_sub_total : TextView? = null
    var tv_shipping_charge : TextView? = null
    var tv_total_amount : TextView? = null

    private lateinit var mProductsList : ArrayList<Product>
    private lateinit var mCartListItems : ArrayList<CartItem>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)


        rv_cart_items_list = findViewById(R.id.rv_cart_items_list)
        ll_checkout = findViewById(R.id.ll_checkout)
        tv_no_cart_item_found = findViewById(R.id.tv_no_cart_item_found)
        tv_sub_total = findViewById(R.id.tv_sub_total)
        tv_shipping_charge = findViewById(R.id.tv_shipping_charge)
        tv_total_amount = findViewById(R.id.tv_total_amount)




        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
            onBackPressed()
        }
    }

    fun itemRemovedSuccess(){
        hideProgressDialog()
        Toast.makeText(this,
        resources.getString(R.string.msg_item_removed_successfully),
        Toast.LENGTH_SHORT).show()

        getCartItemsList()
    }


    fun successCartItemsList(cartList: ArrayList<CartItem>){
        hideProgressDialog()

        for(product in mProductsList){
            for(cartItem in cartList){
                if(product.product_id == cartItem.product_id){
                    cartItem.stock_quantity = product.stock_quantity

                    if(product.stock_quantity.toInt() == 0){
                        cartItem.cart_quantity = product.stock_quantity
                    }
                }
            }
        }

        mCartListItems = cartList

        if(mCartListItems.size > 0){
            rv_cart_items_list?.visibility = View.VISIBLE
            ll_checkout?.visibility = View.VISIBLE
            tv_no_cart_item_found?.visibility = View.GONE

            rv_cart_items_list?.layoutManager = LinearLayoutManager(this)
            rv_cart_items_list?.setHasFixedSize(true)
            val cartListAdapter = CartItemsListAdapter(this, cartList)
            rv_cart_items_list?.adapter = cartListAdapter

            var subTotal : Double = 0.0
            for(item in mCartListItems){

                val availableQuantity = item.stock_quantity.toInt()
                if(availableQuantity > 0){
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subTotal += (price * quantity)
                }


            }

            tv_sub_total?.text = "$${subTotal}"
            tv_shipping_charge?.text = "$10.0"  //TODO change shipping charge

            if(subTotal > 0){
                ll_checkout?.visibility = View.VISIBLE

                val total = subTotal + 10   //TODO change logic
                tv_total_amount?.text = "$$total"
            }else{
                ll_checkout?.visibility = View.GONE
            }
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>){
        hideProgressDialog()
        mProductsList = productsList
        getCartItemsList()
    }

    fun itemUpdateSuccess(){
        hideProgressDialog()
        getCartItemsList()
    }

    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAllProductsList(this)
    }


    private fun getCartItemsList(){
       // showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCartList(this)
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductList()
    }
}