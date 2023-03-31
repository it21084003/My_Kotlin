package com.example.permissionsexample

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

   var btn_submit : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_submit = findViewById(R.id.btnCameraPermission)
        btn_submit?.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"You already have access and file path",Toast.LENGTH_SHORT).show()
            }else{
                //Request Permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ), CAMERA_AND_FILE_LOCATION_PERMISSION_CODE)
            }
        }


    }

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

}