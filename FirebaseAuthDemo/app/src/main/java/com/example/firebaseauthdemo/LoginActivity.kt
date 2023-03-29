package com.example.firebaseauthdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.firebaseauthdemo.utils.ForgotPasswordActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    var et_login_email : EditText? = null
    var et_login_password : EditText? = null
    var tv_register_btn : TextView? = null
    var login_btn : Button? = null
    var tv_forgot_password : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        et_login_email = findViewById(R.id.et_email)
        et_login_password = findViewById(R.id.et_password)
        tv_register_btn = findViewById(R.id.tv_register)
        login_btn = findViewById(R.id.btn_login)
        tv_forgot_password = findViewById(R.id.tv_forgot_password)

        tv_forgot_password?.setOnClickListener{
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        //to Register screen
        tv_register_btn?.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        login_btn?.setOnClickListener{
            when{
                TextUtils.isEmpty(et_login_email?.text.toString().trim(){it <= ' '}) -> {
                    Toast.makeText(this,"Please enter email",
                        Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(et_login_password?.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }else -> {
                val email: String = et_login_email?.text.toString().trim(){it <= ' '}
                val password: String = et_login_password?.text.toString().trim(){it <= ' '}

                // Log-In using FirebaseAuth
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(
                        OnCompleteListener { task ->
                            // If the Login is successfully done
                            if(task.isSuccessful){

                                Toast.makeText(
                                    this,
                                    "You are logged in successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                /**
                                 * Here the new user registered is automatically signed-in so we just sign-out the user from firebase
                                 * and send him to Main Screen with user id and email that user have used for registration.
                                 */

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id",FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id",email)
                                startActivity(intent)
                                finish()
                            }else{
                                // If the login is not successful then show error message.
                                Toast.makeText(
                                    this,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
            }
            }
        }
    }
}