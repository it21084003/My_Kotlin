package com.example.myshop.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.User
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import java.io.IOException

class UserProfileActivity : BaseActivity() ,View.OnClickListener{

    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    private var btn_submit : Button? = null
    private var et_first_name : EditText? = null
    private var et_last_name : EditText? = null
    private var et_email : EditText? = null
    private var et_mobile_number: EditText? = null
    private var et_gender : RadioButton? = null
    private var image_view : ImageView? = null
    private var rb_female: RadioButton? = null
    private var rb_male :RadioButton? = null

    private lateinit var mUserDetails: User


//    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        et_first_name = findViewById(R.id.et_first_name)
        et_last_name = findViewById(R.id.et_last_name)
        et_email = findViewById(R.id.et_email)
        et_mobile_number = findViewById(R.id.et_mobile_number)
        image_view =findViewById(R.id.iv_image_view)
        btn_submit = findViewById(R.id.btn_submit)
        rb_male = findViewById(R.id.rb_male)
        rb_female = findViewById(R.id.rb_female)
        //tv_verfied = findViewById(R.id.tv_verify_icon)


    // Assign the on click event to the user profile photo.
    image_view?.setOnClickListener(this)

    // Assign the on click event to the SAVE button.
    btn_submit?.setOnClickListener(this)
        //Get User detail from firebase
        //Start

    if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
        // Get the user details from intent as a ParcelableExtra.
        mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
    }

    et_first_name?.setText(mUserDetails.firstName)
    et_last_name?.setText(mUserDetails.lastName)
    et_email?.isEnabled = false
    et_email?.setText(mUserDetails.email)

    if(mUserDetails.profileCompleted == 0){
        //tv_verfied?.setBackgroundColor(R.color.colorSnackBarSuccess!!)
        et_first_name?.isEnabled = false
        et_last_name?.isEnabled = false

    }else{
        //tv_verfied?.setBackgroundColor(R.color.colorPrimaryDark!!)
        GlideLoader(this).loaduserPicture(mUserDetails.image, image_view!!)

        et_email?.isEnabled = false
        et_email?.setText(mUserDetails.email)

        if(mUserDetails.mobile != 0L){
            et_mobile_number?.setText(mUserDetails.mobile.toString())
        }
        if(mUserDetails.gender == Constants.MALE){
            rb_male?.isChecked = true
        }else{
            rb_female?.isChecked = true
        }
    }
        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_image_view -> {

                    if (ContextCompat.checkSelfPermission(
                            this,Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)
                    {
                        Constants.showImageChooser(this@UserProfileActivity)
                    } else {
                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {
                    // TODO Step 3: Uncomment the code and use the image URL global variable to update the image URL to Firestore. Make the necessary changes.

                    if (validateUserProfileDetails()) {

                        // TODO Step 12: Make it common for the both cases.
                        // START
                        // Show the progress dialog.
                        showProgressDialog(resources.getString(R.string.please_wait))
                        // END

                        if (mSelectedImageFileUri != null) {

                            FirestoreClass().uploadImageToCloudStorage(
                                this,
                                mSelectedImageFileUri, Constants.USER_PROFILE_IMAGE
                            )
                        } else {
                            updateUserProfileDetails()
                            // END
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@UserProfileActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        GlideLoader(this).loaduserPicture(mSelectedImageFileUri!!,
                        image_view!!)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    /**
     * A function to validate the input entries for profile details.
     */
    private fun validateUserProfileDetails(): Boolean {
        return when {

            // We have kept the user profile picture is optional.
            // The FirstName, LastName, and Email Id are not editable when they come from the login screen.
            // The Radio button for Gender always has the default selected value.

            // Check if the mobile number is not empty as it is mandatory to enter.
            TextUtils.isEmpty(et_mobile_number?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    // TODO Step 7: Create a function to notify the success result and proceed further accordingly.
    // START
    /**
     * A function to notify the success result and proceed further accordingly after updating the user details.
     */
    fun userProfileUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()


        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this, SettingsActivity::class.java))
        finish()
    }
    // END

    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String, Any>()

        val firstName = et_first_name?.text.toString().trim(){it <= ' '}
        if(firstName != mUserDetails.firstName){
            userHashMap[Constants.FIRST_NAME] = firstName
        }

        val lastName = et_last_name?.text.toString().trim(){it <= ' '}
        if(lastName != mUserDetails.lastName){
            userHashMap[Constants.LAST_NAME] = lastName
        }

        val mobileNumber = et_mobile_number?.text.toString().trim(){it <= ' '}
        if(mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        val gender = if(rb_male?.isChecked!!){
            Constants.MALE
        }else{
            Constants.FEMALE
        }
        if(gender.isNotEmpty() && gender != mUserDetails.gender){
            userHashMap[Constants.GENDER] = gender
        }


        if(mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        FirestoreClass().updateUserProfileData(this, userHashMap)

    }

    fun imageUploadSuccess(imageURL: String){
        //hideProgressDialog()



        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }

}