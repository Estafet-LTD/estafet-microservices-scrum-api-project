package com.estafet.microservices.api.project.container.tests;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class NewProjectTopic {

	Connection connection;
	MessageConsumer messageConsumer;

	public NewProjectTopic() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(System.getenv("JBOSS_A_MQ_BROKER_URL"));
		connection = connectionFactory.createConnection(System.getenv("JBOSS_A_MQ_BROKER_USER"), System.getenv("JBOSS_A_MQ_BROKER_PASSWORD"));
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic("new.project.topic");
		messageConsumer = session.createConsumer(topic);
		connection.start();
	}

	public void closeConnection() throws JMSException {
		connection.close();
	}

	public String consumeMessage(int timeout) throws JMSException {
		Message message = messageConsumer.receive(timeout);
		return ((TextMessage) message).getText();
	}
}