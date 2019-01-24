package com.selfawarelab.workouttracker.editor

import android.app.DatePickerDialog
import android.app.Dialog
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
import kotlinx.android.synthetic.main.exercise_creator_dialog.*
import kotlinx.android.synthetic.main.fragment_add.*
import timber.log.Timber
import java.util.*

class EditorFragment : Fragment() {
    private val exerciseSuggestionList = Database.instance().loadworkoutDayData()?.toMutableList()!!
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

        workoutDay = viewModel.getWorkoutDayForDate(EditorFragmentArgs.fromBundle(arguments!!).calendarMills)

        dateDisplay.let {
            it.text = workoutDay.day.getDateString()
            it.setOnClickListener {
                DatePickerDialog(
                    requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val calendar = getTodayStart()
                        calendar.set(year, month, dayOfMonth)
                        workoutDay = viewModel.getWorkoutDayForDate(calendar.timeInMillis)
                        editorAdapter.setDataList(workoutDay)
                        editorAdapter.notifyDataSetChanged()

                        dateDisplay.text = workoutDay.day.getDateString()
                        Timber.e(workoutDay.day.getDateString())
                    },
                    workoutDay.day.get(Calendar.YEAR),
                    workoutDay.day.get(Calendar.MONTH),
                    workoutDay.day.get(Calendar.DAY_OF_MONTH)
                )
                    .show()
            }
        }

        addExercise.setOnClickListener { _ ->
            val newExercise = Exercise.getPlaceholder()
            val dialog = Dialog(view.context)
            dialog.let {
                it.setContentView(R.layout.exercise_creator_dialog)
                it.setTitle("Weight")

                it.name.setText(newExercise.name)
                it.weight.text = newExercise.weight.toString()
                it.unit.text = newExercise.unit.string
                it.reps.text = newExercise.reps.toString()

                val weightClickListener = getWeightOnClickListener(newExercise, editorAdapter)
                it.weight.setOnClickListener(weightClickListener)
                it.unit.setOnClickListener(weightClickListener)

                val repsClickListener = getRepsOnClickListener(newExercise, editorAdapter)
                it.reps.setOnClickListener(repsClickListener)

                it.save.setOnClickListener {
                    workoutDay.addExercise(newExercise)
                    editorAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }

                it.cancel.setOnClickListener {
                    dialog.cancel()
                }
            }

            dialog.show()
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
        editorAdapter.notifyDataSetChanged()

        suggestionAdapter.onClickSubject.subscribeBy(
            onNext = { exercise: Exercise ->
                workoutDay.addExercise(exercise)
                editorAdapter.notifyDataSetChanged()
                Timber.e(workoutDay.workout.toString())
            }
        )
    }
}