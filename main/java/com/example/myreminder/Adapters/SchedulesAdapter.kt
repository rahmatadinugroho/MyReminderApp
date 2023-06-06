package com.example.myreminder.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myreminder.Models.ScheduleModel
import com.example.myreminder.R

class SchedulesAdapter(private val scheduleList: List<ScheduleModel>) :
    RecyclerView.Adapter<SchedulesAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(clickListener: onItemClickListener){
        mListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.schedule_list_item, parent, false)
        return ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSchedule = scheduleList[position]
        holder.tvScheduleName.text = currentSchedule.name
        holder.tvScheduleDate.text = currentSchedule.date
        holder.tvScheduleTime.text = currentSchedule.time
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val tvScheduleName: TextView = itemView.findViewById(R.id.tv_ScheduleName)
        val tvScheduleDate: TextView = itemView.findViewById(R.id.tv_DateSchedule)
        val tvScheduleTime: TextView = itemView.findViewById(R.id.tv_TimeSchedule)

        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}