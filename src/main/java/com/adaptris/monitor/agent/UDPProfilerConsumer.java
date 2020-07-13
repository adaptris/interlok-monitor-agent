package com.adaptris.monitor.agent;

import javax.validation.Valid;
import com.adaptris.annotation.Removal;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.AdaptrisPollingConsumer;
import com.adaptris.core.ConsumeDestination;
import com.adaptris.core.CoreException;
import com.adaptris.core.util.DestinationHelper;
import com.adaptris.core.util.LoggingHelper;
import com.adaptris.monitor.agent.activity.ActivityMap;
import com.adaptris.monitor.agent.json.EventJsonMarshaller;
import lombok.Getter;
import lombok.Setter;

public class UDPProfilerConsumer extends AdaptrisPollingConsumer {
  private int packetSize = 120400;
  private EventJsonMarshaller jsonMarshaller;


  /**
   * The consume destination has no meaning.
   *
   */
  @Deprecated
  @Valid
  @Removal(version = "4.0.0", message = "Has no meaning, and will be removed.")
  @Getter
  @Setter
  private ConsumeDestination destination;

  private transient boolean destinationWarningLogged = false;

  @Override
  protected void prepareConsumer() throws CoreException {
    DestinationHelper.logConsumeDestinationWarning(destinationWarningLogged,
        () -> destinationWarningLogged = true, getDestination(),
        "{} uses destination, it's not required", LoggingHelper.friendlyName(this));
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
    AdaptrisMessage msg =
        AdaptrisMessageFactory.defaultIfNull(getMessageFactory()).newMessage(data);
    retrieveAdaptrisMessageListener().onAdaptrisMessage(msg);
  }

  @Override
  protected String newThreadName() {
    return DestinationHelper.threadName(retrieveAdaptrisMessageListener(), getDestination());
  }
}
