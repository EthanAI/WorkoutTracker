package com.selfawarelab.workouttracker

import android.arch.lifecycle.ViewModel
import com.applandeo.materialcalendarview.EventDay
import com.selfawarelab.workouttracker.database.Database
import java.util.*

class MainViewModel : ViewModel() {
    val calendarData = mutableListOf<EventDay>()

    fun addWorkoutDay(workoutDay: WorkoutDay) {
        calendarData.add(workoutDay)
        Database.instance().storeCalendarData(calendarData.toList() as List<WorkoutDay>)
    }

    fun loadWorkoutListFromDb() {
        calendarData.clear()
        calendarData.addAll(Database.instance().loadCalendarData()?.toMutableList()!!)
    }

    fun getWorkoutDay(timeInMills: Long): WorkoutDay {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMills

        val foundWorkoutDay = calendarData.find { it.calendar == calendar } ?: return WorkoutDay(calendar)
        return foundWorkoutDay as WorkoutDay
    }
}