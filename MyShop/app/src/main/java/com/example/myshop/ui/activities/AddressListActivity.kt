package com.example.myshop.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Address
import com.example.myshop.ui.adapter.AddressListAdapter
import com.example.myshop.utils.Constants
import com.example.myshop.utils.SwipeToDeleteCallback
import com.example.myshop.utils.SwipeToEditCallback

class AddressListActivity : BaseActivity() {

    private var mSelectAddress: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        findViewById<TextView>(R.id.tv_add_address).setOnClickListener{
            val intent = Intent(this, AddEditAddressActivity::class.java)
            startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        }

        if(intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mSelectAddress = intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }

        if(mSelectAddress){
            findViewById<TextView>(R.id.tv_title).text = "SELECT ADDRESS"
        }

        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
            onBackPressed()
        }

        getAddressList()
    }
    override fun onResume() {
        super.onResume()
        // getAddressList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Constants.ADD_ADDRESS_REQUEST_CODE){
                getAddressList()
            }
            }else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("Request Cancelled", "To add the address.")
        }
    }

    fun deleteAddressSuccess(){
        hideProgressDialog()
        Toast.makeText(this, resources.getString(R.string.err_your_address_deleted_successfully),
        Toast.LENGTH_SHORT).show()

        getAddressList()
    }

    private fun getAddressList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getAddressList(this)
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {
        hideProgressDialog()

        if (addressList.size > 0) {
            findViewById<RecyclerView>(R.id.rv_address_list).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tv_no_address_found).visibility = View.GONE

            findViewById<RecyclerView>(R.id.rv_address_list).layoutManager =
                LinearLayoutManager(this)
            findViewById<RecyclerView>(R.id.rv_address_list).setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList, mSelectAddress)
            findViewById<RecyclerView>(R.id.rv_address_list).adapter = addressAdapter


            if (!mSelectAddress) {

                val editSwipeHandler = object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter =
                            findViewById<RecyclerView>(R.id.rv_address_list).adapter as AddressListAdapter
                        adapter.notifyEditItem(
                            this@AddressListActivity, viewHolder.adapterPosition
                        )
                    }
                }

                val editItemTochHelper = ItemTouchHelper(editSwipeHandler)
                editItemTochHelper.attachToRecyclerView(findViewById<RecyclerView>(R.id.rv_address_list))

                val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        showProgressDialog(resources.getString(R.string.please_wait))

                        FirestoreClass().deleteAddress(
                            this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id
                        )
                    }
                }

                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(findViewById<RecyclerView>(R.id.rv_address_list))

            }
        } else {
            findViewById<RecyclerView>(R.id.rv_address_list).visibility = View.GONE
            findViewById<TextView>(R.id.tv_no_address_found).visibility = View.VISIBLE
        }

    }
}