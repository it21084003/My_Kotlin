package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    @Suppress("DEPRECATION")
    Handler().postDelayed(
        {
        startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))

        finish()
    },250
    )





    }
}


