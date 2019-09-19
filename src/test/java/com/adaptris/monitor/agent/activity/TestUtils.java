package com.adaptris.monitor.agent.activity;

import com.adaptris.core.Adapter;
import com.adaptris.core.Channel;
import com.adaptris.core.NullMessageConsumer;
import com.adaptris.core.NullMessageProducer;
import com.adaptris.core.ServiceList;
import com.adaptris.core.StandardWorkflow;
import com.adaptris.core.services.LogMessageService;

public class TestUtils {

  public static final String ADAPTER_ID = "adapter";

  public static Adapter buildNestedServiceTestAdapter() {
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

    ServiceList serviceList2 = new ServiceList();
    serviceList2.setUniqueId("serviceList2");

    ServiceList serviceList = new ServiceList(service2, serviceList2);
    serviceList.setUniqueId("serviceList1");

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
