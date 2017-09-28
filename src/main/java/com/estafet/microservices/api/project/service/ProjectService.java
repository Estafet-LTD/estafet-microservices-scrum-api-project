package com.estafet.microservices.api.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.dao.ProjectDAO;
import com.estafet.microservices.api.project.messages.ProjectDetails;
import com.estafet.microservices.api.project.model.Project;

@Service
public class ProjectService {

	@Autowired
	private ProjectDAO projectDAO;
	
	@Transactional(readOnly = true)
	public List<Project> getProjects() {
		return projectDAO.getProjects();
	}

	@Transactional(readOnly = true)
	public Project getProject(int projectId) {
		return projectDAO.getProject(projectId);
	}

	@Transactional
	public Project createProject(ProjectDetails message) {
		return projectDAO.createProject(message.getProject());
	}

	@Transactional
	public void changeProjectDetails(int projectId, ProjectDetails message) {
		Project project = getProject(projectId);
		project.setTitle(message.getTitle());
		projectDAO.updateProject(project);
	}

	@Transactional
	public void deleteProject(int projectId) {
		projectDAO.deleteProject(projectId);
	}

}
