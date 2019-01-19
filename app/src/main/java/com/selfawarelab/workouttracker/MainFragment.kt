package com.selfawarelab.workouttracker

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applandeo.materialcalendarview.EventDay
import com.selfawarelab.workouttracker.MainViewModel.SelectedFragment.EDITOR
import com.selfawarelab.workouttracker.database.Database
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*


class MainFragment : Fragment() {
    private val adapter = WorkoutAdapter()

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        putFakeDataInDB()
//
//        viewModel.loadWorkoutListFromDb()
//        calendarView.setEvents(viewModel.calendarData)
//        Timber.e("calendarData: ${viewModel.calendarData.size}")

        manuallyPopulateCalendar()


        loadFromDB.setOnClickListener {
            //            loadWorkoutListFromDb()
            loadDifferentData()
        }

        launchEditorButton.setOnClickListener {
            viewModel.selectedFragment.value = EDITOR
        }

        calendarView.setOnDayClickListener { eventDay ->
            //            if (eventDay !is WorkoutDay) {
//                val clickedDay = eventDay.calendar
//                val newWorkoutDay = WorkoutDay(clickedDay, placeholderWorkout())
//                viewModel.addWorkoutDay(newWorkoutDay)
//                calendarView.setEvents(viewModel.calendarData)
//
//                return@setOnDayClickListener
//            }

//            val clickedDay = eventDay.calendar
//            val workout = (eventDay as WorkoutDay).workout
//
//            adapter.setDataList(eventDay)
//            adapter.notifyDataSetChanged()
//
//            Timber.e(
//                "eventDay ${clickedDay.get(Calendar.YEAR)} ${clickedDay.get(Calendar.MONTH)} ${clickedDay.get(Calendar.DAY_OF_MONTH)} $workout"
//            )
        }

        workoutRV.layoutManager = LinearLayoutManager(context)
        workoutRV.adapter = adapter
    }

    private fun manuallyPopulateCalendar() {
        val calendarData = mutableListOf<EventDay>()
//        val eventDay = EventDay(Calendar.getInstance(), R.drawable.ic_accessibility_black_24dp)
//        calendarData.add(eventDay)

        // This works
//        val workoutDay = WorkoutDay(Calendar.getInstance(), Workout())
//        calendarData.add(workoutDay)

        val dbInput = mutableListOf<EventDay>()
        val workoutDay = WorkoutDay(Calendar.getInstance(), Workout())
        dbInput.add(workoutDay)
        Database.instance().storeCalendarData(dbInput)
        val dbOutput = Database.instance().loadCalendarData()
        calendarView.setEvents(dbOutput)

//        calendarView.setEvents(calendarData)
    }

    private fun loadDifferentData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun putFakeDataInDB() {
        Database.instance().clearCalendarData()


        val calendarData = mutableListOf<EventDay>()
        val eventDay = EventDay(Calendar.getInstance(), R.drawable.ic_accessibility_black_24dp)
        val workoutDay = WorkoutDay(Calendar.getInstance(), Workout())
        calendarData.add(eventDay)
        calendarData.add(workoutDay)

        Database.instance().storeCalendarData(calendarData)

//        Database.instance().storeCalendarData(placeholderCalendarData())
    }
}