package com.selfawarelab.workouttracker.database

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.selfawarelab.workouttracker.Workout
import com.selfawarelab.workouttracker.WorkoutDay
import java.io.IOException
import java.util.*

// Obsolete but kept for reference
internal class WorkoutDayDeserializer private constructor(vc: Class<*>?) : StdDeserializer<WorkoutDay>(vc) {
    constructor() : this(null)

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext): WorkoutDay {
        val node = jp.codec.readTree<JsonNode>(jp)

        //        Workout workout = node.get("workout")  // TODO: Understand more about jackson deserializing objects .writeObjectField
        val dayString = node.get("dayString").asLong()
        val day = Calendar.getInstance()
        day.timeInMillis = dayString
        val mDrawable = node.get("mDrawable").asInt()
        val mIsDisabled = !node.get("mIsDisabled").asBoolean() // TODO: figure out how to do this. Default jackson is accessing these so...

        return WorkoutDay(day, Workout(mDrawable))
    }
}