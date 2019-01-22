package com.selfawarelab.workouttracker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.selfawarelab.workouttracker.database.Database
import timber.log.Timber
import java.util.*

/*
    Icons: Exercise categories:
    Arms
    Chest
    Back
    Legs
    Shoulders
    Core: abs, lower back
 */
// TODO: multiple icons per day
// TODO: icons kept in Exercise object
// TODO: Exercise adder fragment
// TODO: Backup to cloud
// TODO: Possible weight changes per set
// TODO: Total / streak counter
// TODO: Input sets on the fly during rest period
// TODO: Log past workouts (text)
// TODO: Log past workouts (calendar UI)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())
        Database.instance().initDatabase(applicationContext)

        addExerciseSuggestionsIfNone()
    }

    override fun onSupportNavigateUp() = findNavController(this, R.id.nav_host_fragment).navigateUp()

    private fun addExerciseSuggestionsIfNone() {
        // A little hacky to make a dated object and hide it in the past, but works for now
        val calendarData = Database.instance().loadCalendarData()
        if (calendarData == null || calendarData.isEmpty() || calendarData[0].workout.exerciseList.isEmpty()) {
            val initialData = mutableListOf<WorkoutDay>()

            val exerciseSuggestionList = getInitialExerciseSuggestionList()
            val ancientDay = Calendar.getInstance()
            ancientDay.timeInMillis = 0
            val workout = Workout(exerciseSuggestionList)
            val workoutDay = WorkoutDay(ancientDay, workout)

            initialData.add(workoutDay)

            Database.instance().storeCalendarData(initialData)
        }
    }
}