package com.estafet.microservices.api.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.microservices.api.project.model.Project;
import com.estafet.microservices.api.project.service.ProjectService;

import io.opentracing.Tracer;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
public class ProjectController {

	@Value("${app.version}")
	private String appVersion;
	
	@Autowired
	private Tracer tracer;
	
	@Autowired
	private ProjectService projectService;
	
	@GetMapping("/api")
	public Project projectAPI() {
		tracer.activeSpan().setTag("api", true);
		return Project.getAPI(appVersion);
	}
	
	@GetMapping("/project/{id}")
	public Project getProject(@PathVariable int id) {
		tracer.activeSpan().setTag("api", false);
		return projectService.getProject(id);
	}
	
	@GetMapping(value = "/projects")
	public List<Project> getProjects() {
		tracer.activeSpan().setTag("api", false);
		return projectService.getProjects();
	}
	
	@PostMapping("/project")
	public ResponseEntity createProject(@RequestBody Project project) {
		tracer.activeSpan().setTag("api", false);
		return new ResponseEntity(projectService.createProject(project), HttpStatus.OK);
	}
	
	@DeleteMapping("/project/{id}")
	public ResponseEntity deleteProject(@PathVariable int id) {
		tracer.activeSpan().setTag("api", false);
		projectService.deleteProject(id);
		return new ResponseEntity(id, HttpStatus.OK);
	}
	
}
