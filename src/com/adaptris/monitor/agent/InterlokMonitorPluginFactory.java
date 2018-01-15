package com.adaptris.monitor.agent;

import com.adaptris.profiler.client.ClientPlugin;
import com.adaptris.profiler.client.PluginFactory;

public class InterlokMonitorPluginFactory extends PluginFactory {

  private ClientPlugin plugin;

  @Override
  public ClientPlugin getPlugin() {
    if (this.plugin == null)
      this.plugin = new InterlokMonitorProfilerPlugin();
    return this.plugin;
  }

}
