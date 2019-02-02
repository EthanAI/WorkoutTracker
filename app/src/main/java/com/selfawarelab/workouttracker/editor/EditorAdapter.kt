package com.selfawarelab.workouttracker.editor

import android.app.Dialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.ExerciseType
import com.selfawarelab.workouttracker.R
import com.selfawarelab.workouttracker.WorkoutDay
import kotlinx.android.synthetic.main.exercise_type_dialog.*
import kotlinx.android.synthetic.main.item_exercise.view.*

class EditorAdapter : RecyclerView.Adapter<EditorAdapter.EditorViewHolder>() {
    var workoutDay: WorkoutDay? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
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
                itemView.name.text = exercise.type.name
                itemView.sets.text = exercise.toSetListString()

                // Allow item deletion
                itemView.delete_button.visibility = VISIBLE
                itemView.delete_button.setOnClickListener { view ->
                    android.app.AlertDialog.Builder(view.context)
                        .setTitle("Delete Exercise?")
                        .setMessage(exercise.toString())
                        .setPositiveButton("Delete") { dialog, _ ->
                            workoutDay?.removeExercise(exercise)
                            notifyDataSetChanged()
                            dialog.cancel()
                        }
                        .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                        .show()
                }

                itemView.name.setOnClickListener { view ->
                    val dialog = Dialog(view.context)
                    dialog.let { it ->
                        val muscleGroupList = exercise.type.muscles.toMutableList()

                        it.setContentView(R.layout.exercise_type_dialog)
                        it.setTitle("Exercise Type")
                        it.name_edit_text.append(exercise.type.name)
                        it.name_edit_text.requestFocus()
                        it.muscle_list.text = muscleGroupList.fold("") {output, input ->
                            output.plus(input.name + "\n")
                        }

                        // TODO: Select Muscle type(s)
                        it.muscle_picker.apply {
                            val muscleGroups = ExerciseType.MuscleGroup.values()
                            this.displayedValues = muscleGroups.map { muscleGroup -> muscleGroup.name }.toTypedArray()
                            this.minValue = 0
                            this.maxValue = muscleGroups.size - 1
                            this.wrapSelectorWheel = false
                        }

                        it.add_type.setOnClickListener { _ ->
                            val selectedMuscleGroup = ExerciseType.MuscleGroup.values()[it.muscle_picker.value]
                            muscleGroupList.add(selectedMuscleGroup)
                            it.muscle_list.text = muscleGroupList.fold("") {output, input ->
                                output.plus(input.name + "\n")
                            }
                        }

                        it.saveType.setOnClickListener { _ ->
                            val exerciseType = ExerciseType(it.name_edit_text.text.toString(), *muscleGroupList.toTypedArray(), extraOffset = true)
                            exercise.type = exerciseType
                            this@EditorAdapter.notifyDataSetChanged()
                            it.dismiss()
                        }
                        it.cancelType.setOnClickListener { _ ->
                            it.cancel()
                        }

                        it.show()
                    }
                }

                val setsClickListener = getSetsOnClickListener(exercise, this@EditorAdapter)
                itemView.sets.setOnClickListener(setsClickListener)
            }
        }
    }
}

