package com.adaptris.monitor.agent.json;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.adaptris.monitor.agent.activity.ProducerActivity;
import com.google.gson.JsonObject;

public class ProducerActivitySerializerTest {
  
  private ProducerActivitySerializer serializer; 

  @Before
  public void setUp() throws Exception {
    serializer = new ProducerActivitySerializer();
  }
  
  @Test
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
