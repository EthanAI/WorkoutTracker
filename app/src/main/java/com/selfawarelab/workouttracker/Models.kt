package com.selfawarelab.workouttracker

import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.selfawarelab.workouttracker.ExerciseType.Companion.BACK_EXTENSION
import com.selfawarelab.workouttracker.ExerciseType.Companion.BENCHPRESS
import com.selfawarelab.workouttracker.ExerciseType.Companion.BENCHPRESS_INCLINE
import com.selfawarelab.workouttracker.ExerciseType.Companion.CURL_PAIR
import com.selfawarelab.workouttracker.ExerciseType.Companion.LATERAL_RAISE
import com.selfawarelab.workouttracker.ExerciseType.Companion.LAT_PULLDOWN
import com.selfawarelab.workouttracker.ExerciseType.Companion.ROW
import com.selfawarelab.workouttracker.ExerciseType.Companion.SHRUGS
import com.selfawarelab.workouttracker.ExerciseType.Companion.SQUATS
import com.selfawarelab.workouttracker.ExerciseType.Companion.TRICEP_PUSHDOWN
import com.selfawarelab.workouttracker.Unit.LBS
import java.util.*

// TODO: make getters and setters to make duplicate fields protected
class WorkoutDay(val workout: Workout, val day: Calendar, var icon: Int, var isEnabled: Boolean) { // Inheritance sucks
    var eventDay: EventDay = EventDay(day, icon) // Easier to recreate from scratch than modify via reflection

    constructor() : this(Workout(), getTodayStart(), R.drawable.ic_accessibility_black_24dp, true)
    constructor(calendar: Calendar) : this(Workout(), calendar, R.drawable.ic_accessibility_black_24dp, true)
    constructor(workout: Workout) : this(workout, getTodayStart(), R.drawable.ic_accessibility_black_24dp, true)
    constructor(calendar: Calendar, workout: Workout) : this(
        workout,
        calendar,
        R.drawable.ic_accessibility_black_24dp,
        true
    )

    constructor(workout: Workout, day: Calendar, icon: Int) : this(workout, day, icon, true)

    fun addExercise(exercise: Exercise) {
        workout.exerciseList.add(exercise)
        if (workout.exerciseList.size == 1) {
            icon = R.drawable.ic_accessibility_black_24dp
            updateEventDay()
        }
    }

    fun removeExercise(exercise: Exercise) {
        workout.exerciseList.remove(exercise)
        if (workout.exerciseList.isEmpty()) {
            icon = R.drawable.navigation_empty_icon
            updateEventDay()
        }
    }

    private fun updateEventDay(day: Calendar = this.day, icon: Int = this.icon) {
        eventDay = EventDay(day, icon)
    }
}

fun CalendarView.setEvents(workoutDayList: MutableList<WorkoutDay>) {
    val calendarData = workoutDayList.fold(mutableListOf<EventDay>()) { calendarData, workoutDay ->
        calendarData.add(workoutDay.eventDay)
        calendarData
    }
    this.setEvents(calendarData)
}


fun Calendar.getDateString(): String {
    return "${this.getDisplayName(
        Calendar.MONTH,
        Calendar.LONG,
        Locale.getDefault()
    )} ${this.get(Calendar.DAY_OF_MONTH)}, ${this.get(Calendar.YEAR)}"
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

class Exercise(var type: ExerciseType, var weight: Int, var unit: Unit, var reps: Reps) {
    constructor(type: ExerciseType, weight: Int, unit: Unit, vararg reps: Int) : this(type, weight, unit, Reps(*reps))
    constructor() : this(ExerciseType(), 0, LBS, Reps())

    companion object {
        fun getPlaceholder(): Exercise {
            return Exercise(ExerciseType(), 50, LBS, 10, 10, 10)
        }
    }

    override fun toString(): String {
        return "$type $weight $unit $reps"
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
    constructor() : this(mutableListOf()) // Jackson deserialization seems to require empty constructors

    override fun toString(): String {
        return sets.fold("") { string: String, int -> string.plus("$int ") }
    }
}

fun getInitialExerciseSuggestionList(): MutableList<Exercise> {
    val exerciseSuggestionList = mutableListOf<Exercise>()

    exerciseSuggestionList.add(Exercise(CURL_PAIR, 50, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise(TRICEP_PUSHDOWN, 60, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise(LAT_PULLDOWN, 100, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise(ROW, 110, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise(SHRUGS, 60, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise(BENCHPRESS, 100, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise(BENCHPRESS_INCLINE, 90, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise(LATERAL_RAISE, 10, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise(BACK_EXTENSION, 0, LBS, 10, 10, 10))
    exerciseSuggestionList.add(Exercise(SQUATS, 0, LBS, 10, 10, 10))

    return exerciseSuggestionList
}