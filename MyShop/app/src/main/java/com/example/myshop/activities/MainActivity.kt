package com.example.myshop.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.myshop.R
import com.example.myshop.utils.Constants

class MainActivity : AppCompatActivity() {

    var tv_main : TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_main = findViewById(R.id.tv_main)

        val sharedPreference = getSharedPreferences(Constants.MYSHOP_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreference.getString(Constants.LOGGED_IN_USERNAME,"")!!
        tv_main?.text = "The logged in user is $username"

    }
}