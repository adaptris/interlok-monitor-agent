package com.adaptris.monitor.agent;

import com.adaptris.core.CoreException;
import com.adaptris.core.PollerImp;
import com.adaptris.monitor.agent.activity.ActivityMap;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class UDPPoller extends PollerImp {

    private transient boolean stopped = false;
    private transient Thread monitorThread;

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

    @Override
    public void close() {
    }

    @Override
    public void prepare() throws CoreException {
    }

    private Thread createThread() {
        return new Thread("UDP Event Poller Thread") {
            final byte[] udpPacket = new byte[getPacketSize()];
            @Override
            @SuppressWarnings("unchecked")
            public void run() {
                while(!stopped) {
                    try {
                        final DatagramPacket packet = new DatagramPacket(udpPacket, udpPacket.length);
                        getSocket().receive(packet);

                        ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                        ActivityMap activityMap = (ActivityMap) iStream.readObject();
                        convertAndProcessMessage(activityMap);
//                        AdaptrisMessage msg = AdaptrisMessageFactory.defaultIfNull(retrieveConsumer().getMessageFactory()).newMessage(data);
//                        retrieveConsumer().retrieveAdaptrisMessageListener().onAdaptrisMessage(msg);
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

    private int getPacketSize() {
        return ((UDPProfilerConsumer)this.retrieveConsumer()).getPacketSize();
    }

    private MulticastSocket getSocket() {
        UDPConnection udpConnection = ((UDPProfilerConsumer) this.retrieveConsumer()).retrieveConnection(UDPConnection.class);
        return udpConnection.getSocket();
    }
}
