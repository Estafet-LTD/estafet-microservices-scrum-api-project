package com.estafet.microservices.api.project.model;

import java.util.ArrayList;
import java.util.List;

public class Project {

	private int id;

	private String title;

	private List<Sprint> sprints = new ArrayList<Sprint>();

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<Sprint> getSprints() {
		return sprints;
	}

	public Project setTitle(String title) {
		this.title = title;
		return this;
	}

}
