package com.adaptris.monitor.agent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.adaptris.core.Adapter;
import com.adaptris.core.Channel;
import com.adaptris.core.StandardWorkflow;
import com.adaptris.core.services.LogMessageService;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.BaseActivity;
import com.adaptris.profiler.client.EventReceiver;

public class InterlokMonitorProfilerPluginTest {
  private static final String ADAPTER_ID = "adapter";

  private InterlokMonitorProfilerPlugin plugin;

  private Adapter adapter;

  @Before
  public void setUp() throws Exception {
    plugin = new InterlokMonitorProfilerPlugin();
    adapter = this.buildTestAdapter();
  }

  @Test
  public void testLifecycle() throws Exception {
    plugin.init(adapter);
    plugin.start(adapter);

    ActivityMap activityMap = EventMonitorReceiver.getInstance().getAdapterActivityMap();
    BaseActivity baseActivity = activityMap.getAdapters().get(ADAPTER_ID);

    assertEquals(ADAPTER_ID, baseActivity.getUniqueId());

    plugin.stop(adapter);
    plugin.close(adapter);
  }

  @Test
  public void testLifecycleNoAdapter() throws Exception {
    plugin.init(new Object());
    plugin.start(new Object());

    ActivityMap activityMap = EventMonitorReceiver.getInstance().getAdapterActivityMap();

    assertNull(activityMap);

    plugin.stop(new Object());
    plugin.close(new Object());
  }

  @Test
  public void testReceiversInitialised() throws Exception {
    plugin.init(adapter);
    plugin.start(adapter);

    EventReceiver eventReceiver = plugin.getReceivers().get(0);

    assertTrue(eventReceiver instanceof EventMonitorReceiver);

    plugin.stop(adapter);
    plugin.close(adapter);
  }

  private Adapter buildTestAdapter() {
    Adapter adapter = new Adapter();
    adapter.setUniqueId(ADAPTER_ID);
    Channel channel = new Channel();
    channel.setUniqueId("channel1");
    StandardWorkflow workflow = new StandardWorkflow();
    workflow.setUniqueId("workflow1");
    LogMessageService service = new LogMessageService();
    service.setUniqueId("service1");

    workflow.getServiceCollection().add(service);
    channel.getWorkflowList().add(workflow);
    adapter.getChannelList().add(channel);

    return adapter;
  }
  
    @Test
    public void testConstructor() {
        List<EventReceiver> receivers = (new InterlokMonitorProfilerPlugin()).getReceivers();
        assertTrue(receivers instanceof java.util.ArrayList);
        assertEquals(1, receivers.size());
    }

    @Test
    public void testGetReceivers() {
        assertEquals(1, (new InterlokMonitorProfilerPlugin()).getReceivers().size());
    }
}

