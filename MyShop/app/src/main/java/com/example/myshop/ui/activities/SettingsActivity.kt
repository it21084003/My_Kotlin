package com.example.myshop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity() , View.OnClickListener{

   private lateinit var mUserDetails: User

    private var iv_image_view : ImageView? = null
    private var tv_edit : TextView? = null
    private var tv_name : TextView? = null
    private var tv_gender : TextView? = null
    private var tv_email : TextView? = null
    private var tv_ph_number : TextView? = null
    private var btn_logout : Button? = null
    private var tv_back_btn : TextView? = null
    private var tv_verfied: TextView? = null
    private var ll_address: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        iv_image_view = findViewById(R.id.iv_user_photo)
        tv_edit = findViewById(R.id.tv_edit)
        tv_name = findViewById(R.id.tv_name)
        tv_gender = findViewById(R.id.tv_gender)
        tv_email = findViewById(R.id.tv_email)
        tv_ph_number = findViewById(R.id.tv_mobile_number)
        tv_back_btn = findViewById(R.id.back_button)
        btn_logout = findViewById(R.id.btn_logout)
        tv_verfied = findViewById(R.id.tv_verify_icon)
        ll_address = findViewById(R.id.ll_address)

        tv_back_btn?.setOnClickListener(this)
        tv_edit?.setOnClickListener(this)
        btn_logout?.setOnClickListener(this)
        ll_address?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.back_button -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                }
                R.id.tv_edit -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
                R.id.ll_address -> {
                    startActivity(Intent(this, AddressListActivity::class.java))

                }
            }
        }


    }

    private fun getuserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this)

    }

    fun userDetailsSuccess(user: User){
        mUserDetails = user

        hideProgressDialog()

        GlideLoader(this).loaduserPicture(user.image, iv_image_view!!  )
        tv_name?.text = "${user.firstName} ${user.lastName}"
        tv_gender?.text = user.gender
        tv_email?.text = user.email
        tv_ph_number?.text = user.mobile.toString()

        checkVerify()

    }

    override fun onResume() {
        super.onResume()
        getuserDetails()
    }

    fun checkVerify(){
        if(mUserDetails.profileCompleted == 1) {
            tv_verfied?.setBackgroundResource(R.drawable.ic_verified_success_24)
        }else{
            tv_verfied?.setBackgroundResource(R.drawable.ic_verified_24)
        }
    }

}