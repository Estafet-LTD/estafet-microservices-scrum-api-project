package com.estafet.microservices.api.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.estafet.microservices.api.project.model.Project;


@Repository
public class ProjectDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	public Project getProject(int projectId) {
		return entityManager.find(Project.class, new Integer(projectId));
	}
	
	@SuppressWarnings("unchecked")
	public List<Project> getProjects() {
		return entityManager.createQuery("Select p from Project p").getResultList();
	}
	
	public void deleteProject(int projectId) {
		Project project = getProject(projectId);
		entityManager.remove(project);
	}
	
	public Project updateProject(Project project) {
		entityManager.merge(project);
		return project;
	}
	
	public Project createProject(Project project) {
		entityManager.persist(project);
		return project;
	}
	
}
