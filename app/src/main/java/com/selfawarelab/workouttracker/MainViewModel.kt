package com.selfawarelab.workouttracker

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.applandeo.materialcalendarview.EventDay
import com.selfawarelab.workouttracker.database.Database

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
}