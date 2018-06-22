package com.adaptris.monitor.agent;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.activity.AdapterActivity;
import com.adaptris.monitor.agent.activity.ChannelActivity;
import com.adaptris.monitor.agent.activity.ServiceActivity;
import com.adaptris.monitor.agent.activity.WorkflowActivity;
import com.adaptris.monitor.agent.message.ComponentEvent;
import com.adaptris.profiler.ProcessStep;

public class MulticastEventPropagator implements EventPropagator {

  protected transient Logger log = LoggerFactory.getLogger(this.getClass());

  private static final int DELAY_ON_POLL_MS = 5000;

  private static final String DEFAULT_MULTICAST_GROUP = "224.0.0.4";

  private static final int DEFAULT_MULTICAST_PORT = 5577;

  private volatile boolean running;

  private EventMonitorReceiver eventMonitorReciever;

  private MulticastSocket socket;

  public MulticastEventPropagator(EventMonitorReceiver eventMonitorReceiver) {
    eventMonitorReciever = eventMonitorReceiver;
    running = true;
  }

  @Override
  public void run() {
    try {
      socket = new MulticastSocket(DEFAULT_MULTICAST_PORT);
      socket.joinGroup(InetAddress.getByName(DEFAULT_MULTICAST_GROUP));
      socket.setTimeToLive((byte) 1); // 1 byte ttl for subnet only
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    while(running) {
      try {
        Thread.sleep(DELAY_ON_POLL_MS);
      } catch (InterruptedException e) {
        running = false;
      }

      List<ProcessStep> events = eventMonitorReciever.getEvents();
      log.debug("Getting process events - " + events.size());
      if(events.size() > 0) {
        try {
          // create a map of the events we have seen, then send the map.
          ActivityMap activityMap = new ActivityMap();
          for(ProcessStep step : events) {
            activityMap.addActivity(step);
          }
          // Make the message smaller by getting rid of the list of messages.
          compactActivityMap(activityMap);
          propagateProcessEvent(activityMap);
        } catch (Throwable t) {
          t.printStackTrace();
        }
      }
    }
  }

  private void compactActivityMap(ActivityMap activityMap) {
    for (String adapterKey : activityMap.getAdapters().keySet()) {
      AdapterActivity aa = activityMap.getAdapters().get(adapterKey);
      for (String channelKey : aa.getChannels().keySet()) {
        ChannelActivity ca = aa.getChannels().get(channelKey);
        for (String workflowKey : ca.getWorkflows().keySet()) {
          WorkflowActivity wa = ca.getWorkflows().get(workflowKey);
          if (wa.getMessageIds() != null) {
            wa.getMessageIds().clear();
          }
          if (wa.getConsumerActivity() != null) {
            if (wa.getConsumerActivity().getMessageIds() != null) {
              wa.getConsumerActivity().getMessageIds().clear();
            }
            if (wa.getConsumerActivity().getMsTaken() != null) {
              wa.getConsumerActivity().getMsTaken().clear();
            }
          }
          if (wa.getProducerActivity() != null) {
            if (wa.getProducerActivity().getMessageIds() != null) {
              wa.getProducerActivity().getMessageIds().clear();
            }
            if (wa.getProducerActivity().getMsTaken() != null) {
              wa.getProducerActivity().getMsTaken().clear();
            }
          }

          for (String serviceKey : wa.getServices().keySet()) {
            ServiceActivity sa = wa.getServices().get(serviceKey);
            if (sa.getMessageIds() != null) {
              sa.getMessageIds().clear();
            }
            if (sa.getMsTaken() != null) {
              sa.getMsTaken().clear();
            }
          }
        }
      }
    }


  }

  @Override
  public void propagateProcessEvent(ActivityMap activityMap) {
    log.debug(activityMap.toString());

    sendMulticast(activityMap);
  }


  @Override
  public void propagateLifecycleEvent(ComponentEvent componentEvent) {
    // TODO Auto-generated method stub

  }

  private void sendMulticast(Object object) {
    //    DatagramSocket socket = null;
    try {
      //      socket = new DatagramSocket(DEFAULT_MULTICAST_PORT);
      //      InetAddress client = InetAddress.getByName(DEFAULT_MULTICAST_GROUP);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      oos.flush();
      byte[] data= baos.toByteArray();

      DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(DEFAULT_MULTICAST_GROUP), DEFAULT_MULTICAST_PORT);
      socket.send(packet);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      //      if(socket != null)
      //        socket.close();
    }
  }

  @Override
  public void startPropagator() {

  }

  @Override
  public void stopPropagator() {
    running = false;
  }

}
