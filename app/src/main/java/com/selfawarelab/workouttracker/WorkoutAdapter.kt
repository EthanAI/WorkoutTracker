package com.selfawarelab.workouttracker

import android.support.v7.widget.RecyclerView
import android.system.Os.bind
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_workoutday.view.*

class WorkoutAdapter() : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    var data: WorkoutDay? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workoutday, parent, false)
        return WorkoutViewHolder(view)
    }

    fun setDataList(newData: WorkoutDay) {
        data = newData
    }

    override fun getItemCount(): Int {
        return if(data == null) 0
        else data?.workout?.exerciseList!!.size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val exercise = data?.workout!!.exerciseList[position]
        holder.bindData(exercise)
    }

    class WorkoutViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindData(exercise: Exercise) {
            itemView.itemText.text = exercise.toString()
        }

    }
}