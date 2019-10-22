package com.adaptris.monitor.agent.activity;

import com.adaptris.core.Adapter;
import com.adaptris.core.ServiceList;
import com.adaptris.core.services.LogMessageService;

import junit.framework.TestCase;

public class AdapterInstanceActivityMapCreatorTest extends TestCase {

  public void testCreateBaseMap() {
    Adapter adapter = TestUtils.buildNestedServiceTestAdapter();

    AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
    ActivityMap activityMap = activityMapCreator.createBaseMap(adapter);

    assertEquals(1, activityMap.getAdapters().size());

    AdapterActivity adapterActivity = (AdapterActivity) activityMap.getAdapters().get(TestUtils.ADAPTER_ID);
    assertEquals(TestUtils.ADAPTER_ID, adapterActivity.getUniqueId());
    assertEquals(1, adapterActivity.getChannels().size());

    ChannelActivity channelActivity = adapterActivity.getChannels().get("channel1");
    assertNotNull(channelActivity);
    assertEquals("channel1", channelActivity.getUniqueId());
    assertEquals(1, channelActivity.getWorkflows().size());

    WorkflowActivity workflowActivity = channelActivity.getWorkflows().get("workflow1");
    assertNotNull(workflowActivity);
    assertEquals("workflow1", workflowActivity.getUniqueId());
    assertEquals(2, workflowActivity.getServices().size());

    assertNotNull(workflowActivity.getConsumerActivity());
    assertEquals("consumer", workflowActivity.getConsumerActivity().getUniqueId());

    assertNotNull(workflowActivity.getProducerActivity());
    assertEquals("producer", workflowActivity.getProducerActivity().getUniqueId());

    ServiceActivity serviceActivity1 = workflowActivity.getServices().get("service1");
    assertNotNull(serviceActivity1);
    assertEquals("service1", serviceActivity1.getUniqueId());
    assertEquals(LogMessageService.class.getSimpleName(), serviceActivity1.getClassName());

    ServiceActivity serviceActivityList = workflowActivity.getServices().get("serviceList1");
    assertNotNull(serviceActivityList);
    assertEquals("serviceList1", serviceActivityList.getUniqueId());
    assertEquals(ServiceList.class.getSimpleName(), serviceActivityList.getClassName());
    assertEquals(2, serviceActivityList.getServices().size());

    ServiceActivity serviceActivity2 = serviceActivityList.getServices().get("service2");
    assertNotNull(serviceActivity2);
    assertEquals("service2", serviceActivity2.getUniqueId());
    assertEquals(LogMessageService.class.getSimpleName(), serviceActivity2.getClassName());

    ServiceActivity serviceActivityList2 = serviceActivityList.getServices().get("serviceList2");
    assertNotNull(serviceActivityList2);
    assertEquals("serviceList2", serviceActivityList2.getUniqueId());
    assertEquals(ServiceList.class.getSimpleName(), serviceActivityList2.getClassName());
    assertEquals(0, serviceActivityList2.getServices().size());
  }

  public void testCreateBaseMapWrongObject() {
    Adapter adapter = TestUtils.buildNestedServiceTestAdapter();

    try {
      AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
      activityMapCreator.createBaseMap(adapter.getEventHandler());
      fail("Should throw a RuntimeException");
    } catch (RuntimeException rte) {
      // The test pass
    }
  }

}
