package com.example.myshop.activities

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.models.User
import com.example.myshop.utils.Constants

class UserProfileActivity : BaseActivity() {

    private var btn_submit : Button? = null
    private var et_first_name : EditText? = null
    private var et_last_name : EditText? = null
    private var et_email : EditText? = null
    private var et_mobile_number: EditText? = null
    private var et_gender : RadioButton? = null
    private var et_image : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        et_first_name = findViewById(R.id.et_first_name)
        et_last_name = findViewById(R.id.et_last_name)
        et_email = findViewById(R.id.et_email)
        et_mobile_number = findViewById(R.id.et_mobile_number)
        et_image = findViewById(R.id.iv_user_photo)

        //Image Permission and import image file
        //Start
        et_image?.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"You already have access and file path", Toast.LENGTH_SHORT).show()
            }else{
                //Request Permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), CAMERA_AND_FILE_LOCATION_PERMISSION_CODE)
            }
        }
        //End

        var userDetails: User = User()
        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            //Get the User details from intent as a ParcelableExtra
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        et_first_name?.isEnabled = false
        et_first_name?.setText(userDetails.firstName)
        et_last_name?.isEnabled = false
        et_last_name?.setText(userDetails.lastName)
        et_email?.isEnabled = false
        et_email?.setText(userDetails.email)


        //back click
        var back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    //Image Permission and import image file
    //Start
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted for camera",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Oops you just denied the permission for camera",Toast.LENGTH_SHORT).show()

            }
        }
    }

    companion object{
        private const val CAMERA_PERMISSION_CODE = 1
        private const val FILE_LOCATION_PERMISSION_CODE = 2
        private const val CAMERA_AND_FILE_LOCATION_PERMISSION_CODE = 12
    }
    //End
}