package com.selfawarelab.workouttracker.database

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.selfawarelab.workouttracker.Workout
import com.selfawarelab.workouttracker.WorkoutDay
import java.io.IOException
import java.util.*

internal class WorkoutDayDeserializer private constructor(vc: Class<*>?) : StdDeserializer<WorkoutDay>(vc) {
    constructor() : this(null)

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): WorkoutDay {
        val node = jp.codec.readTree<JsonNode>(jp)

        val workoutNode = node.get("workout")
        val workout = ObjectMapper().treeToValue<Workout>(workoutNode)

        val dayString = node.get("mDay").asLong()
        val day = Calendar.getInstance()
        day.timeInMillis = dayString

        val mDrawable = node.get("mDrawable").asInt()
        val mIsDisabled = node.get("mIsDisabled").asBoolean()

        // Set private fields in the inherited class EventDay
        val workoutDay = WorkoutDay(day, workout, mDrawable)
//        var field = workoutDay::class.java.superclass.getDeclaredField("mDrawable")
//        field.isAccessible = true
//        field.setLong(workoutDay, mDrawable)

        val field = workoutDay::class.java.superclass.getDeclaredField("mIsDisabled")
        field.isAccessible = true
        field.setBoolean(workoutDay, mIsDisabled)

        return workoutDay
    }
}
