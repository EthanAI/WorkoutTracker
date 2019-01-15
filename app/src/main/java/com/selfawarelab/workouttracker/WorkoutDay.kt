package com.selfawarelab.workouttracker

import com.applandeo.materialcalendarview.EventDay
import java.util.*

class WorkoutDay(day: Calendar, val workout: Workout): EventDay(day, R.drawable.ic_accessibility_black_24dp) {
    // Defaults icon
    val defaultIcon = R.drawable.ic_accessibility_black_24dp
}

class Workout(val exerciseList: List<Exercise>) {
    override fun toString(): String {
        return exerciseList.joinToString { "\n" }
    }
}

class Exercise(val name: String, val reps: Int) {
    override fun toString(): String {
        return name + " " + reps
    }
}