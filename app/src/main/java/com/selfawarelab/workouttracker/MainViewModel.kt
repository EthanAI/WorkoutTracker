package com.selfawarelab.workouttracker

import android.arch.lifecycle.ViewModel
import com.selfawarelab.workouttracker.database.Database
import java.util.*


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

    fun getUniqueRecentExercises(): List<Exercise> {
        workoutDayList.sortByDescending { it.day.timeInMillis } // Newest First // TODO: This is expensive with a long history keep in order another way

        val el = workoutDayList.flatMap { it.exerciseList }
        // Collect only the most recent instance of each exercise
        val exerciseMap = el
            .fold(HashMap()) { hashMap: HashMap<Int, Exercise>, exercise ->
                if (!hashMap.containsKey(exercise.type.id))
                    hashMap[exercise.type.id] = exercise
                hashMap
            }
        // Sort alphabetically
        return exerciseMap.values.toList().sortedBy { it.type.name }
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


    fun getWorkoutDayStreak(): Int {
        var streakCount = 0

        val checkDay = getTodayStart()
        var checkWorkoutDay = getWorkoutDayForDate(checkDay.timeInMillis)
        while(checkWorkoutDay.exerciseList.isNotEmpty()) {
            streakCount++
            checkDay.add(Calendar.DATE, -1)
            checkWorkoutDay = getWorkoutDayForDate(checkDay.timeInMillis)
        }

        return streakCount
    }

    fun getRestData(): List<Pair<ExerciseType.MuscleGroup, Pair<Exercise?, Int>?>> {
        val map = hashMapOf<ExerciseType.MuscleGroup, Pair<Exercise?, Int>?>().apply {
            for (muscle in ExerciseType.MuscleGroup.values()) {
                this[muscle] = null
            }
        }

        val exerciseList = workoutDayList
            .flatMap { workoutDay -> workoutDay.exerciseList }
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