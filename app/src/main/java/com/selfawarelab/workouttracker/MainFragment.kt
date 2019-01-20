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

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadWorkoutListFromDb()
        calendarView.setEvents(viewModel.calendarData)
        Timber.e("calendarData: ${viewModel.calendarData.size}")


        launchEditorButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_editorFragment)
        }

        calendarView.setOnDayClickListener { eventDay ->
            if (eventDay !is WorkoutDay) {
                Timber.e("Not WorkoutDay")
            } else {
                val clickedDay = eventDay.calendar
                val workout = eventDay.workout

                adapter.setDataList(eventDay)
                adapter.notifyDataSetChanged()

                Timber.e("eventDay ${clickedDay.get(Calendar.YEAR)} ${clickedDay.get(Calendar.MONTH)} ${clickedDay.get(Calendar.DAY_OF_MONTH)} $workout"
                )
            }
        }

        workoutRV.layoutManager = LinearLayoutManager(context)
        workoutRV.adapter = adapter
    }
}