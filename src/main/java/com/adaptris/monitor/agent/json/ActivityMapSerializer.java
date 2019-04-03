package com.adaptris.monitor.agent.json;

import com.adaptris.monitor.agent.activity.ActivityMap;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ActivityMapSerializer implements JsonSerializer<ActivityMap> {

    private Gson gson;

    @Override
    public JsonElement serialize(ActivityMap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        for(String key : src.getAdapters().keySet()) {
            JsonElement serializedValue = context.serialize(src.getAdapters().get(key));
            object.add(key, serializedValue);
        }
        // send back to Gson serializer
        return object;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
