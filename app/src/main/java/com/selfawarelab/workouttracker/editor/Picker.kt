package com.selfawarelab.workouttracker.editor

import android.app.Dialog
import android.view.View
import android.widget.NumberPicker
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.R
import com.selfawarelab.workouttracker.Set
import kotlinx.android.synthetic.main.sets_picker_dialog.*

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

fun pickerPairToSet(pickerPair: Pair<NumberPicker, NumberPicker>): Set {
    return Set(positionToWeight(pickerPair.first.value), positionToRep(pickerPair.second.value))
}

fun getSetsOnClickListener(exercise: Exercise, adapter: EditorAdapter): View.OnClickListener {
    return View.OnClickListener { view: View ->
        val dialog = Dialog(view.context)
        dialog.setContentView(R.layout.sets_picker_dialog)
        dialog.setTitle("Reps")

        val pickers =
            mutableListOf<Pair<NumberPicker, NumberPicker>>() // Hold all the pickers we need for this (Weight & Rep picker pairs)
        // Add a weight and number picker for each set entry
        for (set in exercise.setList) {
            addPairPicker(dialog, pickers, set)
        }

        dialog.addSet.setOnClickListener { addPairPicker(dialog, pickers, exercise.setList.last()) }
        dialog.deleteSet.setOnClickListener { removeSetPicker(dialog, pickers) }

        dialog.saveReps.setOnClickListener {
            val newSets = pickers.fold(mutableListOf<Set>()) { setList, pickerPair ->
                setList.add(pickerPairToSet(pickerPair))
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

fun addPairPicker(dialog: Dialog, pickers: MutableList<Pair<NumberPicker, NumberPicker>>, set: Set = Set(50, 10)) {
    if (pickers.size == 5) return // Max size

    // TODO: Headers
    // TODO: Alternating colors
    // Build weight Picker
    val weightPicker = NumberPicker(dialog.context).apply {
        this.displayedValues = getWeightValueList()
        this.minValue = 0
        this.maxValue = weightIncrementCount
        this.value = weightToPosition(set.weight)
        this.wrapSelectorWheel = false

        val endPosition = dialog.pickerLayout.childCount - 1
        dialog.pickerLayout.addView(this, endPosition)
    }

    // Build Rep Picker
    val repPicker = NumberPicker(dialog.context).apply {
        this.displayedValues = getRepValueList()
        this.minValue = 1
        this.maxValue = repMax
        this.value = repToPosition(set.count)
        this.wrapSelectorWheel = false

        val endPosition = dialog.pickerLayout.childCount - 1
        dialog.pickerLayout.addView(this, endPosition)
    }
    pickers.add(Pair(weightPicker, repPicker))
}

private fun removeSetPicker(dialog: Dialog, pickers: MutableList<Pair<NumberPicker, NumberPicker>>) {
    if (pickers.size == 1) return // Minimum size

    val lastPickerIndex = dialog.pickerLayout.childCount - 2
    dialog.pickerLayout.removeViewAt(lastPickerIndex)
    pickers.removeAt(pickers.lastIndex)
    pickers.removeAt(pickers.lastIndex - 1)
}
