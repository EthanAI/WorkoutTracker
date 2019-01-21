package com.selfawarelab.workouttracker.editor

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
    private lateinit var newWorkoutDay: WorkoutDay
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

        val calendarMills = EditorFragmentArgs.fromBundle(arguments!!).calendarMills
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = calendarMills
        newWorkoutDay = WorkoutDay(calendar, Workout(), R.drawable.ic_accessibility_black_24dp)

        addExercise.setOnClickListener {
        }

        submit.setOnClickListener {
            Timber.e("${editorAdapter.data?.workout?.exerciseList?.size}")
            viewModel.addWorkoutDay(newWorkoutDay)
            findNavController().popBackStack()
        }

        suggestionRV.layoutManager = LinearLayoutManager(context)
        suggestionRV.adapter = suggestionAdapter
        suggestionAdapter.setDataList(exerciseSuggestionList)


        addingRV.layoutManager = LinearLayoutManager(context)
        addingRV.adapter = editorAdapter
        editorAdapter.setDataList(newWorkoutDay)

        suggestionAdapter.onClickSubject.subscribeBy(
            onNext = { exercise: Exercise ->
                newWorkoutDay.workout.exerciseList.add(exercise)
                editorAdapter.notifyDataSetChanged()
                Timber.e(newWorkoutDay.workout.toString())
            }
        )
    }
}