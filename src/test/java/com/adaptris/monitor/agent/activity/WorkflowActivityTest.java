package com.adaptris.monitor.agent.activity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.adaptris.core.Adapter;
import com.adaptris.profiler.MessageProcessStep;
import com.adaptris.profiler.StepType;

public class WorkflowActivityTest {
  @Test
  public void testConstructor() {
    assertEquals("\t\tWorkflow = null (0 ( failed (0 ) at 0  nanos (0 ms))\nnullnull",
        (new WorkflowActivity()).toString());
  }

  @Test
  public void testAddActivity() {
    Adapter adapter = TestUtils.buildNestedServiceTestAdapter();

    AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
    ActivityMap activityMap = activityMapCreator.createBaseMap(adapter);
    
    MessageProcessStep step = new MessageProcessStep();
    step.setStepInstanceId("service1");
    step.setStepType(StepType.SERVICE);
    step.setStepName("serviceList2");
    step.setTimeTakenNanos(100);
    
    activityMap.addActivity(step);
    
    assertEquals(100, (((AdapterActivity) activityMap.getAdapters()
        .get(TestUtils.ADAPTER_ID)).getChannels()
        .get("channel1").getWorkflows()
        .get("workflow1").getServices()
        .get("service1").getAvgNsTaken()));
  }
  
  @Test
  public void testResetActivity() {
    Adapter adapter = TestUtils.buildNestedServiceTestAdapter();

    AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
    ActivityMap activityMap = activityMapCreator.createBaseMap(adapter);
    
    MessageProcessStep step = new MessageProcessStep();
    step.setStepInstanceId("service1");
    step.setStepType(StepType.SERVICE);
    step.setStepName("serviceList2");
    step.setTimeTakenNanos(100);
    
    activityMap.addActivity(step);
    
    activityMap.resetActivity();
    
    assertEquals(0, (((AdapterActivity) activityMap.getAdapters()
        .get(TestUtils.ADAPTER_ID)).getChannels()
        .get("channel1").getWorkflows()
        .get("workflow1").getServices()
        .get("service1").getAvgNsTaken()));
  }
  
  @Test
  public void testCloneActivity() {
    Adapter adapter = TestUtils.buildNestedServiceTestAdapter();

    AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
    ActivityMap activityMap = activityMapCreator.createBaseMap(adapter);
    
    MessageProcessStep step = new MessageProcessStep();
    step.setStepInstanceId("service1");
    step.setStepType(StepType.SERVICE);
    step.setStepName("serviceList2");
    step.setTimeTakenNanos(100);
    
    activityMap.addActivity(step);
    
    ActivityMap clone = (ActivityMap) activityMap.clone();
    
    assertEquals(0, (((AdapterActivity) clone.getAdapters()
        .get(TestUtils.ADAPTER_ID)).getChannels()
        .get("channel1").getWorkflows()
        .get("workflow1").getServices()
        .get("service1").getAvgNsTaken()));
  }
}
