package com.estafet.microservices.api.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "PROJECT")
public class Project {

	@Id
	@SequenceGenerator(name = "PROJECT_ID_SEQ", sequenceName = "PROJECT_ID_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_ID_SEQ")
	@Column(name = "PROJECT_ID")
	private Integer id;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "NO_SPRINTS", nullable = false)
	private Integer noSprints;

	@Column(name = "SPRINT_LENGTH_DAYS", nullable = false)
	private Integer sprintLengthDays;

	public Integer getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Project setTitle(String title) {
		this.title = title;
		return this;
	}

	public Integer getNoSprints() {
		return noSprints;
	}

	public void setNoSprints(Integer noSprints) {
		this.noSprints = noSprints;
	}

	public Integer getSprintLengthDays() {
		return sprintLengthDays;
	}

	public void setSprintLengthDays(Integer sprintLengthDays) {
		this.sprintLengthDays = sprintLengthDays;
	}

	public String toJSON() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	public static Project getAPI() {
		Project project = new Project();
		project.id = 1;
		project.title = "my project";
		return project;
	}

}
