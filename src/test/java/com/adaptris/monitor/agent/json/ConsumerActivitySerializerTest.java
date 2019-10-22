package com.adaptris.monitor.agent.json;

import com.adaptris.monitor.agent.activity.ConsumerActivity;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

public class ConsumerActivitySerializerTest extends TestCase {

  private ConsumerActivitySerializer serializer;

  @Override
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
