package com.selfawarelab.workouttracker.editor

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.R
import com.selfawarelab.workouttracker.WorkoutDay
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_exercise.view.*

// TODO: Maybe have it go by entire Workout. Then you can copy a bunch over and modify. Whichever is the least amount of corrections required
class SuggestionAdapter : RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder>() {
    private var data: List<Exercise>? = null
    val onClickSubject: PublishSubject<Exercise> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return SuggestionViewHolder(view)
    }

    fun setDataList(dataSource: List<Exercise>) {
        data = dataSource
    }

    override fun getItemCount(): Int {
        return if (data == null) 0
        else data!!.size
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val exercise = data!![position]
        holder.bindData(exercise)
    }

    inner class SuggestionViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindData(exercise: Exercise) {
            itemView.let {
                it.name.text = exercise.type.name
                it.sets.text = exercise.toSetListString()

                it.setOnClickListener {
                    onClickSubject.onNext(exercise);
                }
            }
        }

    }
}