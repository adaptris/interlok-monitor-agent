package com.adaptris.monitor.agent.json;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.ConsumerActivity;
import com.adaptris.monitor.agent.activity.ProducerActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class EventJsonMarshaller {
  private Gson gson;

  public EventJsonMarshaller() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(ConsumerActivity.class, new ConsumerActivitySerializer());
    gsonBuilder.registerTypeAdapter(ProducerActivity.class, new ProducerActivitySerializer());
    ActivityMapSerializer activityMapSerializer = new ActivityMapSerializer();
    gsonBuilder.registerTypeAdapter(ActivityMap.class, activityMapSerializer);
    gsonBuilder.excludeFieldsWithoutExposeAnnotation();
    //        gsonBuilder.setPrettyPrinting();
    gson = gsonBuilder.create();

    activityMapSerializer.setGson(gson);
  }

  public String marshallToJson(ActivityMap activityMap) {
    String json = gson.toJson(new ActivtyWrapper(activityMap));
    return json;
  }

  // Wrapper class to allow us to add a datestamp to the json output
  private class ActivtyWrapper {
    @Expose
    String datetimestamp;
    @Expose
    ActivityMap adapters;

    public ActivtyWrapper(ActivityMap metrics) {
      datetimestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
      adapters = metrics;
    }
  }
}
