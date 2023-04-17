package com.example.myshop.ui.fragments

//import com.example.myshop.activities.ui.notifications.NotificationsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Order
import com.example.myshop.ui.adapter.MyOrdersListAdapter

class OrdersFragment : BaseFragment() {

    var rv_my_orders_items : RecyclerView? = null
    var tv_no_orders_found : TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_orders, container, false)
//        val textView: TextView = root.findViewById(R.id.text_orders)
//        textView.text = "This is Order Fragment"

        return root
    }

    fun populateOrdersListInUI(ordersList: ArrayList<Order>){
        hideProgressDialog()

        rv_my_orders_items = view?.findViewById(R.id.rv_my_order_items)
        tv_no_orders_found = view?.findViewById(R.id.tv_no_order_found)

        if(ordersList.size > 0){
            rv_my_orders_items?.visibility = View.VISIBLE
            tv_no_orders_found?.visibility = View.GONE

            rv_my_orders_items?.layoutManager = LinearLayoutManager(activity)
            rv_my_orders_items?.setHasFixedSize(true)

            val myOrdersAdapter = MyOrdersListAdapter(requireActivity(),ordersList)
            rv_my_orders_items?.adapter = myOrdersAdapter
        }
        else{
            rv_my_orders_items?.visibility = View.GONE
            tv_no_orders_found?.visibility = View.VISIBLE
        }
    }
    private fun getMyOrdersList(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyOrdersList(this)
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }
}