package com.selfawarelab.workouttracker

import android.arch.lifecycle.ViewModel
import com.selfawarelab.workouttracker.database.Database

class MainViewModel : ViewModel() {
    val workoutDayList = mutableListOf<WorkoutDay>()
    val recoveryDays = HashMap<ExerciseType.MuscleGroup, Int>().apply {
        for(muscle in ExerciseType.MuscleGroup.values()) {
            this[muscle] = 0
        }
    }

    // TODO: get current date and update recoveryDays to be from this day
    fun addWorkoutDay(workoutDay: WorkoutDay) {
        val existingWorkoutDay = findExistingWorkoutDayByDate(workoutDay.day.timeInMillis)
        if (existingWorkoutDay == null) {
            workoutDayList.add(workoutDay)
        }
        Database.instance().storeWorkoutDayData(workoutDayList.toList()) // Write possible changes even if quantity is unchanged
    }

    fun loadWorkoutListFromDb() {
        workoutDayList.clear()
        workoutDayList.addAll(Database.instance().loadWorkoutDayData().toMutableList())
    }

    private fun findExistingWorkoutDayByDate(timeInMills: Long): WorkoutDay? {
        val calendar = getTodayStart()
        calendar.timeInMillis = timeInMills

        return workoutDayList.find { it.eventDay.calendar == calendar }
    }

    fun getWorkoutDayForDate(timeInMills: Long): WorkoutDay {
        val calendar = getTodayStart()
        calendar.timeInMillis = timeInMills

        return findExistingWorkoutDayByDate(timeInMills) ?: WorkoutDay(calendar)
    }
}