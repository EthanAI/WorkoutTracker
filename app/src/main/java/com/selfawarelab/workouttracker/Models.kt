package com.selfawarelab.workouttracker

import com.applandeo.materialcalendarview.EventDay
import java.util.*
import com.selfawarelab.workouttracker.Unit.LBS
import com.selfawarelab.workouttracker.Unit.MACHINE

class WorkoutDay(val day: Calendar, val workout: Workout) : EventDay(day, R.drawable.ic_accessibility_black_24dp) {
    constructor() : this(Calendar.getInstance(), Workout())
    constructor(workout: Workout) : this(Calendar.getInstance(), workout)

    // Defaults icon
    val defaultIcon = R.drawable.ic_accessibility_black_24dp

    fun getDateString(): String {
        return "${day.get(Calendar.YEAR)} ${day.get(Calendar.MONTH)} ${day.get(Calendar.DAY_OF_MONTH)}"
    }
}

fun placeholderCalendarData(): List<WorkoutDay> = listOf(WorkoutDay(placeholderWorkout()))

class Workout(val exerciseList: MutableList<Exercise>) {
    constructor() : this(mutableListOf<Exercise>())

    override fun toString(): String {
        return exerciseList.joinToString { "\n" }
    }
}

fun placeholderWorkout(): Workout = Workout(
    mutableListOf(
        Exercise("Row", 50, LBS, Reps(10, 10, 8)),
        Exercise("Curl", 25, LBS, Reps(12, 10, 8)),
        Exercise("Bench Press", 10, MACHINE, Reps(12, 12, 5))
    )
)

// TODO: Handle category icons
class Exercise(val name: String, var weight: Int, val unit: Unit, var reps: Reps) {
    override fun toString(): String {
        return "$name $weight $unit $reps"
    }
}

enum class Unit(val string: String) {
    LBS("lbs."),
    MACHINE("sel.")
}

// TODO: Handle different weights per set
class Reps() {
    lateinit var sets: MutableList<Int>

    constructor(vararg sets: Int) : this() {
        this.sets = sets.asList().toMutableList()
    }

    constructor(sets: List<Int>) : this() {
        this.sets = sets.toMutableList()
    }

    override fun toString(): String {
        return sets.fold("") { string: String, int -> string.plus("$int ") }
    }
}