package com.estafet.microservices.api.project.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.estafet.microservices.api.project.jms.NewProjectProducer;
import com.estafet.microservices.api.project.model.Project;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private NewProjectProducer newProjectProducer;
	
	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.dao.ProjectDAO#getProject(int)
	 */
	@Override
	public Project getProject(int projectId) {
		return entityManager.find(Project.class, new Integer(projectId));
	}
	
	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.dao.ProjectDAO#getProjects()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getProjects() {
		return entityManager.createQuery("Select p from Project p").getResultList();
	}
	
	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.dao.ProjectDAO#deleteProject(int)
	 */
	@Override
	public void deleteProject(int projectId) {
		Project project = getProject(projectId);
		entityManager.remove(project);
	}
	
	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.dao.ProjectDAO#updateProject(com.estafet.microservices.api.project.model.Project)
	 */
	@Override
	public Project updateProject(Project project) {
		entityManager.merge(project);
		return project;
	}
	
	/* (non-Javadoc)
	 * @see com.estafet.microservices.api.project.dao.ProjectDAO#createProject(com.estafet.microservices.api.project.model.Project)
	 */
	@Override
	public Project createProject(Project project) {
		entityManager.persist(project);
		newProjectProducer.sendMessage(project);
		return project;
	}
	
}
