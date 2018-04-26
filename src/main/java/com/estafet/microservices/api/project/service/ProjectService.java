package com.estafet.microservices.api.project.service;

import java.util.List;

import com.estafet.microservices.api.project.model.Project;

public interface ProjectService {

	List<Project> getProjects();

	Project getProject(int projectId);

	Project createProject(Project project);

	void deleteProject(int projectId);

}