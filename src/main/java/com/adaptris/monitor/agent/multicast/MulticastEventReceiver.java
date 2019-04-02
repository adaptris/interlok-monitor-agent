package com.adaptris.monitor.agent.multicast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.adaptris.core.CoreException;
import com.adaptris.monitor.agent.EventReceiverListener;
import com.adaptris.monitor.agent.UDPProfilerConsumer;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.ConsumerActivity;
import com.adaptris.monitor.agent.activity.ProducerActivity;
import com.adaptris.monitor.agent.json.ConsumerActivitySerializer;
import com.adaptris.monitor.agent.json.EventJsonMarshaller;
import com.adaptris.monitor.agent.json.ProducerActivitySerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instantiate an instance of this class, register your custom listener and execute the start method to begin receiving event data.
 * @author aaron
 *
 */
public class MulticastEventReceiver {

  protected transient Logger log = LoggerFactory.getLogger(this.getClass().getName());

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
            log.debug("Attempting to read packet");
            final DatagramPacket packet = new DatagramPacket(udpPacket, udpPacket.length);
            socket.receive(packet);
            log.debug("packet read");
            ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            ActivityMap activityMap = (ActivityMap) iStream.readObject();
            
            for(EventReceiverListener listener : getListeners())
              listener.eventReceived(activityMap);
            
            log.debug("Activity map processed");
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

//  private static EventJsonMarshaller jsonMarshaller;


  public static void main(String args[]) throws CoreException {
    final EventJsonMarshaller jsonMarshaller = new EventJsonMarshaller();
    System.out.println("Starting udp listener");
    MulticastEventReceiver multicastEventReceiver = new MulticastEventReceiver();
    multicastEventReceiver.start();
    multicastEventReceiver.addEventReceiverListener(activityMap -> System.out.println(jsonMarshaller.marshallToJson(activityMap)));
  }
}
