package com.adaptris.monitor.agent.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.adaptris.core.Adapter;
import com.adaptris.profiler.MessageProcessStep;
import com.adaptris.profiler.StepType;


public class ActivityMapTest {

  private ActivityMap activityMap;

  @Before
  public void setUp() throws Exception {
    Adapter adapter = TestUtils.buildNestedServiceTestAdapter();

    AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
    activityMap = activityMapCreator.createBaseMap(adapter);
  }

  @After
  public void tearDown() throws Exception {
    activityMap.resetActivity();
  }

  @Test
  public void testProcessEvents() {
    MessageProcessStep serviceListStep = new MessageProcessStep();
    serviceListStep.setMessageId("1");
    serviceListStep.setStepInstanceId("serviceList1");
    serviceListStep.setStepType(StepType.SERVICE);
    serviceListStep.setTimeStarted(System.currentTimeMillis());
    serviceListStep.setTimeStartedNanos(System.nanoTime());
    serviceListStep.setTimeTakenMs(1);
    serviceListStep.setTimeTakenNanos(1000);
    serviceListStep.setOrder(0);

    activityMap.addActivity(serviceListStep);

    MessageProcessStep serviceStep = new MessageProcessStep();
    serviceStep.setMessageId("1");
    serviceStep.setStepInstanceId("service2");
    serviceStep.setStepType(StepType.SERVICE);
    serviceStep.setTimeStarted(System.currentTimeMillis());
    serviceListStep.setTimeStartedNanos(System.nanoTime());
    serviceStep.setTimeTakenMs(1);
    serviceListStep.setTimeTakenNanos(1000);
    serviceStep.setOrder(1);

    activityMap.addActivity(serviceStep);

    MessageProcessStep consumerStep = new MessageProcessStep();
    consumerStep.setMessageId("1");
    consumerStep.setStepInstanceId("consumer");
    consumerStep.setStepType(StepType.CONSUMER);
    consumerStep.setTimeStarted(System.currentTimeMillis());
    consumerStep.setTimeStartedNanos(System.nanoTime());
    consumerStep.setTimeTakenMs(1);
    consumerStep.setTimeTakenNanos(1000);

    activityMap.addActivity(consumerStep);

    MessageProcessStep producerStep = new MessageProcessStep();
    producerStep.setMessageId("1");
    producerStep.setStepInstanceId("producer");
    producerStep.setStepType(StepType.PRODUCER);
    producerStep.setTimeStarted(System.currentTimeMillis());
    producerStep.setTimeStartedNanos(System.nanoTime());
    producerStep.setTimeTakenMs(0);
    producerStep.setTimeTakenNanos(0);

    activityMap.addActivity(producerStep);

    WorkflowActivity workflowActivity = ((AdapterActivity) activityMap.getAdapters().get(TestUtils.ADAPTER_ID)).getChannels()
        .get("channel1").getWorkflows().get("workflow1");

    ConsumerActivity consumerActivity = workflowActivity.getConsumerActivity();
    ProducerActivity producerActivity = workflowActivity.getProducerActivity();

    ServiceActivity serviceActivity1 = workflowActivity.getServices().get("service1");

    ServiceActivity serviceListActivity1 = workflowActivity.getServices().get("serviceList1");
    ServiceActivity serviceActivity2 = serviceListActivity1.getServices().get("service2");

    assertEquals(1, consumerActivity.getMessageCount());
    assertEquals(1, producerActivity.getMessageCount());

    assertEquals(0, serviceActivity1.getMessageCount());
    assertEquals(-1, serviceActivity1.getOrder());

    assertEquals(1, serviceListActivity1.getMessageCount());
    assertEquals(0, serviceListActivity1.getOrder());
    assertEquals(1, serviceActivity2.getMessageCount());
    assertEquals(1, serviceActivity2.getOrder());

    String mapString = activityMap.toString();

    assertTrue(mapString.contains(TestUtils.ADAPTER_ID));
    assertTrue(mapString.contains("channel1"));
    assertTrue(mapString.contains("workflow1"));
    assertTrue(mapString.contains("service1"));
    assertTrue(mapString.contains("serviceList1"));
    assertTrue(mapString.contains("service2"));
    assertTrue(mapString.contains("serviceList2"));
    assertTrue(mapString.contains("consumer"));
    assertTrue(mapString.contains("producer"));
  }

  @Test
  public void testProcessEventsWrongConsumerAndProducerProcessStepId() {
    WorkflowActivity workflowActivity = ((AdapterActivity) activityMap.getAdapters().get(TestUtils.ADAPTER_ID)).getChannels()
        .get("channel1").getWorkflows().get("workflow1");

    ConsumerActivity consumerActivity = workflowActivity.getConsumerActivity();
    ProducerActivity producerActivity = workflowActivity.getProducerActivity();

    MessageProcessStep consumerStep = new MessageProcessStep();
    consumerStep.setMessageId("1");
    consumerStep.setStepInstanceId("differentId");
    consumerStep.setStepType(StepType.CONSUMER);
    consumerStep.setTimeStarted(System.currentTimeMillis());
    consumerStep.setTimeStartedNanos(System.nanoTime());
    consumerStep.setTimeTakenMs(1);
    consumerStep.setTimeTakenNanos(1000);

    consumerActivity.addActivity(consumerStep);

    MessageProcessStep producerStep = new MessageProcessStep();
    producerStep.setMessageId("1");
    producerStep.setStepInstanceId("differentId");
    producerStep.setStepType(StepType.PRODUCER);
    producerStep.setTimeStarted(System.currentTimeMillis());
    producerStep.setTimeStartedNanos(System.nanoTime());
    producerStep.setTimeTakenMs(0);
    producerStep.setTimeTakenNanos(0);

    producerActivity.addActivity(producerStep);

    assertEquals(0, consumerActivity.getMessageCount());
    assertEquals(0, producerActivity.getMessageCount());

    String mapString = activityMap.toString();

    assertTrue(mapString.contains(TestUtils.ADAPTER_ID));
    assertTrue(mapString.contains("channel1"));
    assertTrue(mapString.contains("workflow1"));
    assertTrue(mapString.contains("consumer"));
    assertTrue(mapString.contains("producer"));
  }
  
  // INTERLOK-3135
  @Test
  public void testConsumerAddedToCorrectWorkflow() throws Exception {
    WorkflowActivity workflowActivity = ((AdapterActivity) activityMap.getAdapters().get(TestUtils.ADAPTER_ID)).getChannels()
        .get("channel1").getWorkflows().get("workflow1");

    MessageProcessStep consumerStep = new MessageProcessStep();
    consumerStep.setMessageId("1");
    consumerStep.setStepInstanceId("differentId");
    consumerStep.setStepType(StepType.CONSUMER);
    consumerStep.setTimeStarted(System.currentTimeMillis());
    consumerStep.setTimeStartedNanos(System.nanoTime());
    consumerStep.setTimeTakenMs(1);
    consumerStep.setTimeTakenNanos(1000);

    workflowActivity.addActivity(consumerStep);

    MessageProcessStep producerStep = new MessageProcessStep();
    producerStep.setMessageId("1");
    producerStep.setStepInstanceId("differentId");
    producerStep.setStepType(StepType.PRODUCER);
    producerStep.setTimeStarted(System.currentTimeMillis());
    producerStep.setTimeStartedNanos(System.nanoTime());
    producerStep.setTimeTakenMs(0);
    producerStep.setTimeTakenNanos(0);

    workflowActivity.addActivity(producerStep);

    assertEquals(0, workflowActivity.getConsumerActivity().getMessageCount());
    assertEquals(0, workflowActivity.getProducerActivity().getMessageCount());
  }

}
