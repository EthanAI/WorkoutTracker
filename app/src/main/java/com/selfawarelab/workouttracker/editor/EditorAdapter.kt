package com.selfawarelab.workouttracker.editor

import android.app.Dialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.R
import com.selfawarelab.workouttracker.WorkoutDay
import kotlinx.android.synthetic.main.exercise_name_dialog.*
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
            itemView.let { itemView ->
                itemView.name.text = exercise.name
                itemView.weight.text = exercise.weight.toString()
                itemView.unit.text = exercise.unit.string
                itemView.reps.text = exercise.reps.toString()

                // Allow item deletion
                itemView.setOnLongClickListener {
                    workoutDay?.removeExercise(exercise)
                    notifyDataSetChanged()
                    true
                }

                itemView.name.setOnClickListener { view ->
                    val dialog = Dialog(view.context)
                    dialog.let { it ->
                        it.setContentView(R.layout.exercise_name_dialog)
                        it.setTitle("Exercise Name")
                        it.name_edit_text.append(exercise.name)
                        it.saveName.setOnClickListener { _ ->
                            exercise.name = it.name_edit_text.text.toString()
                            this@EditorAdapter.notifyDataSetChanged()
                            it.dismiss()
                        }
                        it.cancelName.setOnClickListener { _ ->
                            it.cancel()
                        }

                        it.show()
                    }
                }

                val weightClickListener = getWeightOnClickListener(exercise, this@EditorAdapter)
                itemView.weight.setOnClickListener(weightClickListener)
                itemView.unit.setOnClickListener(weightClickListener)

                val repsClickListener = getRepsOnClickListener(exercise, this@EditorAdapter)
                itemView.reps.setOnClickListener(repsClickListener)
            }
        }
    }
}

