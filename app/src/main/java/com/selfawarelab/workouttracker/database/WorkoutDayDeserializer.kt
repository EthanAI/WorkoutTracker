package com.selfawarelab.workouttracker.database

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.selfawarelab.workouttracker.Exercise
import com.selfawarelab.workouttracker.WorkoutDay
import com.selfawarelab.workouttracker.getTodayStart
import java.io.IOException
import java.util.*

internal class WorkoutDayDeserializer private constructor(vc: Class<*>?) : StdDeserializer<WorkoutDay>(vc) {
    constructor() : this(null)

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, ctxt: DeserializationContext): WorkoutDay {
        val node = jsonParser.codec.readTree<JsonNode>(jsonParser)

        val exerciseListNode = node.get("exerciseList")
        val reader = ObjectMapper().readerFor(object : TypeReference<MutableList<Exercise>>() {})
        val exerciseList = reader.readValue<MutableList<Exercise>>(exerciseListNode)

        val dayString = node.get("day").asLong()
        val day = getTodayStart()
        day.timeInMillis = dayString

        val icon = node.get("icon").asInt()

        return WorkoutDay(exerciseList, day, icon)
    }
}
