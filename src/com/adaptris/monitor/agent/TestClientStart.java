package com.adaptris.monitor.agent;

import javax.jms.JMSException;

public class TestClientStart {

	public static void main(String args[]) {
		MulticastEventReceiver multicastEventReceiver = new MulticastEventReceiver();
		multicastEventReceiver.start();
		
		// Configure JMS listener
		JMSMsgSenderSingleton statsMsgSender = null;
		try {
			statsMsgSender = JMSMsgSenderSingleton.getInstance();
			multicastEventReceiver.addEventReceiverListener(statsMsgSender);
		} catch (JMSException e) {
			System.err.println("Failed to configure JMS msg sender, no JMS msgs will be sent out. Error: "+e.getMessage());
		} finally {
//			statsMsgSender.close();
		}
		System.out.println("TestClientStart now running...");
	}

}
