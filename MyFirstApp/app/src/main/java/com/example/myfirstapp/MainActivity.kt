package com.example.myfirstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnClickme = findViewById<Button>(R.id.mybutton)
        val tvMyTextView = findViewById<TextView>(R.id.textView)
        val btnMinus = findViewById<Button>(R.id.minusbtn)
        var timesClicked = 0

        btnClickme.setOnClickListener{
            timesClicked++
            tvMyTextView.text = timesClicked.toString()
            Toast.makeText(this,"Hey Antt Min ++",Toast.LENGTH_SHORT).show()
        }
        btnMinus.setOnClickListener{
            timesClicked--
            tvMyTextView.text = timesClicked.toString()
            Toast.makeText(this,"Hey Antt Min --",Toast.LENGTH_LONG).show()

        }
    }


}