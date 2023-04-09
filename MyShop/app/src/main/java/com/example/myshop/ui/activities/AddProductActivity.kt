package com.example.myshop.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.example.myshop.firestore.FirestoreClass
import com.example.myshop.models.Product
import com.example.myshop.utils.Constants
import com.example.myshop.utils.GlideLoader
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedImageFileUri: Uri? = null
    private var mProductImageURL: String = ""
//    private var mUserProfileImageURL: String = ""
    private var btn_submit : Button? = null
    private var iv_add_update_product: ImageView? = null
    private var image_view : ImageView? = null
    private var et_product_title : TextView? = null
    private var et_product_price : TextView? = null
    private var et_product_description : TextView? = null
    private var et_product_quantity : TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        image_view = findViewById(R.id.iv_product_image)
        iv_add_update_product = findViewById(R.id.iv_add_update_product)
        et_product_title =findViewById(R.id.et_product_title)
        et_product_price =findViewById(R.id.et_product_price)
        et_product_description =findViewById(R.id.et_product_description)
        et_product_quantity =findViewById(R.id.et_product_quantity)
        btn_submit =findViewById(R.id.btn_submit)

        btn_submit?.setOnClickListener(this)
        iv_add_update_product?.setOnClickListener(this)

        backBtn()


    }



    fun backBtn(){
        //back click
        val back_btn : View
        back_btn = findViewById(R.id.back_button)
        back_btn.setOnClickListener{
            onBackPressed()
            //startActivity(Intent(this, ProductsFragment::class.java))
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when(v.id){
                R.id.iv_add_update_product -> {
                    if (ContextCompat.checkSelfPermission(
                            this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)
                    {
                        Constants.showImageChooser(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit ->{
                    if(validateUserProfileDetails()){
                        uploadProductImage()
                    }
                }
            }
        }
    }

    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri, Constants.PRODUCT_IMAGE)
    }

    fun productUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.product_uploades_success_message),
            Toast.LENGTH_LONG
        ).show()
        finish()
       // startActivity(Intent(this, ProductsFragment::class.java))
    }

    fun imageUploadSuccess(imageURL: String){
//        hideProgressDialog()
//        showErrorSnackBar("Product image is uploaded successfuklly $imageURL", true)
//
        mProductImageURL = imageURL
        uploadProductDetails()
    }

    private fun uploadProductDetails(){
        val username = this.getSharedPreferences(Constants.MYSHOP_PREFERENCES,
        Context.MODE_PRIVATE).getString(Constants.LOGGED_IN_USERNAME,"")!!

        val product = Product(

            FirestoreClass().getCurrentuserID(),
            username,
            et_product_title?.text.toString().trim(){it <= ' '},
            et_product_price?.text.toString().trim(){it <= ' '},
            et_product_description?.text.toString().trim(){it <= ' '},
        et_product_quantity?.text.toString().trim(){it <= ' '},
            mProductImageURL,
//            FirestoreClass().getCurrentProductID()
        )

        FirestoreClass().uploadProductDetails(this, product)
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
                Constants.showImageChooser(this)
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
                    image_view?.setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.ic_vector_edit))

                    mSelectedImageFileUri = data.data!!

                    try{
                        GlideLoader(this).loaduserPicture(mSelectedImageFileUri!!,image_view!!)
                    }
                    catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this,
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

    private fun validateUserProfileDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
                false
            }

            TextUtils.isEmpty(et_product_title?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
                false
            }
            TextUtils.isEmpty(et_product_price?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
                false
            }
            TextUtils.isEmpty(et_product_description?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_description), true)
                false
            }
            TextUtils.isEmpty(et_product_quantity?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_quantity), true)
                false
            }else -> {
                true
            }
        }
    }
}