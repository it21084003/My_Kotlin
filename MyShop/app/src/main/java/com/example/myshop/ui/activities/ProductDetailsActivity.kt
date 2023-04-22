package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.CartItem
import com.example.myshop.models.Product
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader

class ProductDetailsActivity : BaseActivity(), View.OnClickListener {

    private var mProductId: String = ""
    private lateinit var mProductDetails: Product
    private var mProductOwnerId : String = ""

    private var iv_product_detail_image : ImageView? = null
    private var tv_product_details_title : TextView? = null
    private var tv_product_details_price : TextView? = null
    private var tv_product_details_description : TextView? = null
    private var tv_product_details_available_quantity : TextView? = null
    private var btn_add_to_cart : Button? = null
    private var btn_go_to_cart : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productdetails)

        iv_product_detail_image = findViewById(R.id.iv_product_detail_image)
        tv_product_details_title = findViewById(R.id.tv_product_details_title)
        tv_product_details_price = findViewById(R.id.tv_product_details_price)
        tv_product_details_description = findViewById(R.id.tv_product_details_description)
        tv_product_details_available_quantity = findViewById(R.id.tv_product_details_available_quantity)
        btn_add_to_cart = findViewById(R.id.btn_add_to_cart)
        btn_go_to_cart = findViewById(R.id.btn_go_to_cart)

        btn_add_to_cart?.setOnClickListener(this)
        btn_go_to_cart?.setOnClickListener(this)

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        //var productOwnerId: String = ""

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            mProductOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }
        if(FirestoreClass().getCurrentuserID() == mProductOwnerId){
            btn_add_to_cart?.visibility = View.GONE
            btn_go_to_cart?.visibility = View.GONE
        }else{
            btn_add_to_cart?.visibility = View.VISIBLE
        }

        getProductDetails()









        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
//            startActivity(Intent(this, ProductsFragment::class.java))
            onBackPressed()
        }
    }

    private fun getProductDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getProductDetails(this,mProductId)
    }

    fun productExistsInCart(){
        hideProgressDialog()
        btn_add_to_cart?.visibility = View.GONE
        btn_go_to_cart?.visibility = View.VISIBLE
    }

    fun productDetailsSuccess(product: Product){

        mProductDetails = product

        GlideLoader(this).loaduserPicture(
            product.image,iv_product_detail_image!!
        )

        tv_product_details_title?.text = product.title
        tv_product_details_price?.text = "${product.price}$"
        tv_product_details_description?.text = product.description
        tv_product_details_available_quantity?.text = product.stock_quantity

        if(product.stock_quantity.toInt() == 0){
            hideProgressDialog()

            btn_add_to_cart?.visibility = View.GONE
            tv_product_details_available_quantity?.text =
                resources.getString(R.string.lbl_out_of_stock)
            tv_product_details_available_quantity?.setTextColor(
                ContextCompat.getColor(
                    this,R.color.colorSnackBarError
                )
            )
        }else{
            if(FirestoreClass().getCurrentuserID() == product.user_id){
                hideProgressDialog()
            }else{
                FirestoreClass().checkIfItemExitinCart(this,mProductId)
            }
        }


    }

    private fun addToCart(){
        val cartItem = CartItem(
            FirestoreClass().getCurrentuserID(),
            mProductOwnerId,
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY

        )

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().addCartItems(this, cartItem)
    }

    fun addToCartSuccess(){
        hideProgressDialog()
        Toast.makeText(this, resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_SHORT).show()

        btn_add_to_cart?.visibility = View.GONE
        btn_go_to_cart?.visibility = View.VISIBLE
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.btn_add_to_cart ->{
                    addToCart()
                }
                R.id.btn_go_to_cart -> {
                    startActivity(Intent(this, CartListActivity::class.java))
                }
            }
        }

    }
}