# Estafet Microservices Scrum Project Management API
Microservices api for "projects" within that scrum demo application.
## What is this?
This application is a microservice for managing project records within a scrum application. It is one of 8 microservices that form a composition for the scrum micro service demo application.

Each microservice has it's own git repository, but there is a master git repository that contains links to all of the repositories [here](https://github.com/Estafet-LTD/estafet-microservices-scrum).
## Getting Started
You can find a detailed explanation of how to install this (and other microservices) [here](https://github.com/Estafet-LTD/estafet-microservices-scrum#getting-started).
## API Interface

### Messaging

|Topic            |Direction |Event                                                      |Message Type       |
|-----------------|----------|-----------------------------------------------------------|-------------------|
|new.project.topic|Producer  |When a new project is created it is published to this topic|Project JSON Object|

### Project JSON object

```json
{
    "id": 1,
    "title": "A fresh project"
}
```

### Restful Operations

To retrieve an example the project object (useful for testing to see the microservice is online).

```
GET http://<project-api-uri>/api
```

To retrieve a project with id = 4

```
GET http://<project-api-uri>/project/4
```

To retrieve all of the projects

```
GET http://project-api/projects
```

To create a new project

```
POST http://<project-api-uri>/project
{
	"title" : "this is my project"
}
```

To change the title of a project

```
PUT http://<project-api-uri>/project/title
"change title"
```

To delete a project with id = 1

```
DELETE http://<project-api-uri>/project/1
```

## Environment Variables
```
JBOSS_A_MQ_BROKER_URL
JBOSS_A_MQ_BROKER_USER
JBOSS_A_MQ_BROKER_PASSWORD

PROJECT_API_JDBC_URL
PROJECT_API_DB_USER
PROJECT_API_DB_PASSWORD
```

