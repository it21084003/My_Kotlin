package com.example.firebaseauthdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var tv_userId : TextView? = null
        var tv_emailId : TextView? = null
        var btn_logout : Button? = null

        tv_userId = findViewById(R.id.tv_user_id)
        tv_emailId = findViewById(R.id.tv_email_id)
        btn_logout = findViewById(R.id.btn_logout)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        tv_userId.text = "User ID is : $userId"
        tv_emailId.text = "Email ID is : $emailId"

        btn_logout?.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }




    }
}