package com.selfawarelab.workouttracker.editor

import android.app.Dialog
import android.view.View
import android.widget.NumberPicker
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.R
import com.selfawarelab.workouttracker.Reps
import kotlinx.android.synthetic.main.reps_picker_dialog.*
import kotlinx.android.synthetic.main.weight_picker_dialog.*

// For Weight picking
private val weightIncrement = 5
private val weightIncrementStart = 0
private val weightIncrementCount = 40

private fun weightToPosition(weight: Int) = weightIncrementCount - (weight / weightIncrement)
private fun positionToWeight(position: Int) = (weightIncrementCount - position) * weightIncrement

private fun getWeightValueList(): Array<String> {
    val valueList = mutableListOf<String>()
    for (i in weightIncrementStart..weightIncrementCount) {
        valueList.add("${i * weightIncrement}")
    }
    return valueList.reversed().toTypedArray()
}

fun getWeightOnClickListener(exercise: Exercise, adapter: EditorAdapter): View.OnClickListener {
    return View.OnClickListener { view: View ->
        val dialog = Dialog(view.context)
        dialog.setContentView(R.layout.weight_picker_dialog)

        val numberPicker = dialog.weight_number_picker.apply {
            this.displayedValues = getWeightValueList()
            this.minValue = 0
            this.maxValue = weightIncrementCount
            this.value = weightToPosition(exercise.weight)
            this.wrapSelectorWheel = false
        }

        dialog.setTitle("Weight")
        dialog.saveWeight.setOnClickListener {
            exercise.weight = positionToWeight(numberPicker.value)
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        dialog.cancelWeight.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }
}

// For rep picking
private val repMin = 1
private val repMax = 20

private fun repToPosition(rep: Int) = repMax - rep + repMin
private fun positionToRep(position: Int) = repMax - position + repMin

private fun getRepValueList(): Array<String> {
    val valueList = mutableListOf<String>()
    for (i in repMin..repMax) {
        valueList.add("$i")
    }
    return valueList.reversed().toTypedArray()
}

fun getRepsOnClickListener(
    exercise: Exercise,
    adapter: EditorAdapter
): View.OnClickListener {
    return View.OnClickListener { view: View ->
        val dialog = Dialog(view.context)
        dialog.setContentView(R.layout.reps_picker_dialog)
        dialog.setTitle("Reps")

        val repPickers = mutableListOf<NumberPicker>() // Hold all the pickers we need for this
        // Add a number picker for each rep entry
        for (set in exercise.reps.sets) {
            addRepPicker(dialog, repPickers, set)
        }

        dialog.addSet.setOnClickListener { addRepPicker(dialog, repPickers) }
        dialog.deleteSet.setOnClickListener { removeRepPicker(dialog, repPickers) }

        dialog.saveReps.setOnClickListener {
            val newSets = repPickers.fold(mutableListOf<Int>()) { sets, repPicker ->
                sets.add(positionToRep(repPicker.value))
                sets
            }

            exercise.reps = Reps(newSets)
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        dialog.cancelReps.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }
}

fun addRepPicker(dialog: Dialog, repPickers: MutableList<NumberPicker>, startValue: Int = 10) {
    if (repPickers.size == 5) return

    NumberPicker(dialog.context).apply {
        this.displayedValues = getRepValueList()
        this.minValue = 1
        this.maxValue = repMax
        this.value = repToPosition(startValue)
        this.wrapSelectorWheel = false

        repPickers.add(this)
        val endPosition = dialog.pickerLayout.childCount - 1
        dialog.pickerLayout.addView(this, endPosition)
    }
}

private fun removeRepPicker(dialog: Dialog, repPickers: MutableList<NumberPicker>) {
    if (repPickers.size == 1) return

    val lastPickerIndex = dialog.pickerLayout.childCount - 2
    dialog.pickerLayout.removeViewAt(lastPickerIndex)
    repPickers.removeAt(repPickers.lastIndex)
}
