package com.estafet.microservices.api.project.container.tests;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.estafet.microservices.api.project.model.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class })
public class ITProjectTest {

	NewProjectTopic topic;

	@Before
	public void before() throws Exception {
		RestAssured.baseURI = System.getenv("PROJECT_API_SERVICE_URI");
		topic = new NewProjectTopic();
	}

	@After
	public void after() throws Exception {
		topic.closeConnection();
	}

	@Test
	public void testProjectAPI() {
		get("/api").then()
			.body("id", equalTo(1))
			.body("title", equalTo("my project"));
	}

	@Test
	@DatabaseSetup("ITProjectTest-data.xml")
	public void testGetProject() {
		get("/project/2000").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", equalTo(2000))
			.body("title", equalTo("My Project #7082"))
			.body("noSprints", equalTo(5));
	}

	@Test
	@DatabaseSetup("ITProjectTest-data.xml")
	public void testGetProjects() {
		get("/projects").then()
			.body("id", hasItems(1000, 2000));
	}

	@Test
	@DatabaseSetup("ITProjectTest-data.xml")
	public void testCreateProject() throws Exception {
		given().contentType(ContentType.JSON)
			.body("{\"title\":\"My Project #1\",\"noSprints\":5,\"sprintLengthDays\":5}")
		.when()
			.post("/project")
		.then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is(1))
			.body("title", equalTo("My Project #1"));
		
		get("/project/1").then()
			.statusCode(HttpURLConnection.HTTP_OK)
			.body("id", is(1))
			.body("title", is("My Project #1"))
			.body("noSprints", is(5))
			.body("sprintLengthDays", is(5));
		
		Project project = new ObjectMapper().readValue(topic.consumeMessage(), Project.class);
		assertThat(project.getId(), is(1));
		assertThat(project.getTitle(), is("My Project #1"));
		assertThat(project.getNoSprints(), is(5));
		assertThat(project.getSprintLengthDays(), is(5));
	}

}
