package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Address
import com.example.myshop.models.CartItem
import com.example.myshop.models.Order
import com.example.myshop.models.Product
import com.example.myshop.ui.adapter.CartItemsListAdapter
import com.example.myshop.utils.Constants


class CheckoutActivity : BaseActivity() {

    private var mAddressDetails: Address? = null
    private  lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    private var mSubTotal : Double = 0.0
    private var mTotalAmount: Double = 0.0

    private var tv_checkout_address_type : TextView? = null
    private var tv_checkout_full_name : TextView? = null
    private var tv_checkout_address : TextView? = null
    private var tv_checkout_additional_note : TextView? = null
    private var tv_checkout_other_details : TextView? = null
    private var tv_mobile_number : TextView? = null
    private var rv_cart_list_items : RecyclerView? = null
    private var tv_checkout_sub_total : TextView? = null
    private var tv_checkout_shipping_charge : TextView? = null
    private var ll_checkout_place_order : LinearLayout? = null
    private var tv_checkout_total_amount : TextView? = null
    private var btn_place_order : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        tv_checkout_address_type = findViewById(R.id.tv_checkout_address_type)
        tv_checkout_full_name = findViewById(R.id.tv_checkout_full_name)
        tv_checkout_address = findViewById(R.id.tv_checkout_address)
        tv_checkout_additional_note = findViewById(R.id.tv_checkout_additional_note)
        tv_checkout_other_details = findViewById(R.id.tv_checkout_other_details)
        tv_mobile_number = findViewById(R.id.tv_mobile_number)
        rv_cart_list_items = findViewById(R.id.rv_cart_list_items)
        tv_checkout_sub_total = findViewById(R.id.tv_checkout_sub_total)
        tv_checkout_shipping_charge = findViewById(R.id.tv_checkout_shipping_charge)
        ll_checkout_place_order = findViewById(R.id.ll_checkout_place_order)
        tv_checkout_total_amount = findViewById(R.id.tv_checkout_total_amount)
        btn_place_order = findViewById(R.id.btn_place_order)


        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails = intent.getParcelableExtra<Address>(
                Constants.EXTRA_SELECTED_ADDRESS
            )
        }
        if(mAddressDetails != null){
            tv_checkout_address_type?.text = mAddressDetails?.type
            tv_checkout_full_name?.text = mAddressDetails?.name
            tv_checkout_address?.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            tv_checkout_additional_note?.text = mAddressDetails?.additionalNote

            if(mAddressDetails?.otherDetails!!.isNotEmpty()){
                tv_checkout_other_details?.text = mAddressDetails?.otherDetails
            }
            tv_mobile_number?.text = mAddressDetails?.mobileNumber
        }


        getProductList()

        btn_place_order?.setOnClickListener{
            placeAnOrder()
        }




        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
            onBackPressed()
        }
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>){
        mProductsList = productsList
        getCartItemsList()
    }

    private fun getCartItemsList(){
        FirestoreClass().getCartList(this)
    }

    fun allDetailsUpdatedSuccessfully(){
        hideProgressDialog()
        Toast.makeText(this,"Your order was placed successfull",
            Toast.LENGTH_SHORT).show()

        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun orderPlacedSuccess(){
       FirestoreClass().updateAllDetails(this, mCartItemsList)
    }

    private fun placeAnOrder(){
        showProgressDialog(resources.getString(R.string.please_wait))

        if(mAddressDetails != null){
            val order = Order(
                FirestoreClass().getCurrentuserID(),
                mCartItemsList,
                mAddressDetails!!,
                "My order ${System.currentTimeMillis()}",
                mCartItemsList[0].image,
                mSubTotal.toString(),
                "10.0",
                mTotalAmount.toString(),
            )
            FirestoreClass().placeOrder(this, order)
        }

    }

    fun successCartItemsList(cartList: ArrayList<CartItem>){
        hideProgressDialog()

        for(product in mProductsList){
            for(cartItem in cartList){
                if(product.product_id == cartItem.product_id){
                    cartItem.stock_quantity = product.stock_quantity
                }
            }
        }
        mCartItemsList = cartList

        rv_cart_list_items?.layoutManager = LinearLayoutManager(this)
        rv_cart_list_items?.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this, mCartItemsList, false)
        rv_cart_list_items?.adapter = cartListAdapter

        for(item in mCartItemsList){
            val availableQuantity = item.stock_quantity.toInt()
            if(availableQuantity > 0){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price * quantity)

            }
        }

        tv_checkout_sub_total?.text = "$$mSubTotal"
        tv_checkout_shipping_charge?.text = "$10.0"

        if(mSubTotal > 0){
            ll_checkout_place_order?.visibility = View.VISIBLE
            mTotalAmount = mSubTotal + 10.0
            tv_checkout_total_amount?.text = "$$mTotalAmount"
        }else{
            ll_checkout_place_order?.visibility = View.GONE
        }

    }


    private fun getProductList(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }
}