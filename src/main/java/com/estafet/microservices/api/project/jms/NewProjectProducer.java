package com.estafet.microservices.api.project.jms;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.model.Project;

@Component
public class NewProjectProducer {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	public void sendMessage(Project project) {
		jmsTemplate.setPubSubDomain(true);
		jmsTemplate.convertAndSend("new.project.topic", project.toJSON(), new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws JMSException {
				message.setStringProperty("message.event.interaction.reference", UUID.randomUUID().toString());
				return message;
			}
		});
	}
}
