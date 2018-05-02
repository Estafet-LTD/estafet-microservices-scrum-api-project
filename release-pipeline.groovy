node() {

	def project = "test"
	def microservice = "project-api"

	stage("checkout") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-api-project"
	}

	stage("update the test database schema") {
		sh "oc get pods --selector app=postgresql -o json -n ${project} > pods.json"
		def json = readFile('pods.json');
		def pod = new groovy.json.JsonSlurper().parseText(json).items[0].metadata.name
		sh "oc rsync --no-perms=true --include=\"*.ddl\" --exclude=\"*\" ./ ${pod}:/tmp -n ${project}"	
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/drop-${microservice}-db.ddl\""
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/create-${microservice}-db.ddl\""
	}
	
	stage("deploy the test container") {
		sh "oc get is -o json -n ${project} > is.json"
		def json = readFile('is.json');
		def item = new groovy.json.JsonSlurper().parseText(json).items.find{it.metadata.name == microservice}
		def image = item.status.dockerImageRepository
        println image
        sh "oc new-app ${image} -e JAEGER_SAMPLER_TYPE=const -e JAEGER_SAMPLER_PARAM=1 -e JAEGER_SAMPLER_MANAGER_HOST_PORT=jaeger-agent.${project}.svc:5778 -e JAEGER_AGENT_HOST=jaeger-agent.${project}.svc -e PROJECT_API_JDBC_URL=jdbc:postgresql://postgresql.${project}.svc:5432/${microservice} -e PROJECT_API_DB_USER=postgres -e PROJECT_API_DB_PASSWORD=welcome1 -e JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616 -e JBOSS_A_MQ_BROKER_USER=amq -e JBOSS_A_MQ_BROKER_PASSWORD=amq -n ${project}"
		//sh "oc create dc ${microservice} --image=${image}:PrepareForTesting -n ${project}"
		//sh "oc set env dc/${microservice} -e JAEGER_SAMPLER_TYPE=const -e JAEGER_SAMPLER_PARAM=1 -e JAEGER_SAMPLER_MANAGER_HOST_PORT=jaeger-agent.${project}.svc:5778 -e JAEGER_AGENT_HOST=jaeger-agent.${project}.svc -n ${project}"
		//sh "oc set env dc/${microservice} -e PROJECT_API_JDBC_URL=jdbc:postgresql://postgresql.${project}.svc:5432/${microservice} -e PROJECT_API_DB_USER=postgres -e PROJECT_API_DB_PASSWORD=welcome1 -n ${project}" 
		//sh "oc set env dc/${microservice} -e JBOSS_A_MQ_BROKER_URL=tcp://broker-amq-tcp.${project}.svc:61616 -e JBOSS_A_MQ_BROKER_USER=amq -e JBOSS_A_MQ_BROKER_PASSWORD=amq -n ${project}"
		//openshiftDeploy namespace: project, deploymentConfig: microservice, waitTime: '300000'
	}
  	  
	//stage("verify test container deployment") {
	//	openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"	
	//}

}

