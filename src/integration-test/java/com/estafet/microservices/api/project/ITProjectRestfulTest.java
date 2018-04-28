package com.estafet.microservices.api.project;

import static org.junit.Assert.*;
import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import io.restassured.RestAssured;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
	DbUnitTestExecutionListener.class })
public class ITProjectRestfulTest {

	@Before
	public void before() {
		RestAssured.baseURI = System.getenv("PROJECT_API_SERVICE_URI");
	}

	@Test
	public void testProjectAPI() {
		get("/api").then().body("id", equalTo(1));
		get("/api").then().body("title", equalTo("my project"));
	}

	@Test
	@DatabaseSetup("sampleData.xml")
	public void testGetProject() {
		fail("Not yet implemented");
	}

	@Test
	@DatabaseSetup("sampleData.xml")
	public void testGetProjects() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateProject() {
		fail("Not yet implemented");
	}

	@Test
	@DatabaseSetup("sampleData.xml")
	public void testDeleteProject() {
		fail("Not yet implemented");
	}

}
