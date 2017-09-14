package com.estafet.microservices.manage.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.manage.project.messages.ProjectDetails;
import com.estafet.microservices.manage.project.model.Project;
import com.estafet.microservices.manage.project.model.Sprint;
import com.estafet.microservices.manage.project.model.Story;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class ProjectService {

	public List<Project> getProjects() {
		RestTemplate template = new RestTemplate();
		return template.getForObject("http://localhost:8080/sprint-repository/projects",
				new ArrayList<Project>().getClass());
	}

	public Project getProject(int projectId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", projectId);
		return template.getForObject("http://localhost:8080/sprint-repository/project/{id}", Project.class, params);
	}

	public Project createProject(ProjectDetails message) {
		RestTemplate template = new RestTemplate();
		return template.postForObject("http://localhost:8080/sprint-repository/project", message, Project.class);
	}

	public void changeProjectDetails(int projectId, ProjectDetails message) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", projectId);
		template.put("http://localhost:8080/sprint-repository/project/{id}", message, params);
	}

	public void deleteProject(int projectId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", projectId);
		template.delete("http://localhost:8080/sprint-repository/project/{id}", params);
	}

	public List<Sprint> getProjectSprints(int projectId) {
		return getProject(projectId).getSprints();
	}
	
	public List<Story> getProjectStories(int projectId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", projectId);
		return template.getForObject("http://localhost:8080/story-repository/stories?projectId={id}",
				new ArrayList().getClass(), params);
	}

}
