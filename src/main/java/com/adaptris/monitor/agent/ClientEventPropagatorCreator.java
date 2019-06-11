package com.adaptris.monitor.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adaptris.monitor.agent.jmx.JmxEventPropagator;
import com.adaptris.monitor.agent.multicast.MulticastEventPropagator;

public enum ClientEventPropagatorCreator {

  JMX {
    @Override
    public EventPropagator createClientPropagator(EventMonitorReceiver eventMonitorReceiver) throws Exception {
      return new JmxEventPropagator(eventMonitorReceiver);
    }
  },
  
  MULTICAST {
    @Override
    public EventPropagator createClientPropagator(EventMonitorReceiver eventMonitorReceiver) throws Exception {
      return new MulticastEventPropagator(eventMonitorReceiver);
    }
  };
  
  protected static final Logger log = LoggerFactory.getLogger(ClientEventPropagatorCreator.class);
  
  public abstract EventPropagator createClientPropagator(EventMonitorReceiver eventMonitorReceiver) throws Exception;
  
  public static ClientEventPropagatorCreator getCreator(String implementation) {
    ClientEventPropagatorCreator eventPropagatorCreator = null;
    try {
      eventPropagatorCreator = ClientEventPropagatorCreator.valueOf(implementation.toUpperCase().trim());
      log.debug("Profiling events will be propagated by the {} implementation.", eventPropagatorCreator.name());
    } catch (Exception ex) {
      log.info("Could not find event propagator for value {}.  Defaulting to JMX ", implementation);
      eventPropagatorCreator = ClientEventPropagatorCreator.JMX;
    }
    
    return eventPropagatorCreator;
  }
}
