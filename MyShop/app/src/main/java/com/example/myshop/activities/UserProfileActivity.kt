package com.example.myshop.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.provider.MediaStore
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
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

    // Here, the some of the edittext components are disabled because it is added at a time of Registration.
    et_first_name?.isEnabled = false
    et_first_name?.setText(mUserDetails.firstName)

    et_last_name?.isEnabled = false
    et_last_name?.setText(mUserDetails.lastName)

    et_email?.isEnabled = false
    et_email?.setText(mUserDetails.email)

//    image_view?.isEnabled = true
//    image_view?.setImageResource(mUserDetails.image.toInt())


        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.iv_image_view -> {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
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
                                this@UserProfileActivity,
                                mSelectedImageFileUri
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
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    // END

    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String, Any>()
        val mobileNumber = et_mobile_number?.text.toString().trim(){it <= ' '}
        val gender = if(rb_male?.isChecked!!){
            Constants.MALE
        }else{
            Constants.FEMALE
        }
        if(mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }
        if(mobileNumber.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
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