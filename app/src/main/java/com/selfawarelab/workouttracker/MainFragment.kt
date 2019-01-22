package com.selfawarelab.workouttracker

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.selfawarelab.workouttracker.database.Database
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

        viewModel.loadWorkoutListFromDb()
        calendarView.setEvents(viewModel.calendarData)
        Timber.e("calendarData: ${viewModel.calendarData.size}")

        upload.setOnClickListener {
            Database.instance().clearCalendarData()
        }

        launchEditorButton.setOnClickListener {
//            Timber.e("Selected day: ${calendarView.selectedDates[0].get(Calendar.DAY_OF_MONTH)}")
//            val action = MainFragmentDirections.actionMainFragmentToEditorFragment(calendarView.selectedDates[0].timeInMillis)
            val action = MainFragmentDirections.actionMainFragmentToEditorFragment(clickedDay.timeInMillis)
            findNavController().navigate(action)
        }

//        val y1 = Calendar.getInstance()
//        y1.set(Calendar.DAY_OF_MONTH, 18)
//
//        val y2 = Calendar.getInstance()
//        y2.set(Calendar.DAY_OF_MONTH, 17)
//
//        val y3 = Calendar.getInstance()
//        y3.set(Calendar.DAY_OF_MONTH, 16)
//
//        calendarView.setDate(y1)
//        calendarView.setDisabledDays(listOf(y2))
//        calendarView.selectedDates = listOf(y3)



        calendarView.setOnDayClickListener { eventDay ->
            clickedDay = eventDay.calendar

            if (eventDay !is WorkoutDay) {
                Timber.e("Not WorkoutDay")
                adapter.clearData()
                adapter.notifyDataSetChanged()

//                val yesterday = Calendar.getInstance()
//                yesterday.set(Calendar.DAY_OF_MONTH, 18)

//                calendarView.setDisabledDays(mutableListOf(eventDay.calendar))
            } else {
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