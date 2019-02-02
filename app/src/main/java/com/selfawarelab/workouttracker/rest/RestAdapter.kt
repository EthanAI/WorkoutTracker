package com.selfawarelab.workouttracker.rest

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.ExerciseType
import com.selfawarelab.workouttracker.R
import com.selfawarelab.workouttracker.ageInDays
import kotlinx.android.synthetic.main.item_rest.view.*

class RestAdapter() : RecyclerView.Adapter<RestAdapter.ViewHolder>() {
    lateinit var data: List<Pair<ExerciseType.MuscleGroup, Pair<Exercise?, Int>?>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rest, parent, false)
        return ViewHolder(view)
    }

    fun setDataList(newData: List<Pair<ExerciseType.MuscleGroup, Pair<Exercise?, Int>?>>) {
        data = newData
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bindData(entry: Pair<ExerciseType.MuscleGroup, Pair<Exercise?, Int>?>) {
            itemView.let {
                it.muscle.text = entry.first.name
                it.age.text = entry.second?.first?.time?.ageInDays().toString()
                it.targetAge.text = entry.second?.second.toString()

                if(entry.second?.first?.time?.ageInDays()!! > entry.second?.second!! ) {
                    it.age.setTextColor(Color.RED)
                }
            }
        }
    }
}