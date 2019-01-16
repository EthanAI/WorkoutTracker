package com.selfawarelab.workouttracker

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.arch.lifecycle.ViewModelProviders
import com.selfawarelab.workouttracker.MainViewModel.SelectedFragment.*
import com.selfawarelab.workouttracker.editor.EditorFragment
import timber.log.Timber

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
}
