package com.selfawarelab.workouttracker

import com.selfawarelab.workouttracker.ExerciseType.MuscleGroup.*

class ExerciseType(val name: String, vararg val muscles: MuscleGroup, extraOffset: Boolean = false) {
    val id = generateId(muscles[0], extraOffset)

    constructor() : this("", BICEPS)

    init {
        for (muscle in muscles) {
            val listOfThisType = exerciseMap[muscle]
            listOfThisType!!.add(this)
            exerciseMap[muscle] = listOfThisType
        }
    }

    companion object {
        val exerciseMap = hashMapOf<MuscleGroup, MutableList<ExerciseType>>().let {
            it[BICEPS] = mutableListOf()
            it[TRICEPS] = mutableListOf()
            it[SHOULDERS] = mutableListOf()
            it[CHEST] = mutableListOf()
            it[BACK_TRAPS] = mutableListOf()
            it[BACK_LATS] = mutableListOf()
            it[BACK_LUMBAR] = mutableListOf()
            it[ABS] = mutableListOf()
            it[GLUTES] = mutableListOf()
            it[LEGS] = mutableListOf()
            it
        }

        val CURL_PAIR = ExerciseType("Curl - Pair", BICEPS)
        val CURL_DB = ExerciseType("Curl - DB", BICEPS)
        val TRICEP_PUSHDOWN = ExerciseType("Tricep Pushdown", TRICEPS)
        val TRICEP_DB = ExerciseType("Tricep DB", TRICEPS)
        val LAT_PULLDOWN = ExerciseType("Lat Pulldown", BACK_LATS)
        val PULLUP = ExerciseType("Pullup", BACK_LATS, BICEPS)
        val ROW = ExerciseType("Row", BACK_TRAPS)
        val SHRUGS = ExerciseType("Shrugs", BACK_TRAPS)
        val BENCHPRESS = ExerciseType("Benchpress", CHEST)
        val BENCHPRESS_DB = ExerciseType("Benchpress - DB", CHEST)
        val BENCHPRESS_INCLINE = ExerciseType("Benchpress - Incline", CHEST)
        val BENCHPRESS_DB_INCLINE = ExerciseType("Benchpress - DB Incline", CHEST)
        val LATERAL_RAISE = ExerciseType("Lateral Raise", SHOULDERS)
        val SITUP = ExerciseType("Situp", ABS)
        val SITUP_DECLINE = ExerciseType("Situp decline", ABS)
        val BACK_EXTENSION = ExerciseType("Back Extension", BACK_LUMBAR, GLUTES)
        val SQUATS = ExerciseType("Squats", LEGS, GLUTES)
    }

    private fun generateId(muscle: MuscleGroup, extraOffset: Boolean): Int {
        var offset = exerciseMap[muscle]?.size ?: 0
        if(extraOffset) offset += 50
        return muscle.ordinal * 100 + offset
    }

    enum class MuscleGroup {
        BICEPS,
        TRICEPS,
        SHOULDERS,
        CHEST,
        BACK_TRAPS,
        BACK_LATS,
        BACK_LUMBAR,
        ABS,
        GLUTES,
        LEGS
    }

    override fun toString(): String {
        return name
    }
}