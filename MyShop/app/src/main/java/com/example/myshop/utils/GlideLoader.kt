package com.example.myshop.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myshop.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loaduserPicture(image: Any, imageView: ImageView){
        try{
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_placeholder)
                .into(imageView)
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

}