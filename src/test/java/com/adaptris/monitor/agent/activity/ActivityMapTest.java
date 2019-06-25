package com.adaptris.monitor.agent.activity;

import com.adaptris.core.Adapter;
import com.adaptris.core.Channel;
import com.adaptris.core.NullMessageConsumer;
import com.adaptris.core.NullMessageProducer;
import com.adaptris.core.ServiceList;
import com.adaptris.core.StandardWorkflow;
import com.adaptris.core.services.LogMessageService;
import com.adaptris.profiler.MessageProcessStep;
import com.adaptris.profiler.StepType;

import junit.framework.TestCase;

public class ActivityMapTest extends TestCase {
  
  private static final String ADAPTER_ID = "adapter";
  
  private ActivityMap activityMap;
  
  public void setUp() throws Exception {
    Adapter adapter = buildNestedServiceTestAdapter();
    
    AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
    activityMap = activityMapCreator.createBaseMap(adapter);
  }
  
  public void tearDown() throws Exception {
    activityMap.resetActivity();
  }

  public void testProcessEvents() {
    MessageProcessStep serviceStep = new MessageProcessStep();
    serviceStep.setMessageId("1");
    serviceStep.setStepInstanceId("service2");
    serviceStep.setStepType(StepType.SERVICE);
    serviceStep.setTimeStarted(System.currentTimeMillis());
    serviceStep.setTimeTakenMs(1);
    
    this.activityMap.addActivity(serviceStep);
    
    MessageProcessStep consumerStep = new MessageProcessStep();
    consumerStep.setMessageId("1");
    consumerStep.setStepInstanceId("consumer");
    consumerStep.setStepType(StepType.CONSUMER);
    consumerStep.setTimeStarted(System.currentTimeMillis());
    consumerStep.setTimeTakenMs(1);
    
    this.activityMap.addActivity(consumerStep);
    
    MessageProcessStep producerStep = new MessageProcessStep();
    producerStep.setMessageId("1");
    producerStep.setStepInstanceId("producer");
    producerStep.setStepType(StepType.PRODUCER);
    producerStep.setTimeStarted(System.currentTimeMillis());
    producerStep.setTimeTakenMs(1);
    
    this.activityMap.addActivity(producerStep);
    
    WorkflowActivity workflowActivity = ((AdapterActivity) this.activityMap.getAdapters().get(ADAPTER_ID)).getChannels().get("channel1").getWorkflows().get("workflow1");
    
    ConsumerActivity consumerActivity = workflowActivity.getConsumerActivity();
    ProducerActivity producerActivity = workflowActivity.getProducerActivity();
    
    assertEquals(1, consumerActivity.getMessageCount());
    assertEquals(1, producerActivity.getMessageCount());
    
    String mapString = this.activityMap.toString();
    
    assertTrue(mapString.contains(ADAPTER_ID));
    assertTrue(mapString.contains("channel1"));
    assertTrue(mapString.contains("workflow1"));
    assertTrue(mapString.contains("service1"));
    assertTrue(mapString.contains("service2"));
    assertTrue(mapString.contains("consumer"));
    assertTrue(mapString.contains("producer"));
    
  }
  
  private Adapter buildNestedServiceTestAdapter() {
    Adapter adapter = new Adapter();
    adapter.setUniqueId(ADAPTER_ID);
    Channel channel = new Channel();
    channel.setUniqueId("channel1");
    StandardWorkflow workflow = new StandardWorkflow();
    workflow.setUniqueId("workflow1");
    LogMessageService service = new LogMessageService();
    service.setUniqueId("service1");
    
    LogMessageService service2 = new LogMessageService();
    service2.setUniqueId("service2");
    
    ServiceList serviceList = new ServiceList(service2);
    
    NullMessageConsumer consumer = new NullMessageConsumer();
    consumer.setUniqueId("consumer");
    
    NullMessageProducer producer = new NullMessageProducer();
    producer.setUniqueId("producer");
    
    workflow.getServiceCollection().add(service);
    workflow.getServiceCollection().add(serviceList);
    workflow.setProducer(producer);
    workflow.setConsumer(consumer);
    channel.getWorkflowList().add(workflow);
    adapter.getChannelList().add(channel);
    
    return adapter;
  }
}
