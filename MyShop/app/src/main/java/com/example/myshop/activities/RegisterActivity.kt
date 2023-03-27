package com.example.myshop.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.R

class RegisterActivity :  BaseActivity() {

    private var et_first_name : EditText? = null
    private var et_last_name : EditText? = null
    private var et_email : EditText? = null
    private var et_password : EditText? = null
    private var et_confirm_password : EditText? = null
    private var cb_terms_and_condition : CheckBox? = null
    private var btn_register : Button? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        et_first_name = findViewById(R.id.et_first_name)

        et_last_name = findViewById(R.id.et_last_name)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_confirm_password = findViewById(R.id.et_confirm_password)
        cb_terms_and_condition = findViewById(R.id.cb_terms_and_condition)
        btn_register = findViewById(R.id.btn_register)



        //login
        val tv_login_btn : TextView = findViewById(R.id.tv_login)

        tv_login_btn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //back click
        var back_btn : View
        back_btn = findViewById(R.id.toolbar_register_activity)
        back_btn.setOnClickListener{
            onBackPressed()
        }

        btn_register?.setOnClickListener {
            validateRegisterDetails()
        }




    }
    //validateRegister
    private fun validateRegisterDetails() : Boolean{
        return when{
            TextUtils.isEmpty(et_first_name?.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name),true)
                false
            }
            TextUtils.isEmpty(et_last_name?.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name),true)
                false
            }
            TextUtils.isEmpty(et_email?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(et_confirm_password?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }
            et_password?.text.toString().trim{it <= ' '} != et_confirm_password?.text.toString().trim{it <= ' '} ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !cb_terms_and_condition?.isChecked!! -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition), true)
                false
            }
            else -> {
                showErrorSnackBar(resources.getString(R.string.registery_successfull), false)
                true
            }

        }
    }

}