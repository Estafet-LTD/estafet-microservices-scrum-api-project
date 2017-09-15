package com.estafet.microservices.api.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.estafet.microservices.api.project.messages.ProjectDetails;
import com.estafet.microservices.api.project.model.Project;
import com.estafet.microservices.api.project.service.ProjectService;


@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@GetMapping("/project/{id}")
	public Project getProject(@PathVariable int id) {
		return projectService.getProject(id);
	}
	
	@GetMapping(value = "/projects")
	public List<Project> getProjects() {
		return projectService.getProjects();
	}
	
	@PostMapping("/project")
	public ResponseEntity createProject(@RequestBody ProjectDetails message) {
		return new ResponseEntity(projectService.createProject(message), HttpStatus.OK);
	}
	
	@PutMapping("/project/{id}")
	public ResponseEntity changeProjectDetails(@PathVariable int id, @RequestBody ProjectDetails message) {
		projectService.changeProjectDetails(id, message);
		return new ResponseEntity(projectService.getProject(id), HttpStatus.OK);
	}
	
	@DeleteMapping("/project/{id}")
	public ResponseEntity deleteProject(@PathVariable int id) {
		projectService.deleteProject(id);
		return new ResponseEntity(id, HttpStatus.OK);
	}
	
}
