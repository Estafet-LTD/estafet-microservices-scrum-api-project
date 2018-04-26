package com.estafet.microservices.api.project.dao;

import java.util.List;

import com.estafet.microservices.api.project.model.Project;

public interface ProjectDAO {

	Project getProject(int projectId);

	List<Project> getProjects();

	void deleteProject(int projectId);

	Project updateProject(Project project);

	Project createProject(Project project);

}