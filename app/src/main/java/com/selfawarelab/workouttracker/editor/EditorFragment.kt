package com.selfawarelab.workouttracker.editor

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.selfawarelab.workouttracker.*
import com.selfawarelab.workouttracker.database.Database
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_add.*
import timber.log.Timber
import java.util.*

class EditorFragment : Fragment() {
    private val exerciseSuggestionList = Database.instance().loadCalendarData()?.toMutableList()!!
    private lateinit var workoutDay: WorkoutDay
    private val suggestionAdapter = SuggestionAdapter()
    private val editorAdapter = EditorAdapter()

    private val viewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutDay = viewModel.getWorkoutDay(EditorFragmentArgs.fromBundle(arguments!!).calendarMills)

        dateDisplay.let {
            it.text = workoutDay.calendar.getDateString()
            it.setOnClickListener {
                DatePickerDialog(
                    requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth)
                        workoutDay = viewModel.getWorkoutDay(calendar.timeInMillis)

                        dateDisplay.text = workoutDay.calendar.getDateString()
                        Timber.e(workoutDay.calendar.getDateString())
                    },
                    workoutDay.calendar.get(Calendar.YEAR),
                    workoutDay.calendar.get(Calendar.MONTH),
                    workoutDay.calendar.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }
        }

        dateDisplay.let {
            it.text = workoutDay.calendar.getDateString()
            it.setOnClickListener {
                DatePickerDialog(
                    requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        workoutDay.calendar.set(year, month, dayOfMonth)
                        dateDisplay.text = workoutDay.calendar.getDateString()
                        Timber.e(workoutDay.calendar.getDateString())
                    },
                    workoutDay.calendar.get(Calendar.YEAR),
                    workoutDay.calendar.get(Calendar.MONTH),
                    workoutDay.calendar.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }
        }

        addExercise.setOnClickListener {
        }

        submit.setOnClickListener {
            Timber.e("${editorAdapter.workoutDay?.workout?.exerciseList?.size}")
            viewModel.addWorkoutDay(workoutDay)
            findNavController().popBackStack()
        }

        suggestionRV.layoutManager = LinearLayoutManager(context)
        suggestionRV.adapter = suggestionAdapter
        suggestionAdapter.setDataList(exerciseSuggestionList)


        addingRV.layoutManager = LinearLayoutManager(context)
        addingRV.adapter = editorAdapter
        editorAdapter.setDataList(workoutDay)

        suggestionAdapter.onClickSubject.subscribeBy(
            onNext = { exercise: Exercise ->
                workoutDay.addExercise(exercise)
                editorAdapter.notifyDataSetChanged()
                Timber.e(workoutDay.workout.toString())
            }
        )
    }
}