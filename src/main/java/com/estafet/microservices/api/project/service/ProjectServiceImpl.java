package com.estafet.microservices.api.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estafet.microservices.api.project.dao.ProjectDAO;
import com.estafet.microservices.api.project.model.Project;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectDAO projectDAO;
	
	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.service.ProjectService#getProjects()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Project> getProjects() {
		return projectDAO.getProjects();
	}

	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.service.ProjectService#getProject(int)
	 */
	@Override
	@Transactional(readOnly = true)
	public Project getProject(int projectId) {
		return projectDAO.getProject(projectId);
	}

	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.service.ProjectService#createProject(com.estafet.microservices.api.project.model.Project)
	 */
	@Override
	@Transactional
	public Project createProject(Project project) {
		return projectDAO.createProject(project);
	}

	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.service.ProjectService#deleteProject(int)
	 */
	@Override
	@Transactional
	public void deleteProject(int projectId) {
		projectDAO.deleteProject(projectId);
	}

}
