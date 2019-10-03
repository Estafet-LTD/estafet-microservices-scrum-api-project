node("maven") {

	def project = "build"
	def microservice = "project-api"

	currentBuild.description = "Build a container from the source, then execute unit and container integration tests before promoting the container as a release candidate for acceptance testing."

	properties([
	  parameters([
	     string(name: 'GITHUB'),
	  ])
	])

	stage("checkout") {
		git branch: "master", url: "https://github.com/${params.GITHUB}/estafet-microservices-scrum-api-project"
	}

	stage("unit tests") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
	      sh "mvn clean test"
	    } 
	}
	
	stage("prepare the database") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
	      sh "mvn clean package -P db -Dmaven.test.skip=true -Dproject=build"
	    } 
	}
	
	stage("reset a-mq to purge topics") {
		openshiftDeploy namespace: project, depCfg: "broker-amq", showBuildLogs: "true",  waitTime: "3000000"
		openshiftVerifyDeployment namespace: project, depCfg: "broker-amq", replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"
	}

	stage("build & deploy container") {
		openshiftBuild namespace: project, buildConfig: microservice, showBuildLogs: "true",  waitTime: "300000"
		sh "oc set env dc/${microservice} JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616 -n ${project}"
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"
		sleep time:120 
	}

	stage("execute the container tests") {
			withEnv(
				[	"PROJECT_API_JDBC_URL=jdbc:postgresql://postgresql.${project}.svc:5432/${project}-${microservice}", 
					"PROJECT_API_DB_USER=postgres", 
					"PROJECT_API_DB_PASSWORD=welcome1",
					"PROJECT_API_SERVICE_URI=http://${microservice}.${project}.svc:8080",
					"JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616",
					"JBOSS_A_MQ_BROKER_USER=amq",
					"JBOSS_A_MQ_BROKER_PASSWORD=amq"
				]) {
			withMaven(mavenSettingsConfig: 'microservices-scrum') {
					try {
						sh "mvn clean verify -P integration-test"
					} finally {
						sh "oc set env dc/${microservice} JBOSS_A_MQ_BROKER_URL=tcp://localhost:61616 -n ${project}"
					}
			}
		}
	}

	stage("deploy snapshots") {
		withMaven(mavenSettingsConfig: 'microservices-scrum') {
 			sh "mvn clean deploy -Dmaven.test.skip=true"
		} 
	}	
	
	stage("promote to test") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'latest', destinationNamespace: 'test', destinationStream: microservice, destinationTag: 'PrepareForTesting'
	}

}
