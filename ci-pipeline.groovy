node("maven") {

	def project = "dev"
	def microservice = "project-api"

	stage("checkout") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-api-project"
	}

	stage("build and execute unit tests") {
		sh "mvn clean test"
		junit "**/target/surefire-reports/*.xml"	
	}

	stage("update the database schema") {
		sh "oc get pods --selector app=postgresql -o json -n ${project} > pods.json"
		def json = readFile('pods.json');
		def pod = new groovy.json.JsonSlurper().parseText(json).items[0].metadata.name
		sh "oc rsync --no-perms=true --include=\"*.ddl\" --exclude=\"*\" ./ ${pod}:/tmp -n ${project}"
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/drop-${microservice}-db.ddl\""
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/create-${microservice}-db.ddl\""
	}

	stage("build & deploy container") {
		openshiftBuild namespace: project, buildConfig: microservice, showBuildLogs: "true",  waitTime: "3000000"
	}
  	  
	stage("verify container deployment") {
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"	
	}

	stage("execute the container tests") {
		withEnv(
			[	"PROJECT_API_JDBC_URL=jdbc:postgresql://postgresql.${project}.svc:5432/${microservice}", 
				"PROJECT_API_DB_USER=postgres", 
				"PROJECT_API_DB_PASSWORD=welcome1",
				"PROJECT_API_SERVICE_URI=http://${microservice}.${project}.svc:8080",
				"JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616",
				"JBOSS_A_MQ_BROKER_USER=amq",
				"JBOSS_A_MQ_BROKER_PASSWORD=amq"
			]) {
				sh "mvn verify -P integration-test"
			}
			junit "**/target/failsafe-reports/*.xml"
	}
	
	stage("tag container as preparing for testing") {
		sh "oc get pods --selector app=postgresql -o json -n test > test-pods.json"
		def json = readFile('test-pods.json');
		def pod = new groovy.json.JsonSlurper().parseText(json).items[0].metadata.name
		sh "oc rsync --no-perms=true --include=\"*.ddl\" --exclude=\"*\" ./ ${pod}:/tmp -n test"
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'latest', destinationNamespace: 'test', destinationStream: microservice, destinationTag: 'PrepareForTesting'
	}

}

