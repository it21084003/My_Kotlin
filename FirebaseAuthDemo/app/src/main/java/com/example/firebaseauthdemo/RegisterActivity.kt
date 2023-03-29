package com.example.firebaseauthdemo

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

//import com.google.firebase.auth.AuthResult
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import kotlinx.android.synthetic.main.activity_register.*

// TODO Step 6: Create an empty activity as Register Activity.
/**
 * Register Screen of the application.
 */
class RegisterActivity : AppCompatActivity() {

    private var et_register_email : EditText? = null
    private var et_register_password : EditText? = null
    private var btn_register : Button? = null
    private var btn_register_to_login : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_register)

        et_register_email = findViewById(R.id.et_register_email)
        et_register_password = findViewById(R.id.et_register_password)
        btn_register = findViewById(R.id.btn_register)
        btn_register_to_login = findViewById(R.id.tv_to_login)

        btn_register_to_login?.setOnClickListener{
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
            onBackPressed()
        }

        btn_register?.setOnClickListener{
            when{
                TextUtils.isEmpty(et_register_email?.text.toString().trim(){it <= ' '}) -> {
                    Toast.makeText(this,"Please enter email",
                Toast.LENGTH_SHORT).show()
            }
                TextUtils.isEmpty(et_register_password?.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this,
                    "Please enter password.",
                    Toast.LENGTH_SHORT
                ).show()
            }else -> {
                val email: String = et_register_email?.text.toString().trim(){it <= ' '}
                val password: String = et_register_password?.text.toString().trim(){it <= ' '}

                // Create an instance and create a register a user with email and password.
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                            // If the registration is successfully done
                            if(task.isSuccessful){
                                // Firebase registered user
                                val firebaseUser : FirebaseUser = task.result!!.user!!

                                Toast.makeText(
                                    this,
                                    "You are registered successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id",firebaseUser.uid)
                                intent.putExtra("email_id",email)
                                startActivity(intent)
                                finish()
                            }else{
                                // If the registering is not successful then show error message.
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