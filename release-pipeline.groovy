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
		println ${image}
		//sh "oc create dc ${microservice} --image=${image}:PrepareForTesting"
		//openshiftDeploy namespace: project, deploymentConfig: microservice, waitTime: '300000'
	}
  	  
	//stage("verify test container deployment") {
	//	openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "300000"	
	//}

}

