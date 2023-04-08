package com.example.myshop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Product
import com.example.myshop.ui.activities.SettingsActivity
import com.example.myshop.ui.adapter.DashboardItemsListAdapter

class DashboardFragment : BaseFragment() {

    var rv_my_product_items : RecyclerView? = null
    var tv_no_product_found : TextView? = null
    private lateinit var mRootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mRootView = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return mRootView
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.action_settings -> {
                // TODO Step 9: Launch the SettingActivity on click of action item.
                // START
                startActivity(Intent(activity, SettingsActivity::class.java))
                // END
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>){
        hideProgressDialog()

        rv_my_product_items = view?.findViewById(R.id.rv_my_product_items)
        tv_no_product_found = view?.findViewById(R.id.tv_no_product_found)

        if(dashboardItemsList.size > 0){
            rv_my_product_items?.visibility = View.VISIBLE
            tv_no_product_found?.visibility = View.GONE

            rv_my_product_items?.layoutManager = GridLayoutManager(activity,2)
            rv_my_product_items?.setHasFixedSize(true)
            val adapterProducts = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)
            rv_my_product_items?.adapter = adapterProducts

        }else{
            rv_my_product_items?.visibility = View.GONE
            tv_no_product_found?.visibility = View.VISIBLE
        }
    }

    private fun getDashboardItemList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getDashboardItemList(this)
    }

}