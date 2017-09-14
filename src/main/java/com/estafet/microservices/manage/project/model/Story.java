package com.estafet.microservices.manage.project.model;

public class Story {

	private int id;

	private String title;

	private String description;

	private Integer storypoints;

	private Integer sprintId;

	private String status = "Not Started";

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Integer getStorypoints() {
		return storypoints;
	}

	public Integer getSprintId() {
		return sprintId;
	}

	public String getStatus() {
		return status;
	}

}
