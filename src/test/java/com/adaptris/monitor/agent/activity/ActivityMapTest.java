package com.adaptris.monitor.agent.activity;

import com.adaptris.core.Adapter;
import com.adaptris.profiler.MessageProcessStep;
import com.adaptris.profiler.StepType;

import junit.framework.TestCase;


public class ActivityMapTest extends TestCase {

  private ActivityMap activityMap;

  @Override
  public void setUp() throws Exception {
    Adapter adapter = TestUtils.buildNestedServiceTestAdapter();

    AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
    activityMap = activityMapCreator.createBaseMap(adapter);
  }

  @Override
  public void tearDown() throws Exception {
    activityMap.resetActivity();
  }

  public void testProcessEvents() {
    MessageProcessStep serviceListStep = new MessageProcessStep();
    serviceListStep.setMessageId("1");
    serviceListStep.setStepInstanceId("serviceList1");
    serviceListStep.setStepType(StepType.SERVICE);
    serviceListStep.setTimeStarted(System.currentTimeMillis());
    serviceListStep.setTimeTakenMs(1);
    serviceListStep.setOrder(0);

    activityMap.addActivity(serviceListStep);

    MessageProcessStep serviceStep = new MessageProcessStep();
    serviceStep.setMessageId("1");
    serviceStep.setStepInstanceId("service2");
    serviceStep.setStepType(StepType.SERVICE);
    serviceStep.setTimeStarted(System.currentTimeMillis());
    serviceStep.setTimeTakenMs(1);
    serviceStep.setOrder(1);

    activityMap.addActivity(serviceStep);

    MessageProcessStep consumerStep = new MessageProcessStep();
    consumerStep.setMessageId("1");
    consumerStep.setStepInstanceId("consumer");
    consumerStep.setStepType(StepType.CONSUMER);
    consumerStep.setTimeStarted(System.currentTimeMillis());
    consumerStep.setTimeTakenMs(1);

    activityMap.addActivity(consumerStep);

    MessageProcessStep producerStep = new MessageProcessStep();
    producerStep.setMessageId("1");
    producerStep.setStepInstanceId("producer");
    producerStep.setStepType(StepType.PRODUCER);
    producerStep.setTimeStarted(System.currentTimeMillis());
    producerStep.setTimeTakenMs(1);

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

}
