package com.selfawarelab.workouttracker

import com.applandeo.materialcalendarview.EventDay
import com.selfawarelab.workouttracker.Unit.*
import java.util.*

class WorkoutDay(day: Calendar, val workout: Workout, icon: Int) : EventDay(day, icon) {
    constructor() : this(Calendar.getInstance(), Workout(), R.drawable.ic_accessibility_black_24dp)
    constructor(calendar: Calendar) : this(calendar, Workout(), R.drawable.ic_accessibility_black_24dp)
    constructor(workout: Workout) : this(Calendar.getInstance(), workout, R.drawable.ic_accessibility_black_24dp)
    constructor(calendar: Calendar, workout: Workout) : this(calendar, workout, R.drawable.ic_accessibility_black_24dp)

    fun addExercise(exercise: Exercise) {
        workout.exerciseList.add(exercise)
    }

    fun removeExercise(exercise: Exercise) {
        workout.exerciseList.remove(exercise)
    }
}

fun Calendar.getDateString(): String {
    return "${this.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())} ${this.get(Calendar.DAY_OF_MONTH)}, ${this.get(Calendar.YEAR)}"
}

fun getTodayStart(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar
}

// TODO: Make val icon part of exercise so multiple icons can stack up per day
class Workout(val exerciseList: MutableList<Exercise>, val icon: Int) {
    constructor() : this(mutableListOf<Exercise>(), R.drawable.ic_accessibility_black_24dp)
    constructor(exerciseList: MutableList<Exercise>) : this(exerciseList, R.drawable.ic_accessibility_black_24dp)
    constructor(mDrawable: Int) : this(mutableListOf<Exercise>(), mDrawable)

    override fun toString(): String {
        return exerciseList.joinToString { "\n" }
    }
}

class Exercise(val name: String, var weight: Int, val unit: Unit, var reps: Reps) {
    constructor(name: String, weight: Int, unit: Unit, vararg reps: Int) : this(name, weight, unit, Reps(*reps))
    constructor(): this("", 0, LBS, Reps())

    override fun toString(): String {
        return "$name $weight $unit $reps"
    }
}

enum class Unit(val string: String) {
    LBS("lbs."),
    KGS("kgs"),
    MACHINE("sel.")
}

// TODO: Handle different weights per set
class Reps(val sets: MutableList<Int>) {
    constructor(vararg sets: Int) : this(mutableListOf(*sets.toTypedArray()))
    constructor(): this(mutableListOf()) // Jackson deserialization seems to require empty constructors

    override fun toString(): String {
        return sets.fold("") { string: String, int -> string.plus("$int ") }
    }
}

fun getInitialExerciseSuggestionList(): MutableList<Exercise> {
    val exerciseSuggestionList = mutableListOf<Exercise>()

    exerciseSuggestionList.add(Exercise("Curl - Pair", 50, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise("Triceps - Pair",60, LBS, 10, 10, 10 ))
    exerciseSuggestionList.add(Exercise("Pulldown",100, LBS, 10, 10, 10 ))
    exerciseSuggestionList.add(Exercise("Row",110, LBS, 10, 10, 10 ))
    exerciseSuggestionList.add(Exercise("Shrugs",60, LBS, 10, 10, 10 ))
    exerciseSuggestionList.add(Exercise("Benchpress",100, LBS, 10, 10, 10 ))
    exerciseSuggestionList.add(Exercise("Benchpress - incline",90, LBS, 10, 10, 10 ))
    exerciseSuggestionList.add(Exercise("Shoulder lateral dumbbell",10, LBS, 10, 10, 10 ))
    exerciseSuggestionList.add(Exercise("Back Extension",0, LBS, 10, 10, 10 ))
    exerciseSuggestionList.add(Exercise("Squats",0, LBS, 10, 10, 10 ))

    return exerciseSuggestionList
}