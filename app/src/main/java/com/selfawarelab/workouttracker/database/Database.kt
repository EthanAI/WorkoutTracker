package com.selfawarelab.workouttracker.database

import android.content.Context
import com.applandeo.materialcalendarview.EventDay
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.google.gson.reflect.TypeToken
import com.selfawarelab.workouttracker.*
import com.snappydb.DB
import com.snappydb.DBFactory


class Database {
    private lateinit var db: DB

    private val mapper = ObjectMapper().apply {
        val module = SimpleModule()

        module.addSerializer(WorkoutDay::class.java, WorkoutDaySerializer())
        module.addDeserializer(WorkoutDay::class.java, WorkoutDayDeserializer())
        this.registerModule(module)

        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    private val workoutDayDataKey = "workoutDayDataKey"
    private val exerciseTypesDataKey = "exerciseTypesDataKey"
    private val targetRestDayKey = "targetRestDayKey"

    companion object {
        private var instance: Database? = null
        @Synchronized
        fun instance(): Database {
            if (instance == null) instance =
                    Database()
            return instance as Database
        }
    }

    fun initDatabase(applicationContext: Context) {
        db = DBFactory.open(applicationContext)
    }

    fun clearWorkoutDayData() {
        db.del(workoutDayDataKey)
    }

    fun loadWorkoutDayData(): List<WorkoutDay> {
        if (!db.exists(workoutDayDataKey)) return listOf()

        val workoutDayDataString = db.get(workoutDayDataKey)
        return mapper.readValue(workoutDayDataString, Array<WorkoutDay>::class.java).toList()
    }

    fun storeWorkoutDayData(workoutDayData: List<WorkoutDay>) {
        val workoutListString = mapper.writeValueAsString(workoutDayData)
        db.put(workoutDayDataKey, workoutListString)
    }

    fun loadExerciseTypes(): List<ExerciseType> {
        if (!db.exists(exerciseTypesDataKey)) return listOf()

        val jsonString = db.get(exerciseTypesDataKey)
        return mapper.readValue(jsonString, Array<ExerciseType>::class.java).toList()
    }

    fun storeExerciseTypes(exerciseTypeList: List<ExerciseType>) {
        val jsonString = mapper.writeValueAsString(exerciseTypeList)
        db.put(exerciseTypesDataKey, jsonString)
    }

    fun loadTargetRestData(): Map<ExerciseType.MuscleGroup, Int> {
        if (!db.exists(targetRestDayKey)) return hashMapOf()

        val jsonString = db.get(targetRestDayKey)
        val type = object : TypeReference<java.util.HashMap<ExerciseType.MuscleGroup, Int>>() {} // TODO: Figure out this object: thing
        return mapper.readValue(jsonString, type)
    }

    fun storeTargetRestData(map: HashMap<ExerciseType.MuscleGroup, Int>) {
        val jsonString = mapper.writeValueAsString(map)
        db.put(targetRestDayKey, jsonString)
    }
}