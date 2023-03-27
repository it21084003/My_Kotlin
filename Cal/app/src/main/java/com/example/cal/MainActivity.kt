package com.example.cal

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var tvSelectedDate : TextView? = null
    private var tvAgeInMinutes : TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnDatePicker : Button = findViewById(R.id.btnDatePicker)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvAgeInMinutes = findViewById(R.id.tvAgeInMinutes)

        btnDatePicker.setOnClickListener {

            clickDatePicker()
            }
        }

    private fun clickDatePicker(){
        val myCalender = Calendar.getInstance()
        val year = myCalender.get(Calendar.YEAR)
        val month = myCalender.get(Calendar.MONTH)
        val day = myCalender.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener{ view ,selectedYear, selectedMonth, selectedDayOfMonth ->

                val selectedDate = "$selectedDayOfMonth/${selectedMonth+1}/$selectedYear"
                tvSelectedDate?.text = selectedDate

                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

                val theDate = sdf.parse(selectedDate)
                theDate?.let {
                    val selectedDateinMinutes = theDate.time/ 60000

                    val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                    currentDate?.let{
                        val currentDateInMinutes = currentDate.time/ 60000

                        val differentInMinutes = currentDateInMinutes - selectedDateinMinutes

                        tvAgeInMinutes?.text = differentInMinutes.toString()
                    }

                }


                val nowAge = year - selectedYear
                if(nowAge > 30){
                    Toast.makeText(this,
                        "စောက်ပေါလုပ်မနေနဲ့ အသက်ပဲ $nowAge ရှိနေပြီး", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this,

                        "Year : $nowAge Month : ${selectedMonth+1} Day : $selectedDayOfMonth", Toast.LENGTH_SHORT).show()

                }

            },
            year,
            month,
            day)

        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dpd.show()

    }
}
