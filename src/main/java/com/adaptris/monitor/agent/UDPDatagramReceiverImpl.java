package com.adaptris.monitor.agent;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class UDPDatagramReceiverImpl implements UDPDatagramReceiver {

  @Override
  public DatagramPacket receive(UDPProfilerConsumer consumer) throws Exception {
    final byte[] udpPacket = new byte[getPacketSize(consumer)];
    
    final DatagramPacket packet = new DatagramPacket(udpPacket, udpPacket.length);
    getSocket(consumer).receive(packet);
    
    return packet;
  } 
  
  private int getPacketSize(UDPProfilerConsumer consumer) {
    return consumer.getPacketSize();
  }

  private MulticastSocket getSocket(UDPProfilerConsumer consumer) {
    UDPConnection udpConnection = consumer.retrieveConnection(UDPConnection.class);
    return udpConnection.getSocket();
  }

}
