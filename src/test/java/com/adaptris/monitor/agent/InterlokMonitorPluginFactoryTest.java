package com.adaptris.monitor.agent;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.adaptris.profiler.client.ClientPlugin;

public class InterlokMonitorPluginFactoryTest {

  private InterlokMonitorPluginFactory factory;

  @Before
  public void setUp() throws Exception {
    factory = new InterlokMonitorPluginFactory();
  }

  @Test
  public void testCreate() throws Exception {
    assertTrue(factory.getPlugin() instanceof ClientPlugin);
  }

  @Test
  public void testCreateOnlyOnce() throws Exception {
    ClientPlugin clientPlugin = factory.getPlugin();
    ClientPlugin clientPlugin2 = factory.getPlugin();

    assertTrue(clientPlugin == clientPlugin2);
  }

  @Test
  public void testGetPlugin() {
    assertTrue((new InterlokMonitorPluginFactory()).getPlugin() instanceof InterlokMonitorProfilerPlugin);
  }
}
