package com.selfawarelab.workouttracker

import android.arch.lifecycle.ViewModel
import com.selfawarelab.workouttracker.database.Database

class MainViewModel : ViewModel() {
    val workoutDayList = mutableListOf<WorkoutDay>()
    private var targetRestDays = mapOf<ExerciseType.MuscleGroup, Int>()

    // TODO: get current date and update recoveryDays to be from this day
    fun addWorkoutDay(workoutDay: WorkoutDay) {
        val existingWorkoutDay = findExistingWorkoutDayByDate(workoutDay.day.timeInMillis)
        if (existingWorkoutDay == null) {
            workoutDayList.add(workoutDay)
        }
        Database.instance()
            .storeWorkoutDayData(workoutDayList.toList()) // Write possible changes even if quantity is unchanged
    }

    fun loadWorkoutListFromDb() {
        workoutDayList.clear()
        workoutDayList.addAll(Database.instance().loadWorkoutDayData().toMutableList())
    }

    fun loadTargetRestDataFromDb() {
        targetRestDays = Database.instance().loadTargetRestData()
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

    fun getRestData(): List<Pair<ExerciseType.MuscleGroup, Pair<Exercise?, Int>?>> {
        val map = hashMapOf<ExerciseType.MuscleGroup, Pair<Exercise?, Int>?>().apply {
            for (muscle in ExerciseType.MuscleGroup.values()) {
                this[muscle] = null
            }
        }

        val exerciseList = workoutDayList
            .flatMap { workoutDay -> workoutDay.workout.exerciseList }
        val exerciseListRecent = exerciseList.sortedByDescending { exercise -> exercise.time.timeInMillis }
        exerciseListRecent.forEach { exercise ->
            for (muscle in exercise.type.muscles) {
                val targetRest = targetRestDays[muscle]!!
                if (map[muscle] == null) {
                    map[muscle] = Pair<Exercise?, Int>(exercise, targetRest)
                }
            }
            if (!map.containsValue(null))
                return map.toList().sortedBy { it.first.name }
        }
        return map.toList().sortedBy { it.first.name }
    }
}