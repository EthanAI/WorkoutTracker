package com.selfawarelab.workouttracker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.applandeo.materialcalendarview.EventDay
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity() {
    private val events = mutableListOf<EventDay>()
    private val adapter = WorkoutAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())
        Database.instance().initDatabase(applicationContext)
        Database.instance().clearWorkoutList()

        loadFromDB.setOnClickListener {
            getWorkoutListFromDb()
        }

        calendarView.setOnDayClickListener { eventDay ->
            if (eventDay !is WorkoutDay) {
                val clickedDay = eventDay.calendar
                val newEventDay = WorkoutDay(clickedDay, Workout(listOf(Exercise("Row", 10))))
                addWorkout(newEventDay)
                calendarView.setEvents(events)

                return@setOnDayClickListener
            }

            val clickedDay = eventDay.calendar
            val workout = eventDay.workout

            adapter.setDataList(eventDay)
            adapter.notifyDataSetChanged()

            Timber.e(
                "eventDay ${clickedDay.get(Calendar.YEAR)} ${clickedDay.get(Calendar.MONTH)} ${clickedDay.get(Calendar.DAY_OF_MONTH)} $workout"
            )
        }

        workoutRV.layoutManager = LinearLayoutManager(baseContext)
//        adapter.setDataList(events as List<WorkoutDay>)
        workoutRV.adapter = adapter

        val calendar = Calendar.getInstance()

        calendar.set(2019, 0, 12);

        val eventDay = WorkoutDay(calendar, Workout(listOf(Exercise("Row", 10))))
        addWorkout(eventDay)
        calendarView.setEvents(events)

    }

    fun addWorkout(workoutDay: WorkoutDay) {
        events.add(workoutDay)
        Database.instance().storeWorkoutList(events.toList() as List<WorkoutDay>)
    }

    fun getWorkoutListFromDb() {
        events.clear()
        events.addAll(Database.instance().getWorkoutList()?.toMutableList()!!)
    }


}
