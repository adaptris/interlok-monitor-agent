package com.adaptris.monitor.agent.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.Adapter;
import com.adaptris.core.AdaptrisComponent;
import com.adaptris.core.Channel;

public class AdapterStructure extends ComponentStructure implements Serializable {

  private static final long serialVersionUID = -6357140948990752783L;
  
  private List<ChannelStructure> channels;
  
  public AdapterStructure() {
    channels = new ArrayList<>();
  }
  
  @Override
  public void build(AdaptrisComponent component) {
    if(component instanceof Adapter) {
      super.build(component);

      Adapter adapter = (Adapter) component;
      for(Channel channel : adapter.getChannelList().getChannels()) {
        ChannelStructure channelStructure = new ChannelStructure();
        channelStructure.build(channel);
        this.getChannels().add(channelStructure);
      }
    }
  }

  public List<ChannelStructure> getChannels() {
    return channels;
  }

  public void setChannels(List<ChannelStructure> channels) {
    this.channels = channels;
  }

}
