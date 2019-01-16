package com.selfawarelab.workouttracker

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.applandeo.materialcalendarview.EventDay

class MainViewModel: ViewModel() {
    val selectedFragment = MutableLiveData<SelectedFragment>().apply { value = SelectedFragment.CALENDAR }
    val calendarData = mutableListOf<EventDay>()

    enum class SelectedFragment {
        CALENDAR,
        EDITOR
    }

    fun addWorkoutDay(workoutDay: WorkoutDay) {
        calendarData.add(workoutDay)
        Database.instance().storeCalendarData(calendarData.toList() as List<WorkoutDay>)
    }

    fun loadWorkoutListFromDb() {
        calendarData.clear()
        calendarData.addAll(Database.instance().loadCalendarData()?.toMutableList()!!)
    }
}