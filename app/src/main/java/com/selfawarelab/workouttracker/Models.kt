package com.selfawarelab.workouttracker

import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.selfawarelab.workouttracker.ExerciseType.Companion.BACK_EXTENSION
import com.selfawarelab.workouttracker.ExerciseType.Companion.BENCHPRESS
import com.selfawarelab.workouttracker.ExerciseType.Companion.BENCHPRESS_DB
import com.selfawarelab.workouttracker.ExerciseType.Companion.BENCHPRESS_DB_INCLINE
import com.selfawarelab.workouttracker.ExerciseType.Companion.BENCHPRESS_INCLINE
import com.selfawarelab.workouttracker.ExerciseType.Companion.CURL_DB
import com.selfawarelab.workouttracker.ExerciseType.Companion.CURL_PAIR
import com.selfawarelab.workouttracker.ExerciseType.Companion.LATERAL_RAISE
import com.selfawarelab.workouttracker.ExerciseType.Companion.LAT_PULLDOWN
import com.selfawarelab.workouttracker.ExerciseType.Companion.PULLUP
import com.selfawarelab.workouttracker.ExerciseType.Companion.ROW
import com.selfawarelab.workouttracker.ExerciseType.Companion.SHRUGS
import com.selfawarelab.workouttracker.ExerciseType.Companion.SITUP
import com.selfawarelab.workouttracker.ExerciseType.Companion.SITUP_DECLINE
import com.selfawarelab.workouttracker.ExerciseType.Companion.SQUATS
import com.selfawarelab.workouttracker.ExerciseType.Companion.TRICEP_DB
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

// TODO: Make val icon part of exercise so multiple icons can stack up per day
class Workout(val exerciseList: MutableList<Exercise>, val icon: Int) {
    constructor() : this(mutableListOf<Exercise>(), R.drawable.ic_accessibility_black_24dp)
    constructor(exerciseList: MutableList<Exercise>) : this(exerciseList, R.drawable.ic_accessibility_black_24dp)
    constructor(mDrawable: Int) : this(mutableListOf<Exercise>(), mDrawable)

    override fun toString(): String {
        return exerciseList.joinToString { " " }
    }
}

class Exercise(var type: ExerciseType, val unit: Unit, var setList: MutableList<Set>, var time: Calendar) {
    constructor() : this(ExerciseType(), LBS, mutableListOf(), Calendar.getInstance())

    companion object {
        private fun getPlaceholder(): Exercise {
            return Exercise(ExerciseType(), LBS, getPlaceholderSetList(), time = Calendar.getInstance())
        }

        fun getPlaceholder(day: Calendar): Exercise {
            val exercise = getPlaceholder()
            exercise.time = day
            return exercise
        }

        private fun getPlaceholderSetList(): MutableList<Set> {
            return mutableListOf(Set(50, 10), Set(50, 10), Set(50, 10))
        }

        fun getPlaceholderSetList(weight: Int): MutableList<Set> {
            return mutableListOf(Set(weight, 10), Set(weight, 10), Set(weight, 10))
        }
    }

    fun toSetListString(): String {
        return if (constantWeight(setList)) {
            setList.fold("${setList.first().weight} ${unit.string} x") { string: String, set -> string.plus(" ${set.count}") }
        } else {
            setList.fold("") { string: String, set -> string.plus(" ${set.weight}x${set.count}") }
        }
    }

    private fun constantWeight(setList: MutableList<Set>): Boolean {
        return setList.all { it.weight == setList.first().weight }
    }

//    val averageWeight: Int = if(setList.isEmpty()) 0 else setList.map { it.weight }.average().toInt()

    override fun toString(): String {
        return "$type $setList $time"
    }
}

class Set(var weight: Int, var count: Int) {
    constructor() : this(0, 0)
}


enum class Unit(val string: String) {
    LBS("lbs."),
    KGS("kgs"),
    MACHINE("sel.")
}

fun getInitialExerciseSuggestionList(): MutableList<Exercise> {
    val exerciseSuggestionList = mutableListOf<Exercise>()

    val ancientDay = getTodayStart()
    ancientDay.timeInMillis = 0

    exerciseSuggestionList.add(Exercise(CURL_PAIR, LBS, Exercise.getPlaceholderSetList(50), ancientDay))
    exerciseSuggestionList.add(Exercise(CURL_DB, LBS, Exercise.getPlaceholderSetList(50), ancientDay))
    exerciseSuggestionList.add(Exercise(TRICEP_PUSHDOWN, LBS, Exercise.getPlaceholderSetList(60), ancientDay))
    exerciseSuggestionList.add(Exercise(TRICEP_DB, LBS, Exercise.getPlaceholderSetList(60), ancientDay))
    exerciseSuggestionList.add(Exercise(LAT_PULLDOWN, LBS, Exercise.getPlaceholderSetList(100), ancientDay))
    exerciseSuggestionList.add(Exercise(PULLUP, LBS, Exercise.getPlaceholderSetList(100), ancientDay))
    exerciseSuggestionList.add(Exercise(ROW, LBS, Exercise.getPlaceholderSetList(110), ancientDay))
    exerciseSuggestionList.add(Exercise(SHRUGS, LBS, Exercise.getPlaceholderSetList(60), ancientDay))
    exerciseSuggestionList.add(Exercise(BENCHPRESS, LBS, Exercise.getPlaceholderSetList(100), ancientDay))
    exerciseSuggestionList.add(Exercise(BENCHPRESS_DB, LBS, Exercise.getPlaceholderSetList(100), ancientDay))
    exerciseSuggestionList.add(Exercise(BENCHPRESS_INCLINE, LBS, Exercise.getPlaceholderSetList(90), ancientDay))
    exerciseSuggestionList.add(Exercise(BENCHPRESS_DB_INCLINE, LBS, Exercise.getPlaceholderSetList(90), ancientDay))
    exerciseSuggestionList.add(Exercise(LATERAL_RAISE, LBS, Exercise.getPlaceholderSetList(10), ancientDay))
    exerciseSuggestionList.add(Exercise(BACK_EXTENSION, LBS, Exercise.getPlaceholderSetList(0), ancientDay))
    exerciseSuggestionList.add(Exercise(SQUATS, LBS, Exercise.getPlaceholderSetList(0), ancientDay))
    exerciseSuggestionList.add(Exercise(SITUP, LBS, Exercise.getPlaceholderSetList(0), ancientDay))
    exerciseSuggestionList.add(Exercise(SITUP_DECLINE, LBS, Exercise.getPlaceholderSetList(0), ancientDay))

    return exerciseSuggestionList
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

fun Calendar.ageInDays(): Int {
    val today = getTodayStart()
    return ((today.timeInMillis - getDayStart().timeInMillis) / (24 * 60 * 60 * 1000)).toInt()
}

fun Calendar.getDayStart(): Calendar {
    this.set(Calendar.HOUR, 0)
    this.set(Calendar.MINUTE, 0)
    this.set(Calendar.SECOND, 0)
    this.set(Calendar.MILLISECOND, 0)
    this.set(Calendar.AM_PM, 0)
    return this
}

fun getTodayStart(): Calendar {
    val calendar = Calendar.getInstance()
    return calendar.getDayStart()
}