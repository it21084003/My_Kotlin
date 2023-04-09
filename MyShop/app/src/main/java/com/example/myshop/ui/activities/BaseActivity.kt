package com.example.myshop.ui.activities

import android.app.Dialog
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myshop.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog
    private var doublueBackToExitPressedOnce = false

    //Error Message

    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackBarView = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val view = snackBarView.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP


        if(errorMessage){
            view.setBackgroundColor(
                ContextCompat.getColor(this,R.color.colorSnackBarError)
            )
        }else{
            view.setBackgroundColor(
                ContextCompat.getColor(this,R.color.colorSnackBarSuccess)
            )
        }

        snackBarView.show()
    }

    /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource.
               The resource will be inflated, adding all top-level views to the screen.*/
        mProgressDialog.setContentView(R.layout.dialog_progress)

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }


    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }
    // END

    //
    fun doubleBackToExit(){
        if(doublueBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doublueBackToExitPressedOnce = true

        Toast.makeText(
            this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECTION")
        (android.os.Handler()).postDelayed({doublueBackToExitPressedOnce = false},
        2000)
    }
}