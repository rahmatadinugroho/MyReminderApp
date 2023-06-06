package com.example.myreminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myreminder.Models.ScheduleModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class AddScheduleActivity : AppCompatActivity() {

    private lateinit var layoutScheduleName: TextInputLayout
    private lateinit var editScheduleName: TextInputEditText
    private lateinit var layoutScheduleDetail: TextInputLayout
    private lateinit var editScheduleDetail: TextInputEditText
    private lateinit var layoutDatePicker: TextInputLayout
    private lateinit var editDatePicker: TextInputEditText
    private lateinit var layoutTimePicker: TextInputLayout
    private lateinit var editTimePicker: TextInputEditText
    private lateinit var btnDatePicker: Button
    private lateinit var btnTimePicker: Button
    private lateinit var saveBtn: Button




    // Firebase initialization
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_schedule)

        dbRef = FirebaseDatabase.getInstance("https://login-register-98a53-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Schedules")


        layoutScheduleName = findViewById(R.id.schedule_name)
        editScheduleName = findViewById(R.id.edt_schedule_name)
        layoutScheduleDetail = findViewById(R.id.schedule_detail)
        editScheduleDetail = findViewById(R.id.edt_schedule_detail)
        layoutDatePicker = findViewById(R.id.schedule_date)
        editDatePicker = findViewById(R.id.edt_schedule_date)
        layoutTimePicker = findViewById(R.id.schedule_time)
        editTimePicker = findViewById(R.id.edt_schedule_time)
        btnDatePicker = findViewById(R.id.btndatepick)
        btnTimePicker = findViewById(R.id.btntimepick)
        saveBtn = findViewById(R.id.btnsave)

        saveBtn.setOnClickListener {
            saveScheduleData()
        }

        // date Picker function
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setDateToTextInputEditText(year, month, dayOfMonth)
        }

        btnDatePicker.setOnClickListener {
            DatePickerDialog(
                this,
                datePicker,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // time Picker function
        val timePickerListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            setTimeToTextInputEditText(hourOfDay, minute)
        }

        btnTimePicker.setOnClickListener {
            val currentTime = Calendar.getInstance()
            val hour = currentTime.get(Calendar.HOUR_OF_DAY)
            val minute = currentTime.get(Calendar.MINUTE)

            TimePickerDialog(
                this,
                timePickerListener,
                hour,
                minute,
                true
            ).show()
        }
    }

    private fun setDateToTextInputEditText(year: Int, month: Int, day: Int) {
        val selectedDate = "${day}/${month + 1}/${year}"
        editDatePicker.setText(selectedDate)
    }

    private fun setTimeToTextInputEditText(hour: Int, minute: Int) {
        val selectedTime = String.format("%02d:%02d", hour, minute)
        editTimePicker.setText(selectedTime)
    }

    // save schedule function
    private fun saveScheduleData() {
        val scheduleName = editScheduleName.text.toString()
        val scheduleDetail = editScheduleDetail.text.toString()
        val scheduleDate = editDatePicker.text.toString()
        val scheduleTime = editTimePicker.text.toString()

        if (scheduleName.isEmpty()) {
            layoutScheduleName.error = "Please enter schedule name"
            return
        }
        if (scheduleDetail.isEmpty()) {
            layoutScheduleDetail.error = "Please enter schedule detail"
            return
        }
        if (scheduleDate.isEmpty()) {
            layoutDatePicker.error = "Please pick a date"
            return
        }
        if (scheduleTime.isEmpty()) {
            layoutTimePicker.error = "Please choose a time"
            return
        }

        val scheduleId = dbRef.push().key!!

        val schedule =
            ScheduleModel(scheduleId, scheduleName, scheduleDetail, scheduleDate, scheduleTime)

        dbRef.child(scheduleId).setValue(schedule)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Schedule Data Saved Successfully", Toast.LENGTH_LONG)
                        .show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Failed to save schedule data", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}


