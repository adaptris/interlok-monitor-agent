package com.adaptris.monitor.agent.multicast;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.core.CoreException;
import com.adaptris.monitor.agent.EventReceiverListener;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.json.EventJsonMarshaller;

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
  
  private MulticastSocketReceiver multicastSocketReceiver;
  
  private volatile boolean isRunning;
  
  public MulticastEventReceiver() {
    this.setListeners(new ArrayList<>());
    this.setMulticastSocketReceiver(new MulticastSocketReceiverImpl());
  }
  
  public void start() {
    isRunning = true;
    
    try {
      this.getMulticastSocketReceiver().connect(DEFAULT_MULTICAST_GROUP, DEFAULT_MULTICAST_PORT, true, 30000);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    new Thread(new Runnable() {
      
      @Override
      public void run() {
        while(isRunning) {
          try {
            log.trace("Attempting to read packet");
            final DatagramPacket packet = getMulticastSocketReceiver().receive(STANDARD_PACKET_SIZE);
            log.trace("Packet read");
            ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            ActivityMap activityMap = (ActivityMap) iStream.readObject();
            
            for(EventReceiverListener listener : getListeners())
              listener.eventReceived(activityMap);
            
            log.debug("Activity map processed");
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
        
        getMulticastSocketReceiver().disconnect();
      }
    }, "Event Receiver Thread").start();
  }
  
  public void stop() {
    isRunning = false;
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

  public MulticastSocketReceiver getMulticastSocketReceiver() {
    return multicastSocketReceiver;
  }

  public void setMulticastSocketReceiver(MulticastSocketReceiver multicastSocketReceiver) {
    this.multicastSocketReceiver = multicastSocketReceiver;
  }
}
