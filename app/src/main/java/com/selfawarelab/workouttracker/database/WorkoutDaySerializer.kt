package com.selfawarelab.workouttracker.database

import com.applandeo.materialcalendarview.EventDay
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.selfawarelab.workouttracker.WorkoutDay
import java.io.IOException
import java.lang.reflect.AccessibleObject.setAccessible



// TODO: remove duplicate storage of values
class WorkoutDaySerializer private constructor(t: Class<WorkoutDay>?) : StdSerializer<WorkoutDay>(t) {
    constructor() : this(null)

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(value: WorkoutDay, jgen: JsonGenerator, provider: SerializerProvider) {

        jgen.writeStartObject()

        jgen.writeObjectField("workout", value.workout)

        val field = value::class.java.superclass.getDeclaredField("mDay")
        field.isAccessible = true
        val mDay = field.get(value)
        jgen.writeObjectField("mDay", mDay)

        jgen.writeNumberField("mDrawable", value.workout.icon)
        jgen.writeBooleanField("mIsDisabled", !value.isEnabled)

        jgen.writeEndObject()
    }
}