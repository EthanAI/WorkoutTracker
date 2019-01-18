package com.selfawarelab.workouttracker.database

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.selfawarelab.workouttracker.WorkoutDay
import java.io.IOException

class WorkoutDaySerializer private constructor(t: Class<WorkoutDay>?) : StdSerializer<WorkoutDay>(t) {
    constructor() : this(null)

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: WorkoutDay, jgen: JsonGenerator, provider: SerializerProvider) {

        jgen.writeStartObject()

        jgen.writeObjectField("workout", value.workout)
        jgen.writeObjectField("dayString", value.day)
        jgen.writeNumberField("mDrawable", value.workout.icon)
        jgen.writeBooleanField("mIsDisabled", !value.isEnabled)

        jgen.writeEndObject()
    }
}