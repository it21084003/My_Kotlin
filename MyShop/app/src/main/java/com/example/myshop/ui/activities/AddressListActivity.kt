package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Address
import com.example.myshop.ui.adapter.AddressListAdapter

class AddressListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)



        findViewById<TextView>(R.id.tv_add_address).setOnClickListener{
            startActivity(Intent(this, AddEditAddressActivity::class.java))
        }




        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
            onBackPressed()
        }
    }

    private fun getAddressList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAddressList(this)
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>){
        hideProgressDialog()

        if(addressList.size > 0){
            findViewById<RecyclerView>(R.id.rv_address_list).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_address_found).visibility = View.GONE

            findViewById<RecyclerView>(R.id.rv_address_list).layoutManager = LinearLayoutManager(this)
            findViewById<RecyclerView>(R.id.rv_address_list).setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList)
            findViewById<RecyclerView>(R.id.rv_address_list).adapter = addressAdapter

        }else{
            findViewById<RecyclerView>(R.id.rv_address_list).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_address_found).visibility = View.VISIBLE
        }

    }

    override fun onResume() {
        super.onResume()
        getAddressList()
    }



}