package com.estafet.microservices.api.project.messages;

import com.estafet.microservices.api.project.model.Project;

public class ProjectDetails {

	private String title;

	public String getTitle() {
		return title;
	}
	
	public ProjectDetails setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public Project getProject() {
		Project project = new Project();
		project.setTitle(title);
		return project;
	}

}
