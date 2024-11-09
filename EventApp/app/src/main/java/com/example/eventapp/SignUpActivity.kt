package com.example.eventapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.eventapp.R

import java.util.Calendar

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signUpButton = findViewById<Button>(R.id.btn_sign_up)
        val editDob = findViewById<EditText>(R.id.edit_dob)
        val calendarIcon = findViewById<ImageView>(R.id.calendar_icon)




        val showDatePicker = {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    editDob.setText(date)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        editDob.setOnClickListener { showDatePicker() }
        calendarIcon.setOnClickListener { showDatePicker() }
    }
}
