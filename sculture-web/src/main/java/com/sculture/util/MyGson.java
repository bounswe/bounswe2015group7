package com.sculture.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by safa on 15/12/15.
 */
public class MyGson {
    public static Gson create() {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class, new DateTimeSerializer())
                .registerTypeHierarchyAdapter(Date.class, new DateTimeDeserializer())
                .create();
    }

    private static class DateTimeSerializer implements JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

    private static class DateTimeDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Date(Long.valueOf((json).getAsString()));
        }
    }
}