package com.estafet.microservices.api.project.jms;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import com.estafet.microservices.api.project.model.Project;

@RunWith(MockitoJUnitRunner.class)
public class NewProjectProducerTest {

	@InjectMocks
	NewProjectProducer producer;

	@Mock
	JmsTemplate template;
	
	@Test
	public void testSendMessage() {
		producer.sendMessage(Project.getAPI());
		verify(template).convertAndSend("new.project.topic", Project.getAPI().toJSON());
		
	}

}
