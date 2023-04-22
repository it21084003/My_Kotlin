package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    @Suppress("DEPRECATION")
    Handler().postDelayed(
        {
            val currentUserID = FirestoreClass().getCurrentuserID()

            if(currentUserID.isNotEmpty()){
                startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }


        finish()
    },2500
    )





    }
}


