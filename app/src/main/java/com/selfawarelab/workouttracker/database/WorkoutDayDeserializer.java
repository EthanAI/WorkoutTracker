package com.selfawarelab.workouttracker.database;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.selfawarelab.workouttracker.Workout;
import com.selfawarelab.workouttracker.WorkoutDay;

import java.io.IOException;
import java.util.Calendar;

class WorkoutDayDeserializer extends StdDeserializer<WorkoutDay> {

    public WorkoutDayDeserializer() {
        this(null);
    }

    private WorkoutDayDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public WorkoutDay deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

//        Workout workout = node.get("workout");  // TODO: Understand more about jackson deserializing objects .writeObjectField
        long dayString = node.get("dayString").asLong();
        Calendar day = Calendar.getInstance();
        day.setTimeInMillis(dayString);
        int mDrawable = node.get("mDrawable").asInt();
        boolean mIsDisabled = !node.get("mIsDisabled").asBoolean(); // TODO: figure out how to do this. Default jackson is accessing these so...

        return new WorkoutDay(day, new Workout(mDrawable));
    }
}
