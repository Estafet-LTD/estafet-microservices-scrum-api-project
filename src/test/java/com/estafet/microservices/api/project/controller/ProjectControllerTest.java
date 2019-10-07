package com.estafet.microservices.api.project.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estafet.microservices.api.project.model.Project;
import com.estafet.microservices.api.project.service.ProjectService;

import io.opentracing.ActiveSpan;
import io.opentracing.Tracer;

@RunWith(MockitoJUnitRunner.class)
public class ProjectControllerTest {

	@InjectMocks
	ProjectController controller = new ProjectController();

	@Mock
	ProjectService projectService;

	@Mock
	Tracer tracer;
	
	@Before
	public void before() {
		when(tracer.activeSpan()).thenReturn(mock(ActiveSpan.class));
	}
	
	@Test
	public void testProjectAPI() {
		assertEquals(controller.projectAPI().getId().intValue(), 1);
		assertEquals(controller.projectAPI().getTitle(), "my project");
	}

	@Test
	public void testGetProject() {
		when(projectService.getProject(1)).thenReturn(Project.getAPI("1.0.0"));
		assertEquals(controller.getProject(1).getId().intValue(), 1);
		assertEquals(controller.projectAPI().getTitle(), "my project");
	}

	@Test
	public void testGetProjects() {
		List<Project> projects = new ArrayList<Project>();
		when(projectService.getProjects()).thenReturn(projects);
		List<Project> result = controller.getProjects();
		verify(projectService, times(1)).getProjects();
		assertSame(projects, result);
	}

	@Test
	public void testCreateProject() {
		Project project = Project.getAPI("1.0.0");
		when(projectService.createProject(project)).thenReturn(project);
		ResponseEntity result = controller.createProject(project);
		verify(projectService, times(1)).createProject(project);
		assertEquals(result.getStatusCode(), HttpStatus.OK);
		assertSame(result.getBody(), project);
	}

	@Test
	public void testDeleteProject() {
		ResponseEntity result = controller.deleteProject(12);
		assertEquals(result.getStatusCode(), HttpStatus.OK);
		verify(projectService, times(1)).deleteProject(12);
	}

}
