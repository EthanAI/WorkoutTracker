package com.selfawarelab.workouttracker

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selfawarelab.workouttracker.MainViewModel.SelectedFragment.EDITOR
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.weight_picker_dialog.*
import timber.log.Timber
import java.util.*


class MainFragment: Fragment() {
    private val adapter = WorkoutAdapter()

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        putFakeDataInDB()

        viewModel.loadWorkoutListFromDb()
        calendarView.setEvents(viewModel.calendarData)
        Timber.e("calendarData: ${viewModel.calendarData.size}")

//        loadFromDB.setOnClickListener {
//            loadWorkoutListFromDb()
//        }

        launchEditorButton.setOnClickListener {
            viewModel.selectedFragment.value = EDITOR
        }

        calendarView.setOnDayClickListener { eventDay ->
            if (eventDay !is WorkoutDay) {
                val clickedDay = eventDay.calendar
                val newWorkoutDay = WorkoutDay(clickedDay, placeholderWorkout())
                viewModel.addWorkoutDay(newWorkoutDay)
                calendarView.setEvents(viewModel.calendarData)

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

        workoutRV.layoutManager = LinearLayoutManager(context)
        workoutRV.adapter = adapter

//        numberPicker()
    }

    private fun numberPicker() {
        val d = Dialog(requireContext())
        d.setTitle("Weight")
        d.setContentView(R.layout.weight_picker_dialog)


        d.weight_number_picker.let {
            it.minValue = 5
            it.maxValue = 20
            it.wrapSelectorWheel = false
        }


        d.show()

//        var value = 0
//
//        val ad = AlertDialog.Builder(requireContext())
//        ad.setTitle("Weight")
//        ad.setView(R.layout.weight_picker_dialog)
//        ad.setPositiveButton("Save") { dialog, which ->
//            value = weight_number_picker.value
//            Timber.e("$value")
//
//        }
//
//        val x = ad.create()
//        x.
//
//
//
//        val np = ad.show().weight_number_picker
//        np.minValue = 5
//        np.maxValue = 20
//        np.wrapSelectorWheel = false
    }

    private fun putFakeDataInDB() {
        Database.instance().clearCalendarData()
        Database.instance().storeCalendarData(placeholderCalendarData())
    }


}