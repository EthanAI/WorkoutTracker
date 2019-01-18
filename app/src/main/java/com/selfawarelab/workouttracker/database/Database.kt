package com.selfawarelab.workouttracker.database

import android.content.Context
import com.applandeo.materialcalendarview.EventDay
import com.snappydb.DB
import com.snappydb.DBFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.selfawarelab.workouttracker.WorkoutDay


class Database {
    private lateinit var db: DB

    // TODO: this is black magic
    private val mapper = ObjectMapper().apply {
        val module = SimpleModule()

        module.addSerializer(WorkoutDay::class.java, WorkoutDaySerializer())
        module.addDeserializer(WorkoutDay::class.java, WorkoutDayDeserializer())
        this.registerModule(module)


//        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//        this.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY))
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
        if(!db.exists(calendarDataKey)) return listOf()

        val calendarDataString = db.get(calendarDataKey)

        return mapper.readValue(calendarDataString, Array<WorkoutDay>::class.java).toList()
    }

    fun storeCalendarData(calendarData: List<EventDay>) {
        val workoutListString = mapper.writeValueAsString(calendarData)
        db.put(calendarDataKey, workoutListString)
    }

//    private
//    fun safeGet(key: String): String? {
//        return try {
//            db.exists(key) then { db.get(key) }
//        } catch (e: SnappydbException) {
//            Timber.e(e, "Database Error ->")
//            null
//        }
//    }

//    private
//    fun safePut(key: String, obj: String) {
//        try {
//            db.put(key, obj)
//        } catch (e: SnappydbException) {
//            Timber.e(e, "Database Error ->")
//        }
//    }
}