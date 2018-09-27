package com.adaptris.monitor.agent.multicast;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.adaptris.monitor.agent.AbstractEventPropagator;
import com.adaptris.monitor.agent.EventMonitorReceiver;
import com.adaptris.monitor.agent.activity.ActivityMap;

public class MulticastEventPropagator extends AbstractEventPropagator {

  private static final String DEFAULT_MULTICAST_GROUP = "224.0.0.4";

  private static final int DEFAULT_MULTICAST_PORT = 5577;

  private MulticastSocket socket;

  public MulticastEventPropagator(EventMonitorReceiver eventMonitorReceiver) throws Exception {
    super(eventMonitorReceiver);
  }

  @Override
  public void propagateProcessEvent(ActivityMap activityMap) {
    log.debug(activityMap.toString());

    sendMulticast(activityMap);
  }

  private void sendMulticast(Object object) {
    try {
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
    }
  }

}
