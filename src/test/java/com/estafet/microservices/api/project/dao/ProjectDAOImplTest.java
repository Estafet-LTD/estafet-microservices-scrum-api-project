package com.estafet.microservices.api.project.dao;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

import com.estafet.microservices.api.project.jms.NewProjectProducer;
import com.estafet.microservices.api.project.model.Project;

@RunWith(MockitoJUnitRunner.class)
public class ProjectDAOImplTest {

	@InjectMocks
	ProjectDAO projectDAO = new ProjectDAOImpl();
	
	@Mock
	NewProjectProducer newProjectProducer;
	
	@Mock
	EntityManager entityManager;

	@Test
	public void testGetProject() {
		Project project = Project.getAPI("1.0.0");
		when(entityManager.find(Project.class, Integer.valueOf(1))).thenReturn(project);
		assertEquals(project, projectDAO.getProject(1));
		verify(entityManager, times(1)).find(Project.class, Integer.valueOf(1));
	}

	@Test
	public void testGetProjects() {
		Query query = mock(Query.class);
		when(query.getResultList()).thenReturn(new ArrayList<Project>());
		when(entityManager.createQuery("Select p from Project p")).thenReturn(query);
		assertThat(projectDAO.getProjects().isEmpty(), is(true));
	}

	@Test
	public void testDeleteProject() {
		Project project = Project.getAPI("1.0.0");
		when(entityManager.find(Project.class, Integer.valueOf(1))).thenReturn(project);
		projectDAO.deleteProject(1);
		verify(entityManager, times(1)).remove(project);
	}

	@Test
	public void testUpdateProject() {
		Project project = Project.getAPI("1.0.0");
		projectDAO.updateProject(project);
		verify(entityManager, times(1)).merge(project);
	}

	@Test
	public void testCreateProject() {
		Project project = Project.getAPI("1.0.0");
		projectDAO.createProject(project);
		verify(entityManager, times(1)).persist(project);
		verify(newProjectProducer, times(1)).sendMessage(project);
	}

}
