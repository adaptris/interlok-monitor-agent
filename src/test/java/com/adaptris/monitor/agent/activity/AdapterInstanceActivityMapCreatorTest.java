package com.adaptris.monitor.agent.activity;

import com.adaptris.core.Adapter;
import com.adaptris.core.Service;
import com.adaptris.core.ServiceList;
import com.adaptris.core.services.LogMessageService;
import com.adaptris.core.services.metadata.AddMetadataService;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

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

  public void testScanClassReflectiveAllGetters() {
    AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
    AddMetadataService service = new AddMetadataService();
    service.addMetadataElement("myKey1", "myValue1");
    service.addMetadataElement("myKey2", "myValue2");
    ServiceList serviceList = new ServiceList();
    serviceList.add(service);
    List<Service> services = activityMapCreator.scanClassReflectiveAllGetters(serviceList);
    assertEquals(1, services.size());
  }

  public void testscanclassreflectiveallgettersthatcausedclasscastexception() {
    AdapterInstanceActivityMapCreator activityMapCreator = new AdapterInstanceActivityMapCreator();
    DummyWithPlainCollectionService wrapperService = new DummyWithPlainCollectionService();
    AddMetadataService metadataService = new AddMetadataService();
    List innerServiceList = new ArrayList();
    innerServiceList.add(metadataService);
    wrapperService.setPlainCollection(innerServiceList);
    List<Service> services = null;
    try {
      services = activityMapCreator.scanClassReflectiveAllGetters(wrapperService);
      assertEquals(1, services.size());
    } catch (RuntimeException e) {
      fail();
//      assertTrue(ClassCastException.class.isInstance(e.getCause()));
    }
  }
}
