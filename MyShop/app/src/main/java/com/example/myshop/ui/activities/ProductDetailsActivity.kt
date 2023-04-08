package com.example.myshop.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Product
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader

class ProductDetailsActivity : BaseActivity() {

    private var mProductId: String = ""
    private var iv_product_detail_image : ImageView? = null
    private var tv_product_details_title : TextView? = null
    private var tv_product_details_price : TextView? = null
    private var tv_product_details_description : TextView? = null
    private var tv_product_details_available_quantity : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productdetails)

        iv_product_detail_image = findViewById(R.id.iv_product_detail_image)
        tv_product_details_title = findViewById(R.id.tv_product_details_title)
        tv_product_details_price = findViewById(R.id.tv_product_details_price)
        tv_product_details_description = findViewById(R.id.tv_product_details_description)
        tv_product_details_available_quantity = findViewById(R.id.tv_product_details_available_quantity)


        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product ID", mProductId)
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

    fun productDetailsSuccess(product: Product){
        hideProgressDialog()
        GlideLoader(this).loaduserPicture(
            product.image,iv_product_detail_image!!
        )

        tv_product_details_title?.text = product.title
        tv_product_details_price?.text = "${product.price}$"
        tv_product_details_description?.text = product.description
        tv_product_details_available_quantity?.text = product.stock_quantity





    }
}