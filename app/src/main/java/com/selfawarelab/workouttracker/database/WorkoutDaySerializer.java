package com.selfawarelab.workouttracker.database;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.selfawarelab.workouttracker.WorkoutDay;

import java.io.IOException;

public class WorkoutDaySerializer extends StdSerializer<WorkoutDay> {
    public WorkoutDaySerializer() {
        this(null);
    }

    private WorkoutDaySerializer(Class<WorkoutDay> t) {
        super(t);
    }

    @Override
    public void serialize(WorkoutDay value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();

        jgen.writeObjectField("workout", value.getWorkout());
        jgen.writeObjectField("dayString", value.getDay());
        jgen.writeNumberField("mDrawable", value.getWorkout().getIcon());
        jgen.writeBooleanField("mIsDisabled", !value.isEnabled());

        jgen.writeEndObject();
    }
}
