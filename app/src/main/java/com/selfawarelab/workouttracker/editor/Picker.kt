package com.selfawarelab.workouttracker.editor

import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.NumberPicker
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.R
import com.selfawarelab.workouttracker.Set
import kotlinx.android.synthetic.main.sets_picker_dialog.*
import kotlinx.android.synthetic.main.weight_rep_pickers.view.*

// For Weight picking
private const val weightIncrement = 5
private const val weightIncrementStart = 0
private const val weightIncrementCount = 40

private fun weightToPosition(weight: Int) = weightIncrementCount - (weight / weightIncrement)
private fun positionToWeight(position: Int) = (weightIncrementCount - position) * weightIncrement

private fun getWeightValueList(): Array<String> {
    val valueList = mutableListOf<String>()
    for (i in weightIncrementStart..weightIncrementCount) {
        valueList.add("${i * weightIncrement}")
    }
    return valueList.reversed().toTypedArray()
}

// For rep picking
private const val repMin = 1
private const val repMax = 20

private fun repToPosition(rep: Int) = repMax - rep + repMin
private fun positionToRep(position: Int) = repMax - position + repMin

private fun getRepValueList(): Array<String> {
    val valueList = mutableListOf<String>()
    for (i in repMin..repMax) {
        valueList.add("$i")
    }
    return valueList.reversed().toTypedArray()
}

fun pickerPairToSet(pickerLayout: View): Set {
    return Set(positionToWeight(pickerLayout.weight_picker.value), positionToRep(pickerLayout.rep_picker.value))
}

fun getSetsOnClickListener(exercise: Exercise, adapter: EditorAdapter): View.OnClickListener {
    return View.OnClickListener { view: View ->
        val dialog = Dialog(view.context)
        dialog.setContentView(R.layout.sets_picker_dialog)
        dialog.setTitle("Reps")

        val pickerLayoutList = mutableListOf<View>() // Hold all the pickers we need for this (Weight & Rep picker pairs)
        // Add a weight and number picker for each set entry
        for (set in exercise.setList) {
            addPairPicker(dialog, pickerLayoutList, set)
        }

        dialog.addSet.setOnClickListener { addPairPicker(dialog, pickerLayoutList, pickerPairToSet(pickerLayoutList.last())) }
        dialog.deleteSet.setOnClickListener { removeSetPicker(dialog, pickerLayoutList) }

        dialog.saveReps.setOnClickListener {
            val newSets = pickerLayoutList.fold(mutableListOf<Set>()) { setList, pickerLayout ->
                setList.add(pickerPairToSet(pickerLayout))
                setList
            }

            exercise.setList = newSets
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        dialog.cancelReps.setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }
}

fun addPairPicker(dialog: Dialog, pickerLayoutList: MutableList<View>, set: Set = Set(50, 10)) {
    if (pickerLayoutList.size == 5) return // Max size

    // TODO: Headers
    // TODO: Alternating colors
    // Build weight Picker
    val weightRepPickerLayout = LayoutInflater.from(dialog.context).inflate(R.layout.weight_rep_pickers, dialog.pickerLayout, false)

    weightRepPickerLayout.weight_picker.apply {
        this.displayedValues = getWeightValueList()
        this.minValue = 0
        this.maxValue = weightIncrementCount
        this.value = weightToPosition(set.weight)
        this.wrapSelectorWheel = false
    }

    // Build Rep Picker
    weightRepPickerLayout.rep_picker.apply {
        this.displayedValues = getRepValueList()
        this.minValue = 1
        this.maxValue = repMax
        this.value = repToPosition(set.count)
        this.wrapSelectorWheel = false
    }

    if(pickerLayoutList.size % 2 == 0) weightRepPickerLayout.setBackgroundColor(Color.LTGRAY) else weightRepPickerLayout.setBackgroundColor(Color.WHITE)
    val endPosition = dialog.pickerLayout.childCount - 1 // Insert before the buttons
    dialog.pickerLayout.addView(weightRepPickerLayout, endPosition)
    pickerLayoutList.add(weightRepPickerLayout)
}

private fun removeSetPicker(dialog: Dialog, pickerLayoutList: MutableList<View>) {
    if (pickerLayoutList.size == 1) return // Minimum size

    val lastPickerIndex = dialog.pickerLayout.childCount - 2
    dialog.pickerLayout.removeViewAt(lastPickerIndex)
    pickerLayoutList.removeAt(pickerLayoutList.lastIndex)
}
