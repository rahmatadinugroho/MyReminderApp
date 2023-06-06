package com.example.myreminder


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminder.Adapters.SchedulesAdapter
import com.example.myreminder.Models.ScheduleModel
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var scheduleList: MutableList<ScheduleModel>
    private lateinit var dbRef: DatabaseReference
    private lateinit var mAdapter: SchedulesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scheduleRecyclerView = findViewById(R.id.rv_Schedules)
        scheduleRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        scheduleRecyclerView.setHasFixedSize(true)

        scheduleList = mutableListOf()
        mAdapter = SchedulesAdapter(scheduleList)
        scheduleRecyclerView.adapter = mAdapter

        val addBtn = findViewById<Button>(R.id.add_btn)
        addBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, AddScheduleActivity::class.java)
            startActivity(intent)
        }

        getScheduleListData()
    }

    private fun getScheduleListData() {
        dbRef = FirebaseDatabase.getInstance("https://login-register-98a53-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Schedules")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                scheduleList.clear()
                if (snapshot.exists()) {
                    for (scheduleSnap in snapshot.children) {
                        val scheduleData = scheduleSnap.getValue(ScheduleModel::class.java)
                        scheduleData?.let {
                            scheduleList.add(it)
                        }
                    }
                    mAdapter.notifyDataSetChanged()

                    mAdapter.setOnItemClickListener(object : SchedulesAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@MainActivity, ScheduleDetail::class.java)

                            // put extras
                            intent.putExtra("name", scheduleList[position].name)
                            intent.putExtra("detail", scheduleList[position].detail)
                            intent.putExtra("date", scheduleList[position].date)
                            intent.putExtra("time", scheduleList[position].time)
                            startActivity(intent)
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani pembatalan bila diperlukan
            }
        })
    }
}


