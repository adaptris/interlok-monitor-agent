package com.adaptris.monitor.agent.activity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdapterActivity extends BaseActivity implements Serializable {
  
  private static final long serialVersionUID = -4031508025636325352L;

  private Map<String, ChannelActivity> channels;
  
  public AdapterActivity() {
    channels = new LinkedHashMap<>();
  }

  public void addChannelActivity(ChannelActivity channelActivity) {
    this.getChannels().put(channelActivity.getUniqueId(), channelActivity);
  }

  public Map<String, ChannelActivity> getChannels() {
    return channels;
  }

  public void setChannels(Map<String, ChannelActivity> channels) {
    this.channels = channels;
  }
  
  public boolean equals(Object object) {
    if(object instanceof AdapterActivity) {
      if(((AdapterActivity) object).getUniqueId().equals(this.getUniqueId()))
        return true;
    }
    return false;
  }
  
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Adapter = ");
    buffer.append(this.getUniqueId());
    buffer.append("\n");
    for(ChannelActivity channel : this.getChannels().values()) 
      buffer.append(channel);
    
    return buffer.toString();
  }
}
