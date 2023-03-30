package com.example.myshop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myshop.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {

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
                showErrorSnackBar("Please enter email!", true)

            }else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            showErrorSnackBar("Email sent successfully to reset your password!", true)

                            finish()
                        }else{
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }

        //back click
        var back_btn : View
        back_btn = findViewById(R.id.toolbar_forget_password_activity)
        back_btn.setOnClickListener{
            onBackPressed()
        }
    }

//    private fun setupActionBar(){
//        setSupportActionBar(.toolbar_forget_password_activity)
//    }

}