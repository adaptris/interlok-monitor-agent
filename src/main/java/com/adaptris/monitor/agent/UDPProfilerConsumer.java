package com.adaptris.monitor.agent;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.AdaptrisPollingConsumer;
import com.adaptris.core.CoreException;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.json.EventJsonMarshaller;

public class UDPProfilerConsumer extends AdaptrisPollingConsumer {
    private int packetSize = 120400;
    private EventJsonMarshaller jsonMarshaller;


    @Override
    protected void prepareConsumer() throws CoreException {
        jsonMarshaller = new EventJsonMarshaller();
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    @Override
    protected int processMessages() {
        return 1;
    }

    public void processMessage(ActivityMap activityMap) {
        String data = jsonMarshaller.marshallToJson(activityMap);
        AdaptrisMessage msg = AdaptrisMessageFactory.defaultIfNull(getMessageFactory()).newMessage(data);
        retrieveAdaptrisMessageListener().onAdaptrisMessage(msg);
    }
}
