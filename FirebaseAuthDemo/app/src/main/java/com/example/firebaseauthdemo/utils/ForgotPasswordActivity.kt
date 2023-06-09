package com.example.firebaseauthdemo.utils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firebaseauthdemo.R
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private var btn_submit : Button? = null
    private var et_forgot_email : EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btn_submit = findViewById(R.id.btn_login)
        et_forgot_email = findViewById(R.id.et_forgot_email)

        btn_submit?.setOnClickListener{
            val email:String = et_forgot_email?.text.toString().trim(){it <= ' '}
            if(email.isEmpty()){
                Toast.makeText(this,"Please enter email",
                    Toast.LENGTH_SHORT).show()
            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Email sent successfully to reset your password!",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this,task.exception!!.message.toString(),
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


    }
}