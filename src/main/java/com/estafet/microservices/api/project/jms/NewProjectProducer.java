package com.estafet.microservices.api.project.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.estafet.microservices.api.project.model.Project;

@Component
public class NewProjectProducer {

	@Autowired 
	private JmsTemplate jmsTemplate;
	
	public void sendMessage(Project project) {
		jmsTemplate.convertAndSend("new.project.topic", project.toJSON());
	}
}
