package com.selfawarelab.workouttracker

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.snappydb.DB
import com.snappydb.DBFactory
import com.snappydb.SnappydbException
import timber.log.Timber

class Database {
    lateinit var db: DB
    private val gson: Gson by lazy {
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .create()
    }
    var instance: Database? = null

    private val calendarDataKey = "calendarDataKey"

    companion object {
        private var instance: Database? = null
        @Synchronized
        fun instance(): Database {
            if (instance == null) instance = Database()
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
        return gson.fromJson(calendarDataString, Array<WorkoutDay>::class.java).toList()
    }

    fun storeCalendarData(calendarData: List<WorkoutDay>) {
        val workoutListString = gson.toJson(calendarData.toTypedArray())
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