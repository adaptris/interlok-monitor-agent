package com.adaptris.monitor.agent;

import java.net.DatagramPacket;

public interface UDPDatagramReceiver {

  public DatagramPacket receive(UDPProfilerConsumer consumer) throws Exception;
  
}
