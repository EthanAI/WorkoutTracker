package com.selfawarelab.workouttracker

import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.selfawarelab.workouttracker.Unit.*
import java.util.*

// TODO: make getters and setters to make duplicate fields protected
class WorkoutDay(val workout: Workout, val day: Calendar, var icon: Int, var isEnabled: Boolean) { // Inheritance sucks
    var eventDay: EventDay = EventDay(day, icon) // Easier to recreate from scratch than modify via reflection

    constructor() : this(Workout(), getTodayStart(), R.drawable.ic_accessibility_black_24dp, true)
    constructor(calendar: Calendar) : this(Workout(), calendar, R.drawable.ic_accessibility_black_24dp, true)
    constructor(workout: Workout) : this(workout, getTodayStart(), R.drawable.ic_accessibility_black_24dp, true)
    constructor(calendar: Calendar, workout: Workout) : this(workout, calendar, R.drawable.ic_accessibility_black_24dp, true)
    constructor(workout: Workout, day: Calendar, icon: Int) : this(workout, day, icon, true)

    fun addExercise(exercise: Exercise) {
        workout.exerciseList.add(exercise)
        if(workout.exerciseList.size == 1) {
            icon = R.drawable.ic_accessibility_black_24dp
            updateEventDay()
        }
    }

    fun removeExercise(exercise: Exercise) {
        workout.exerciseList.remove(exercise)
        if(workout.exerciseList.isEmpty()) {
            icon = R.drawable.navigation_empty_icon
            updateEventDay()
        }
    }

    private fun updateEventDay(day: Calendar = this.day, icon: Int = this.icon) {
        eventDay = EventDay(day, icon)
    }
}

fun CalendarView.setEvents(workoutDayList: MutableList<WorkoutDay>) {
    val calendarData =  workoutDayList.fold(mutableListOf<EventDay>()) { calendarData, workoutDay ->
        calendarData.add(workoutDay.eventDay)
        calendarData
    }
    this.setEvents(calendarData)
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
    calendar.set(Calendar.AM_PM, 0)
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

class Exercise(var name: String, var weight: Int, var unit: Unit, var reps: Reps) {
    constructor(name: String, weight: Int, unit: Unit, vararg reps: Int) : this(name, weight, unit, Reps(*reps))
    constructor(): this("", 0, LBS, Reps())

    companion object {
        fun getPlaceholder(): Exercise {
            return Exercise("Exercise Name", 50, LBS, 10, 10, 10)
        }
    }

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