package com.selfawarelab.workouttracker

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber
import java.util.*


class MainFragment : Fragment() {
    private val adapter = WorkoutAdapter()
    private var clickedDay: Calendar = getTodayStart()

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rest.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_restFragment)
        }

        launchEditorButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToEditorFragment(clickedDay.timeInMillis)
            findNavController().navigate(action)
        }

        calendarView.setOnDayClickListener { eventDay ->
            clickedDay = eventDay.calendar

            updateList()
        }

        workoutRV.layoutManager = LinearLayoutManager(context)
        workoutRV.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadWorkoutListFromDb()
        viewModel.loadTargetRestDataFromDb()
        streakHeader.text = "Workout Days in a row: ${viewModel.getWorkoutDayStreak()}"
        calendarView.setEvents(viewModel.workoutDayList)
        Timber.e("workoutDayList: ${viewModel.workoutDayList.size}")
    }

    private fun updateList() {
        val workoutDay = viewModel.getWorkoutDayForDate(clickedDay.timeInMillis)
        adapter.setDataList(workoutDay)
        adapter.notifyDataSetChanged()

        Timber.e("eventDay ${clickedDay.get(Calendar.YEAR)} ${clickedDay.get(Calendar.MONTH)} ${clickedDay.get(Calendar.DAY_OF_MONTH)} }")
    }
}

