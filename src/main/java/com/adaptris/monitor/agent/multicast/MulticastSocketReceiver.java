package com.adaptris.monitor.agent.multicast;

import java.net.DatagramPacket;

public interface MulticastSocketReceiver {

  public void connect(String group, int port, boolean reuse, int soTimeout) throws Exception;
  
  public void disconnect();
  
  public DatagramPacket receive(int packetSize) throws Exception;
  
}
