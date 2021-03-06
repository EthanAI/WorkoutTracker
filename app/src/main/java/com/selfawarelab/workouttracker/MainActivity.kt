package com.selfawarelab.workouttracker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import com.selfawarelab.workouttracker.database.Database
import timber.log.Timber

// TODO: multiple icons per day
// TODO: icons kept in Exercise object
// TODO: Backup to cloud
// TODO: Total / streak counter
// TODO: Input sets on the fly during rest period
// TODO: Log past workouts (calendar UI)
// TODO: Time workouts
// TODO: Public domain diagrams
// TODO: +/- Buttons for rep count
// TODO: Tabbed navigtaion
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeApp()

//        Database.instance().clearWorkoutDayData()

//        Api.api.addWorkoutDay(WorkoutDayRequest())
//            .subscribeBy(
//                onSuccess = {
//                    Timber.e("Success")
//                },
//                onError = {
//                    Timber.e("Error")
//                })


        addExerciseSuggestionsIfNone()
        addTargetRestDaysIfNone()
    }

    private fun initializeApp() {
        Timber.plant(Timber.DebugTree())
        Database.instance().initDatabase(applicationContext)
    }

    override fun onSupportNavigateUp() = findNavController(this, R.id.nav_host_fragment).navigateUp()

    private fun addExerciseSuggestionsIfNone() {
        // A little hacky to make a dated object and hide it in the past, but works for now
        val workoutDayList = Database.instance().loadWorkoutDayData()
        if (workoutDayList.isEmpty() || workoutDayList[0].exerciseList.isEmpty()) {
            val initialData = mutableListOf<WorkoutDay>()

            val exerciseSuggestionList = getInitialExerciseSuggestionList()
            val ancientDay = getTodayStart()
            ancientDay.timeInMillis = 0
            val workoutDay = WorkoutDay(ancientDay, exerciseSuggestionList)

            initialData.add(workoutDay)

            Database.instance().storeWorkoutDayData(initialData)
        }
    }

    private fun addTargetRestDaysIfNone() {
        val map = hashMapOf<ExerciseType.MuscleGroup, Int>()
        for (muscle in ExerciseType.MuscleGroup.values()) {
            map[muscle] = 2
        }
        Database.instance().storeTargetRestData(map)
    }
}