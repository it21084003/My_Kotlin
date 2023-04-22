package com.example.myshop.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.myshop.R
import com.example.myshop.models.SoldProduct
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : BaseActivity() {

    var productDetails: SoldProduct = SoldProduct()
    var tv_sold_products_details_id : TextView? = null
    var tv_sold_products_details_date : TextView? = null
    var iv_product_item_image : ImageView? = null
    var tv_product_item_name : TextView? = null
    var tv_product_item_price : TextView? = null
    var tv_sold_products_quantity : TextView? = null
    var tv_sold_details_address_type : TextView? = null
    var tv_sold_details_full_name : TextView? = null
    var tv_sold_details_address : TextView? = null
    var tv_sold_details_additional_note : TextView? = null
    var tv_sold_details_other_details : TextView? = null
    var tv_sold_details_mobile_number : TextView? = null
    var tv_sold_product_sub_total : TextView? = null
    var tv_sold_product_shipping_charge : TextView? = null
    var tv_sold_product_total_amount : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_product_details)

        tv_sold_products_details_id = findViewById(R.id.tv_sold_products_details_id)
        tv_sold_products_details_date = findViewById(R.id.tv_sold_products_details_date)
        iv_product_item_image = findViewById(R.id.iv_product_item_image)
        tv_product_item_name = findViewById(R.id.tv_product_item_name)
        tv_product_item_price = findViewById(R.id.tv_product_item_price)
        tv_sold_products_quantity = findViewById(R.id.tv_sold_products_quantity)
        tv_sold_details_address_type = findViewById(R.id.tv_sold_details_address_type)
        tv_sold_details_full_name = findViewById(R.id.tv_sold_details_full_name)
        tv_sold_details_address = findViewById(R.id.tv_sold_details_address)
        tv_sold_details_additional_note = findViewById(R.id.tv_sold_details_additional_note)
        tv_sold_details_other_details = findViewById(R.id.tv_sold_details_other_details)
        tv_sold_details_mobile_number = findViewById(R.id.tv_sold_details_mobile_number)
        tv_sold_product_sub_total = findViewById(R.id.tv_sold_product_sub_total)
        tv_sold_product_shipping_charge = findViewById(R.id.tv_sold_product_shipping_charge)
        tv_sold_product_total_amount = findViewById(R.id.tv_sold_product_total_amount)


        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
//            startActivity(Intent(this, ProductsFragment::class.java))
            onBackPressed()
        }

        if(intent.hasExtra(Constants.EXTRA_SOLD_ORDER_PRODUCT_DETAILS)){
            productDetails =
                intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_ORDER_PRODUCT_DETAILS)!!
        }

        setupUI(productDetails)

    }

    private fun setupUI(productDetails: SoldProduct){

        tv_sold_products_details_id?.text = productDetails.order_id

        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.order_date
        tv_sold_products_details_date?.text = formatter.format(calendar.time)

        GlideLoader(this).loadproductPicture(
            productDetails.image,
            iv_product_item_image!!
        )

        tv_product_item_name?.text = productDetails.title
        tv_product_item_price?.text = "$${productDetails.price}"
        tv_sold_products_quantity?.text = productDetails.sold_quantity

        tv_sold_details_address_type?.text = productDetails.address.type
        tv_sold_details_full_name?.text = productDetails.address.name
        tv_sold_details_address?.text = "${productDetails.address.address}, ${productDetails.address.zipCode}"
        tv_sold_details_additional_note?.text = productDetails.address.additionalNote

        if(productDetails.address.otherDetails.isNotEmpty()){
            tv_sold_details_other_details?.visibility = View.VISIBLE
            tv_sold_details_other_details?.text = productDetails.address.otherDetails
        }else{
            tv_sold_details_other_details?.visibility = View.GONE
        }
        tv_sold_details_mobile_number?.text = productDetails.address.mobileNumber

        tv_sold_product_sub_total?.text = productDetails.sub_total_amount
        tv_sold_product_shipping_charge?.text = productDetails.shipping_charge
        tv_sold_product_total_amount?.text = productDetails.total_amount





    }






}