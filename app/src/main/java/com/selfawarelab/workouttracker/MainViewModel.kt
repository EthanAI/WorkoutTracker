package com.selfawarelab.workouttracker

import android.arch.lifecycle.ViewModel
import com.selfawarelab.workouttracker.database.Database
import java.util.*

class MainViewModel : ViewModel() {
    val workoutDayList = mutableListOf<WorkoutDay>()

    fun addWorkoutDay(workoutDay: WorkoutDay) {
        val existingWorkoutDay = findExistingWorkoutDayByDate(workoutDay.day.timeInMillis)
        if(existingWorkoutDay == null) {
            workoutDayList.add(workoutDay)
            Database.instance().storeworkoutDayData(workoutDayList.toList())
        }
    }

    fun loadWorkoutListFromDb() {
        workoutDayList.clear()
        workoutDayList.addAll(Database.instance().loadworkoutDayData()?.toMutableList()!!)
    }

    private fun findExistingWorkoutDayByDate(timeInMills: Long): WorkoutDay? {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMills

        return workoutDayList.find { it.eventDay.calendar == calendar }
    }

    fun getWorkoutDayForDate(timeInMills: Long): WorkoutDay {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMills

        return findExistingWorkoutDayByDate(timeInMills) ?: WorkoutDay(calendar)
    }
}