package com.selfawarelab.workouttracker

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_exercise.view.*

class WorkoutAdapter() : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {
    var data: WorkoutDay? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return WorkoutViewHolder(view)
    }

    fun setDataList(newData: WorkoutDay) {
        data = newData
    }

    fun clearData() {
        data = null
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
            itemView.let {
                it.name.text = exercise.name
                it.weight.text = exercise.weight.toString()
                it.unit.text = exercise.unit.string
                it.reps.text = exercise.reps.toString()
            }
        }

    }
}