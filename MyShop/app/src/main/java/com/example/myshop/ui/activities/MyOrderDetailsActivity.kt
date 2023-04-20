package com.example.myshop.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.models.Order
import com.example.myshop.ui.adapter.CartItemsListAdapter
import com.example.myshop.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MyOrderDetailsActivity : AppCompatActivity() {

    private var tv_order_details_id : TextView? = null
    private var tv_order_details_date : TextView? = null
    private var tv_order_status : TextView? = null
    private var rv_my_order_items_list : RecyclerView? = null
    private var tv_my_order_details_address_type : TextView? = null
    private var tv_my_order_details_full_name : TextView? = null
    private var tv_my_order_details_address : TextView? = null
    private var tv_my_order_details_additional_note : TextView? = null
    private var tv_my_order_details_other_details : TextView? = null
    private var tv_my_order_details_mobile_number : TextView? = null
    private var tv_order_details_sub_total : TextView? = null
    private var tv_order_details_shipping_charge : TextView? = null
    private var tv_order_details_total_amount : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order_details)

        tv_order_details_id = findViewById(R.id.tv_order_details_id)
        tv_order_details_date = findViewById(R.id.tv_order_details_date)
        tv_order_status = findViewById(R.id.tv_order_status)
        rv_my_order_items_list = findViewById(R.id.rv_my_order_items_list)
        tv_my_order_details_address_type = findViewById(R.id.tv_my_order_details_address_type)
        tv_my_order_details_full_name = findViewById(R.id.tv_my_order_details_full_name)
        tv_my_order_details_address = findViewById(R.id.tv_my_order_details_address)
        tv_my_order_details_additional_note = findViewById(R.id.tv_my_order_details_additional_note)
        tv_my_order_details_other_details = findViewById(R.id.tv_my_order_details_other_details)
        tv_my_order_details_mobile_number = findViewById(R.id.tv_my_order_details_mobile_number)
        tv_order_details_sub_total = findViewById(R.id.tv_order_details_sub_total)
        tv_order_details_shipping_charge = findViewById(R.id.tv_order_details_shipping_charge)
        tv_order_details_total_amount = findViewById(R.id.tv_order_details_total_amount)



        var myOrderDetails: Order = Order()
        if(intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)){
            myOrderDetails =  intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDER_DETAILS)!!


        }

        setupUI(myOrderDetails)








        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
//            startActivity(Intent(this, ProductsFragment::class.java))
            onBackPressed()
        }
    }


    private fun setupUI(orderDetails: Order){
        tv_order_details_id?.text = orderDetails.title

        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime
        val orderDateTime = formatter.format(calendar.time)
        tv_order_details_date?.text = orderDateTime

        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHour: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours","$diffInHour")

        when{
            diffInHour < 1 ->{
                tv_order_status?.text = resources.getString(R.string.order_status_pending)
                tv_order_status?.setTextColor(
                    ContextCompat.getColor(
                        this,R.color.colorAccent
                    )
                )
            }
            diffInHour < 2 ->{
                tv_order_status?.text = resources.getString(R.string.order_status_in_process)
                tv_order_status?.setTextColor(
                    ContextCompat.getColor(
                        this,R.color.colorOrderStatusInProcess
                    )
                )
            }else -> {
            tv_order_status?.text = resources.getString(R.string.order_status_delivered)
            tv_order_status?.setTextColor(
                ContextCompat.getColor(
                    this,R.color.colorOrderStatusDelivered
                )
            )
            }
        }

        rv_my_order_items_list?.layoutManager = LinearLayoutManager(this)
        rv_my_order_items_list?.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this, orderDetails.items, false)
        rv_my_order_items_list?.adapter = cartListAdapter

        tv_my_order_details_address_type?.text = orderDetails.address.type
        tv_my_order_details_full_name?.text = orderDetails.address.name
        tv_my_order_details_address?.text = "${orderDetails.address.address}, ${orderDetails.address.zipCode}"
        tv_my_order_details_additional_note?.text = orderDetails.address.additionalNote

        if(orderDetails.address.otherDetails.isNotEmpty()){
            tv_my_order_details_other_details?.visibility = View.VISIBLE
            tv_my_order_details_other_details?.text = orderDetails.address.otherDetails
        }else{
            tv_my_order_details_other_details?.visibility = View.GONE
        }

        tv_my_order_details_mobile_number?.text = orderDetails.address.mobileNumber

        tv_order_details_sub_total?.text = orderDetails.sub_total_amount
        tv_order_details_shipping_charge?.text = orderDetails.shipping_charge
        tv_order_details_total_amount?.text = orderDetails.total_amount



    }

}