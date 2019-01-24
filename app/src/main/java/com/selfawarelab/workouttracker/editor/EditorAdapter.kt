package com.selfawarelab.workouttracker.editor

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.R
import com.selfawarelab.workouttracker.WorkoutDay
import kotlinx.android.synthetic.main.item_workoutday.view.*

class EditorAdapter : RecyclerView.Adapter<EditorAdapter.EditorViewHolder>() {
    var workoutDay: WorkoutDay? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_editor, parent, false)
        return EditorViewHolder(view)
    }

    fun setDataList(newData: WorkoutDay) {
        workoutDay = newData
    }

    override fun getItemCount(): Int {
        return if (workoutDay == null) 0
        else workoutDay?.workout?.exerciseList!!.size
    }

    override fun onBindViewHolder(holder: EditorViewHolder, position: Int) {
        val exercise = workoutDay?.workout!!.exerciseList[position]
        holder.bindData(exercise)
    }

    inner class EditorViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindData(exercise: Exercise) {
            itemView.let { it ->
                it.name.text = exercise.name
                it.weight.text = exercise.weight.toString()
                it.unit.text = exercise.unit.string
                it.reps.text = exercise.reps.toString()

                // Allow item deletion
                it.setOnLongClickListener {
                    workoutDay?.removeExercise(exercise)
                    notifyDataSetChanged()
                    true
                }

                val weightClickListener = getWeightOnClickListener(exercise, this@EditorAdapter)
                it.weight.setOnClickListener(weightClickListener)
                it.unit.setOnClickListener(weightClickListener)

                val repsClickListener = getRepsOnClickListener(exercise, this@EditorAdapter)
                it.reps.setOnClickListener(repsClickListener)
            }
        }
    }
}

