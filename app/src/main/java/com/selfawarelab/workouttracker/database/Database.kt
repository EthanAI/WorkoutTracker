package com.selfawarelab.workouttracker.database

import android.content.Context
import com.applandeo.materialcalendarview.EventDay
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.selfawarelab.workouttracker.WorkoutDay
import com.snappydb.DB
import com.snappydb.DBFactory


class Database {
    private lateinit var db: DB

    private val mapper = ObjectMapper().apply {
        val module = SimpleModule()

        module.addSerializer(WorkoutDay::class.java, WorkoutDaySerializer())
//        module.addDeserializer(WorkoutDay::class.java, WorkoutDayDeserializer())
        this.registerModule(module)

        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    private val calendarDataKey = "calendarDataKey"

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

    fun clearCalendarData() {
        db.del(calendarDataKey)
    }

    fun loadCalendarData(): List<WorkoutDay>? {
        if (!db.exists(calendarDataKey)) return listOf()

        val calendarDataString = db.get(calendarDataKey)

        return mapper.readValue(calendarDataString, Array<WorkoutDay>::class.java).toList()
    }

    fun storeCalendarData(calendarData: List<EventDay>) {
        val workoutListString = mapper.writeValueAsString(calendarData)
        db.put(calendarDataKey, workoutListString)
    }
}