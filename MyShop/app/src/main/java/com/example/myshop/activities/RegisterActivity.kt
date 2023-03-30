package com.example.myshop.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity :  BaseActivity() {

    private var et_first_name : EditText? = null
    private var et_last_name : EditText? = null
    private var et_email : EditText? = null
    private var et_password : EditText? = null
    private var et_confirm_password : EditText? = null
    private var cb_terms_and_condition : CheckBox? = null
    private var btn_register : Button? = null

    @SuppressLint("MissingInflatedId")
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
            onBackPressed()
        }

        //back click
        var back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
            onBackPressed()
        }

        //Register start
        btn_register?.setOnClickListener {
            registerUser()
        }
    }
    private fun registerUser(){
        // Check with validate function if the entries are valid or not.
        if(validateRegisterDetails()){

            showProgressDialog(resources.getString(R.string.please_wait))

//            val first_name: String = et_first_name?.text.toString().trim(){it <= ' '}
//            val last_name: String = et_last_name?.text.toString().trim(){it <= ' '}
            val email: String = et_email?.text.toString().trim(){it <= ' '}
            val password: String = et_password?.text.toString().trim(){it <= ' '}
//            val confirm_password: String = et_confirm_password?.text.toString().trim(){it <= ' '}
//            val cb_terms_and_condition: String? = cb_terms_and_condition?.toString()

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> {task ->



                        // If the registration is successfully done
                        if(task.isSuccessful){
                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            //firebase firestore
                            val user = User(
                                firebaseUser.uid,
                                et_first_name?.text.toString().trim(){it <= ' '},
                                et_last_name?.text.toString().trim(){it <= ' '},
                                et_email?.text.toString().trim(){it <= ' '}
                            )
                            //FiresStore Class
                            FirestoreClass().registerUser(this, user)



                            //after register logout
//                            FirebaseAuth.getInstance().signOut()
//                            finish()
                        }else{
                            hideProgressDialog()
                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(),
                                true)
                        }
                    }
                )
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

                //showErrorSnackBar(resources.getString(R.string.registery_successfull), false)
                true
            }

        }
    }

    fun userRegisterSuccess(){
        hideProgressDialog()

        Toast.makeText(this, resources.getString(R.string.register_success),
        Toast.LENGTH_SHORT).show()
    }

}