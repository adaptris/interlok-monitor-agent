package com.adaptris.monitor.agent.json;

import static org.junit.Assert.assertEquals;

import org.junit.Before;

import com.adaptris.monitor.agent.activity.ConsumerActivity;
import com.google.gson.JsonObject;

public class ConsumerActivitySerializerTest {

  private ConsumerActivitySerializer serializer;

  @Before
  public void setUp() throws Exception {
    serializer = new ConsumerActivitySerializer();
  }

  public void testSerialize() throws Exception {
    ConsumerActivity consumerActivity = new ConsumerActivity();
    consumerActivity.setUniqueId("consumer-activity");
    consumerActivity.setMessageCount(10);
    consumerActivity.setAvgNsTaken(1000);

    JsonObject json = (JsonObject) serializer.serialize(consumerActivity, null, null);

    assertEquals("consumer-activity", json.get("uniqueId").getAsString());
    assertEquals(10, json.get("msgCount").getAsInt());
    assertEquals(1000, json.get("averageTimeTaken").getAsLong());
  }

}
