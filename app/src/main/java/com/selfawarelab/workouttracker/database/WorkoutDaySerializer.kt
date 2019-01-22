package com.selfawarelab.workouttracker.database

import com.applandeo.materialcalendarview.EventDay
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.selfawarelab.workouttracker.WorkoutDay
import java.io.IOException
import java.lang.reflect.AccessibleObject.setAccessible

// Don't even bother to write the EventDay. We are going to reinstantiate it everytime because it's too restrictive with property access
class WorkoutDaySerializer private constructor(t: Class<WorkoutDay>?) : StdSerializer<WorkoutDay>(t) {
    constructor() : this(null)

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: WorkoutDay, jsonGenerator: JsonGenerator, provider: SerializerProvider) {

        jsonGenerator.let {
            it.writeStartObject()
            it.writeObjectField("workout", value.workout)
            it.writeObjectField("day", value.day)
            it.writeNumberField("icon", value.icon)
            it.writeBooleanField("isEnabled", value.isEnabled)
            it.writeEndObject()
        }
    }
}