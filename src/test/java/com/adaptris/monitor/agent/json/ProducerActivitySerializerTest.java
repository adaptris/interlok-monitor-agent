package com.adaptris.monitor.agent.json;

import com.adaptris.monitor.agent.activity.ProducerActivity;
import com.google.gson.JsonObject;

import junit.framework.TestCase;

public class ProducerActivitySerializerTest extends TestCase {
  
  private ProducerActivitySerializer serializer; 

  public void setUp() throws Exception {
    serializer = new ProducerActivitySerializer();
  }
  
  public void testSerialize() throws Exception {
    ProducerActivity producerActivity = new ProducerActivity();
    producerActivity.setUniqueId("consumer-activity");
    producerActivity.setMessageCount(10);
    producerActivity.setAvgNsTaken(1000);
    
    JsonObject json = (JsonObject) serializer.serialize(producerActivity, null, null);
    
    assertEquals("consumer-activity", json.get("uniqueId").getAsString());
    assertEquals(10, json.get("msgCount").getAsInt());
    assertEquals(1000, json.get("averageTimeTaken").getAsLong());
  }

}
