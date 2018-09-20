package com.adaptris.monitor.agent;

import com.adaptris.monitor.agent.multicast.MulticastEventReceiver;

public class TestClientStart {

  public static void main(String args[]) {
    new MulticastEventReceiver().start();
    
//    MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
//    
//    System.out.println(platformMBeanServer);
    
  }
  
}
