package com.example.myreminder

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myreminder.Models.ScheduleModel
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class ScheduleDetail : AppCompatActivity() {

    private lateinit var tvScheduleName: TextView
    private lateinit var tvScheduleDetail: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_detail)

        initView()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("id").toString(),
                intent.getStringExtra("name").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord()
        }

        setValuesToViews()
    }

    // delete function
    private fun deleteRecord() {
        val id = intent.getStringExtra("id").toString()
        val dbRef = FirebaseDatabase.getInstance("https://login-register-98a53-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Schedules")
            .child(id)

        dbRef.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Schedule data deleted", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to delete schedule data", Toast.LENGTH_LONG).show()
            }
        }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun initView() {
        tvScheduleName = findViewById(R.id.scheduleNameInfo)
        tvScheduleDetail = findViewById(R.id.scheduleDetailInfo)
        tvDate = findViewById(R.id.scheduleDateInfo)
        tvTime = findViewById(R.id.scheduleTimeInfo)
        btnUpdate = findViewById(R.id.updateBtn)
        btnDelete = findViewById(R.id.deleteBtn)
    }

    private fun setValuesToViews() {
        tvScheduleName.text = intent.getStringExtra("name")
        tvScheduleDetail.text = intent.getStringExtra("detail")
        tvDate.text = intent.getStringExtra("date")
        tvTime.text = intent.getStringExtra("time")
    }

    // open edit diaolog
    private fun openUpdateDialog(
        id: String,
        name: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etScheduleName = mDialogView.findViewById<EditText>(R.id.etScheduleName)
        val etScheduleDetail = mDialogView.findViewById<EditText>(R.id.etDetail)
        val etDate = mDialogView.findViewById<EditText>(R.id.etDate)
        val etTime = mDialogView.findViewById<EditText>(R.id.etTime)
        val btnDate = mDialogView.findViewById<Button>(R.id.dateBtn)
        val btnTime = mDialogView.findViewById<Button>(R.id.timeBtn)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.saveUpdateBtn)

        etScheduleName.setText(intent.getStringExtra("name").toString())
        etScheduleDetail.setText(intent.getStringExtra("detail").toString())
        etDate.setText(intent.getStringExtra("date").toString())
        etTime.setText(intent.getStringExtra("time").toString())

        mDialog.setTitle("Editing $name Data")

        btnDate.setOnClickListener {
            showDatePicker(etDate)
        }

        btnTime.setOnClickListener {
            showTimePicker(etTime)
        }

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateScheduleData(
                id,
                etScheduleName.text.toString(),
                etScheduleDetail.text.toString(),
                etDate.text.toString(),
                etTime.text.toString()
            )

            Toast.makeText(applicationContext, "Schedule Data Updated", Toast.LENGTH_LONG).show()

            // setting updated data to TextView
            tvScheduleName.text = etScheduleName.text.toString()
            tvScheduleDetail.text = etScheduleDetail.text.toString()
            tvDate.text = etDate.text.toString()
            tvTime.text = etTime.text.toString()

            alertDialog.dismiss()
        }
    }

    // date picker
    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                editText.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    // timepicker
    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val selectedTime = formatTime(selectedHour, selectedMinute)
                editText.setText(selectedTime)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return String.format("%02d:%02d", hour, minute)
    }

    private fun updateScheduleData(
        id: String,
        name: String,
        detail: String,
        date: String,
        time: String
    ) {
        val dbRef =
            FirebaseDatabase.getInstance("https://login-register-98a53-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Schedules").child(id)

        val scheduleInfo = ScheduleModel(id, name, detail, date, time)
        dbRef.setValue(scheduleInfo).addOnCompleteListener { updateTask ->
            if (updateTask.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "Schedule Data Updated",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Failed to update schedule data",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
