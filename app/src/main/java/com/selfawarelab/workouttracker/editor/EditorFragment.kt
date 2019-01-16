package com.selfawarelab.workouttracker.editor

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selfawarelab.workouttracker.*
import kotlinx.android.synthetic.main.fragment_add.*
import com.selfawarelab.workouttracker.MainViewModel.SelectedFragment.*
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class EditorFragment: Fragment() {
    private val suggestionList = Database.instance().loadCalendarData()?.toMutableList()!!
    private val newWorkoutDay = WorkoutDay()
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

        addExercise.setOnClickListener {
        }

        submit.setOnClickListener {
            Timber.e("${editorAdapter.data?.workout?.exerciseList?.size}")
            viewModel.addWorkoutDay(newWorkoutDay)
            viewModel.selectedFragment.value = CALENDAR
        }

        suggestionRV.layoutManager = LinearLayoutManager(context)
        suggestionRV.adapter = suggestionAdapter
        suggestionAdapter.setDataList(suggestionList)


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