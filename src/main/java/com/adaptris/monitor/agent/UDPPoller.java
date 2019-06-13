package com.adaptris.monitor.agent;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;

import com.adaptris.core.CoreException;
import com.adaptris.core.PollerImp;
import com.adaptris.monitor.agent.activity.ActivityMap;

public class UDPPoller extends PollerImp {

    private transient boolean stopped = false;
    private transient Thread monitorThread;
    private transient UDPDatagramReceiver datagramReceiver;

    public UDPPoller() {
      this.setDatagramReceiver(new UDPDatagramReceiverImpl());
    }
    
    @Override
    public void init() throws CoreException {
        if(!(this.retrieveConsumer() instanceof UDPProfilerConsumer))
            throw new CoreException("You cannot configure a UDP Profile Poller with any non poller consumer.");
        else {
            if(((UDPProfilerConsumer)this.retrieveConsumer()).getPacketSize() < 0)
                throw new CoreException("UDP Packet size must be non zero");
        }

        monitorThread = createThread();
    }

    @Override
    public void start() throws CoreException {
        stopped = false;
        monitorThread.start();
    }

    @Override
    public void stop() {
        stopped = true;
        if(monitorThread != null)
            monitorThread.interrupt();
    }

    private Thread createThread() {
        return new Thread("UDP Event Poller Thread") {
            @Override
            public void run() {
                while(!stopped) {
                    try {
                        DatagramPacket packet = getDatagramReceiver().receive(((UDPProfilerConsumer)retrieveConsumer()));
                        
                        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                        ActivityMap activityMap = (ActivityMap) iStream.readObject();
                        convertAndProcessMessage(activityMap);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
    }

    // TODO implement marshalling
    private void convertAndProcessMessage(ActivityMap activityMap) {
        ((UDPProfilerConsumer)retrieveConsumer()).processMessage(activityMap);
    }

    public UDPDatagramReceiver getDatagramReceiver() {
      return datagramReceiver;
    }

    public void setDatagramReceiver(UDPDatagramReceiver datagramReceiver) {
      this.datagramReceiver = datagramReceiver;
    }
}
