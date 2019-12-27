package com.estafet.microservices.api.project.container.tests;

import com.estafet.microservices.api.project.model.Project;
import com.estafet.microservices.scrum.lib.commons.jms.TopicConsumer;

public class NewProjectTopicConsumer extends TopicConsumer {

	public NewProjectTopicConsumer() {
		super("new.project.topic");
	}

	public Project consume() {
		return super.consume(Project.class);
	}
	
}