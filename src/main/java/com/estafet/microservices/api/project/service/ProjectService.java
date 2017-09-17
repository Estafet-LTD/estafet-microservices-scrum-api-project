package com.estafet.microservices.api.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estafet.microservices.api.project.messages.ProjectDetails;
import com.estafet.microservices.api.project.model.Project;
import com.estafet.microservices.api.project.model.Sprint;
import com.estafet.microservices.api.project.model.Story;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
public class ProjectService {

	public List<Project> getProjects() {
		RestTemplate template = new RestTemplate();
		List objects = template.getForObject(System.getenv("PROJECT_REPOSITORY_SERVICE_URI") + "/projects", List.class);
		List<Project> projects = new ArrayList<Project>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object object : objects) {
			Project project = mapper.convertValue(object, new TypeReference<Project>() {
			});
			projects.add(project);
		}
		return projects;
	}

	public Project getProject(int projectId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", projectId);
		return template.getForObject(System.getenv("PROJECT_REPOSITORY_SERVICE_URI") + "/project/{id}", Project.class,
				params);
	}

	public Project createProject(ProjectDetails message) {
		RestTemplate template = new RestTemplate();
		return template.postForObject(System.getenv("PROJECT_REPOSITORY_SERVICE_URI") + "/project", message,
				Project.class);
	}

	public void changeProjectDetails(int projectId, ProjectDetails message) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", projectId);
		template.put(System.getenv("PROJECT_REPOSITORY_SERVICE_URI") + "/project/{id}", message, params);
	}

	public void deleteProject(int projectId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", projectId);
		template.delete(System.getenv("PROJECT_REPOSITORY_SERVICE_URI") + "/project/{id}", params);
	}

	public List<Sprint> getProjectSprints(int projectId) {
		return getProject(projectId).getSprints();
	}

	public List<Story> getProjectStories(int projectId) {
		RestTemplate template = new RestTemplate();
		Map<String, Integer> params = new HashMap<String, Integer>();
		params.put("id", projectId);
		return template.getForObject(System.getenv("STORY_REPOSITORY_SERVICE_URI") + "/stories?projectId={id}",
				new ArrayList().getClass(), params);
	}

}
