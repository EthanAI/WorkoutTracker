package com.selfawarelab.workouttracker.database

import android.content.Context
import com.applandeo.materialcalendarview.EventDay
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
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

    fun loadWorkoutDayData(): List<WorkoutDay>? {
        if (!db.exists(workoutDayDataKey)) return listOf()

        val workoutDayDataString = db.get(workoutDayDataKey)
        return mapper.readValue(workoutDayDataString, Array<WorkoutDay>::class.java).toList()
    }

    fun storeWorkoutDayData(workoutDayData: List<WorkoutDay>) {
        val workoutListString = mapper.writeValueAsString(workoutDayData)
        db.put(workoutDayDataKey, workoutListString)
    }
}