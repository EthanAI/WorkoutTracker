package com.selfawarelab.workouttracker

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.arch.lifecycle.ViewModelProviders
import com.selfawarelab.workouttracker.MainViewModel.SelectedFragment.*
import com.selfawarelab.workouttracker.database.Database
import com.selfawarelab.workouttracker.editor.EditorFragment
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
class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())
        Database.instance().initDatabase(applicationContext)
        Database.instance().clearCalendarData()

        addExerciseSuggestionsIfNone()

        viewModel.selectedFragment.observe(this, Observer { selectedFragment ->
            when (selectedFragment) {
                CALENDAR -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, MainFragment())
                        .commit()
                }
                EDITOR -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, EditorFragment())
                        .commit()
                }
            }
        })
    }

    private fun addExerciseSuggestionsIfNone() {
        // A little hacky to make a dated object and hide it in the past, but works for now
        val calendarData = Database.instance().loadCalendarData()
        if(calendarData == null || calendarData.isEmpty() || calendarData[0].workout.exerciseList.isEmpty()) {
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