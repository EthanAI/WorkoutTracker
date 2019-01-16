package com.selfawarelab.workouttracker.editor

import android.app.Dialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.R
import com.selfawarelab.workouttracker.Reps
import com.selfawarelab.workouttracker.WorkoutDay
import kotlinx.android.synthetic.main.item_workoutday.view.*
import kotlinx.android.synthetic.main.reps_picker_dialog.*
import kotlinx.android.synthetic.main.weight_picker_dialog.*

class EditorAdapter : RecyclerView.Adapter<EditorAdapter.EditorViewHolder>() {
    var data: WorkoutDay? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_editor, parent, false)
        return EditorViewHolder(view)
    }

    fun setDataList(newData: WorkoutDay) {
        data = newData
    }

    override fun getItemCount(): Int {
        return if (data == null) 0
        else data?.workout?.exerciseList!!.size
    }

    override fun onBindViewHolder(holder: EditorViewHolder, position: Int) {
        val exercise = data?.workout!!.exerciseList[position]
        holder.bindData(exercise)
    }

    inner class EditorViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindData(exercise: Exercise) {
            itemView.let { it ->
                it.name.text = exercise.name
                it.weight.text = exercise.weight.toString()
                it.unit.text = exercise.unit
                it.reps.text = exercise.reps.toString()

                it.setOnLongClickListener {
                    data?.workout?.exerciseList?.removeAt(adapterPosition)
                    notifyDataSetChanged()
                    true
                }

                val weightClickListener = View.OnClickListener { view: View ->
                    val dialog = Dialog(it.context)
                    dialog.setContentView(R.layout.weight_picker_dialog)

                    val numberPicker = dialog.weight_number_picker
                    numberPicker.value = exercise.weight
                    numberPicker.minValue = 5
                    numberPicker.maxValue = 20
                    numberPicker.wrapSelectorWheel = false

                    dialog.setTitle("Weight")
                    dialog.saveWeight.setOnClickListener {
                        exercise.weight = numberPicker.value
                        notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    dialog.cancelWeight.setOnClickListener {
                        dialog.cancel()
                    }

                    dialog.show()
                }

                it.weight.setOnClickListener(weightClickListener)
                it.unit.setOnClickListener(weightClickListener)


                it.reps.setOnClickListener {
                    val dialog = Dialog(it.context)
                    dialog.setContentView(R.layout.reps_picker_dialog)

                    // Add a number picker for each rep entry
                    val repPickers = mutableListOf<NumberPicker>()
                    for (set in exercise.reps.sets) {
                        val numberPicker = NumberPicker(it.context)
                        numberPicker.value = exercise.weight
                        numberPicker.minValue = 5
                        numberPicker.maxValue = 20
                        numberPicker.wrapSelectorWheel = false

                        repPickers.add(numberPicker)
                        dialog.pickerLayout.addView(numberPicker)
                    }


                    dialog.setTitle("Reps")
                    dialog.saveReps.setOnClickListener {
                        val newSets = repPickers.fold(mutableListOf<Int>()) { sets, repPicker ->
                            sets.add(repPicker.value)
                            sets
                        }

                        exercise.reps = Reps(newSets)
                        notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    dialog.cancelReps.setOnClickListener {
                        dialog.cancel()
                    }

                    dialog.show()
                }
            }
        }
    }
}