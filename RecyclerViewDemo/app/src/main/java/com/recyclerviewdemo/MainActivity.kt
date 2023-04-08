package com.recyclerviewdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    val LIST_VIEW = "LIST_VIEW" // Value for List View
    val GRID_VIEW = "GRID_VIEW" // Value for Grid View

    var currentVisibleView: String =
        LIST_VIEW // Variable is used check which is current view visible as default it is list view.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listView()

        fabSwitch.setOnClickListener { view ->

            if (currentVisibleView == LIST_VIEW) {
                fabSwitch.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_list
                    )
                )
                gridView()
            } else {
                fabSwitch.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        R.drawable.ic_grid
                    )
                )
                listView()
            }
        }
    }

    /**
     * Function is used to show the items in a list format
     */
    private fun listView() {

        currentVisibleView = LIST_VIEW // Current view is updated

        // Set the LayoutManager that this RecyclerView will use.
        rvItemsList.layoutManager = LinearLayoutManager(this)
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = ItemAdapter(this, getItemsList())
        // adapter instance is set to the recyclerview to inflate the items.
        rvItemsList.adapter = itemAdapter
    }

    /**
     * Function is used to show the items in a grid format
     */
    private fun gridView() {

        currentVisibleView = GRID_VIEW // Current view is updated

        // Set the LayoutManager that this RecyclerView will use.
        rvItemsList.layoutManager = GridLayoutManager(this, 2)
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = ItemAdapter(this, getItemsList())
        // adapter instance is set to the recyclerview to inflate the items.
        rvItemsList.adapter = itemAdapter
    }

    /**
     * Function is used to get the Items List which is added in the list.
     */
    private fun getItemsList(): ArrayList<String> {
        val list = ArrayList<String>()

        list.add("Item One")
        list.add("Item Two")
        list.add("Item Three")
        list.add("Item Four")
        list.add("Item Five")
        list.add("Item Six")
        list.add("Item Seven")
        list.add("Item Eight")
        list.add("Item Nine")
        list.add("Item Ten")
        list.add("Item Eleven")
        list.add("Item Twelve")

        return list
    }
}