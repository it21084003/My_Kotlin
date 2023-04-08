package com.example.myshop.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Product
import com.example.myshop.ui.activities.AddProductActivity
import com.example.myshop.ui.adapters.MyProductsListAdapter

class ProductsFragment : BaseFragment() {

    var rv_my_product_items : RecyclerView? = null
    var tv_no_product_found : TextView? = null
    private lateinit var mRootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true)
    }

    fun deleteProduct(productID: String){
        showAlertDialogToDeleteProduct(productID)
      }

    fun productDeleteSuccess(){
        hideProgressDialog()

        Toast.makeText(requireActivity(),
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT).show()

        //again
        getProductListFromFireStore()

    }

    private fun showAlertDialogToDeleteProduct(productID: String){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //perforaming positive action
        builder.setPositiveButton(resources.getString(R.string.yes)){
            dialogInterface, _ ->
            showProgressDialog(resources.getString(R.string.please_wait))

            FirestoreClass().deleteProduct(this, productID)
            dialogInterface.dismiss()
        }
        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.no)){
                dialogInterface, _ ->

        }
        //Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        //Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()

    }

    fun successProductsListFromFireStore(productList: ArrayList<Product>){
        hideProgressDialog()
        rv_my_product_items = view?.findViewById(R.id.rv_my_product_items)
        tv_no_product_found = view?.findViewById(R.id.tv_no_product_found)

        if(productList.size > 0){
            rv_my_product_items?.visibility = View.VISIBLE
            tv_no_product_found?.visibility = View.GONE

            rv_my_product_items?.layoutManager = LinearLayoutManager(activity)
            rv_my_product_items?.setHasFixedSize(true)
            val adapterProducts = MyProductsListAdapter(requireActivity(), productList,this)
            rv_my_product_items?.adapter = adapterProducts

        }else{
            rv_my_product_items?.visibility = View.GONE
            tv_no_product_found?.visibility = View.VISIBLE
        }
    }

    private fun getProductListFromFireStore(){
        showProgressDialog(resources.getString(R.string.please_click_back_again_to_exit))
        FirestoreClass().getProductList(this)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_products, container, false)
//        val textView: TextView = root.findViewById(R.id.tv_no_product_found)
//        textView.text = "This is Product Fragment"

        return mRootView
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}