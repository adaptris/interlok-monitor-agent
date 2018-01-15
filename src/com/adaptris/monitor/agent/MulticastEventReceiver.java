package com.adaptris.monitor.agent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

import com.adaptris.monitor.agent.activity.ActivityMap;


public class MulticastEventReceiver {
  
  private static final String DEFAULT_MULTICAST_GROUP = "224.0.0.4";
  
  private static final int DEFAULT_MULTICAST_PORT = 5577;

  protected static final int STANDARD_PACKET_SIZE = 120400;
  
  private List<EventReceiverListener> listeners;
  
  private volatile boolean isRunning;

  private MulticastSocket socket;
  
  public MulticastEventReceiver() {
    listeners = new ArrayList<>();
  }
  
  public void start() {
    isRunning = true;
    
    try {
      socketConnect();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    new Thread(new Runnable() {
      
      final byte[] udpPacket = new byte[STANDARD_PACKET_SIZE];
      @Override
      public void run() {
        while(isRunning) {
          try {
            final DatagramPacket packet = new DatagramPacket(udpPacket, udpPacket.length);
            socket.receive(packet);
                        
            ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            ActivityMap activityMap = (ActivityMap) iStream.readObject();
            
            for(EventReceiverListener listener : getListeners())
              listener.eventReceived(activityMap);
            
            System.out.println(activityMap);
            
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        
        if(socket != null)
          socket.close();
      }
    }, "Event Receiver Thread").start();
  }
  
  public void stop() {
    isRunning = false;
  }

  private void socketConnect() throws IOException {
    socket = new MulticastSocket(DEFAULT_MULTICAST_PORT);
    socket.setReuseAddress(true);
    socket.setSoTimeout(300000);
    socket.joinGroup(InetAddress.getByName(DEFAULT_MULTICAST_GROUP));
  }
  
  public void addEventReceiverListener(EventReceiverListener eventReceiverListener) {
    this.getListeners().add(eventReceiverListener);
  }
  
  public List<EventReceiverListener> getListeners() {
    return listeners;
  }

  public void setListeners(List<EventReceiverListener> listeners) {
    this.listeners = listeners;
  }

}
