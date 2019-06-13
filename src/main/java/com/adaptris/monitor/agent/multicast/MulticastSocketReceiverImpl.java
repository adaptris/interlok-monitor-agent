package com.adaptris.monitor.agent.multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastSocketReceiverImpl implements MulticastSocketReceiver {

  private MulticastSocket socket;
  
  @Override
  public void connect(String group, int port, boolean reuse, int soTimeout) throws Exception {
    socket = new MulticastSocket(port);
    socket.setReuseAddress(reuse);
    socket.setSoTimeout(soTimeout);
    socket.joinGroup(InetAddress.getByName(group));
  }

  @Override
  public void disconnect() {
    if(socket != null)
      socket.close();
  }

  @Override
  public DatagramPacket receive(int packetSize) throws Exception {
    final byte[] udpPacket = new byte[packetSize];
    
    final DatagramPacket packet = new DatagramPacket(udpPacket, udpPacket.length);
    socket.receive(packet);
    return packet;
  }

}
