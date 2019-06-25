package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.adaptris.profiler.ProcessStep;
import com.google.gson.annotations.Expose;

public class AdapterActivity extends BaseActivity implements Serializable {

  private static final long serialVersionUID = -4031508025636325352L;

  @Expose
  private Map<String, ChannelActivity> channels;

  public AdapterActivity() {
    this.setChannels(new LinkedHashMap<>());
  }
  
  @Override
  public void addActivity(ProcessStep processStep) {
    for(String channelId : this.getChannels().keySet()) {
      this.getChannels().get(channelId).addActivity(processStep);
    }
  }

  @Override
  public void resetActivity() {
    for(String channelId : this.getChannels().keySet()) {
      this.getChannels().get(channelId).resetActivity();
    }
  }

  public void addChannelActivity(ChannelActivity channelActivity) {
    getChannels().put(channelActivity.getUniqueId(), channelActivity);
  }

  public Map<String, ChannelActivity> getChannels() {
    return channels;
  }

  public void setChannels(Map<String, ChannelActivity> channels) {
    this.channels = channels;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Adapter = ");
    buffer.append(getUniqueId());
    buffer.append("\n");
    for(ChannelActivity channel : getChannels().values()) {
      buffer.append(channel);
    }

    return buffer.toString();
  }

}
