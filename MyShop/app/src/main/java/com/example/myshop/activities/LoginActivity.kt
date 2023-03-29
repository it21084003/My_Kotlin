package com.example.myshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myshop.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {

    private var et_email : EditText? = null
    private var et_password : EditText? = null
    private var tv_register_btn : TextView? = null
    private var tv_forgot_password : TextView? = null
    private var btn_login : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        tv_register_btn = findViewById(R.id.tv_register)
        tv_forgot_password = findViewById(R.id.tv_forgot_password)
        btn_login = findViewById(R.id.btn_login)

        //to Register screen
//        tv_register_btn?.setOnClickListener{
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//
//        }
        tv_register_btn?.setOnClickListener(this)
        tv_forgot_password?.setOnClickListener(this)
        btn_login?.setOnClickListener(this)

    }

    // In Login screen the clickable components are Login Button, ForgotPassword text and Register Text.
    override fun onClick(v: View?){
        if(v != null){
            when(v.id){
                R.id.tv_forgot_password -> {
                    val intent = Intent(this, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login -> {
                    LoginRegistereduser()
                }
                R.id.tv_register -> {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean{
        return when{
            TextUtils.isEmpty(et_email?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }else -> {
                //showErrorSnackBar("Your details are valid.", false)
                true
            }
        }
    }

    private fun LoginRegistereduser(){
        if(validateLoginDetails()){
            //show the progress dialog
            showProgressDialog(resources.getString((R.string.please_wait)))

            //Get the text from edittext and trim the place
            val email = et_email?.text.toString().trim { it <= ' ' }
            val password = et_password?.text.toString().trim { it <= ' ' }

            //log in using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener{task ->

                    hideProgressDialog()

                    if(task.isSuccessful){
                        showErrorSnackBar("You are looged in successfully", false)
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }

    }

}