@NonCPS
def getImage(json, microservice) {
	def item = new groovy.json.JsonSlurper().parseText(json).items.find{it.metadata.name == microservice}
	return item.status.dockerImageRepository
}

@NonCPS
boolean deploymentConfigurationExists(json, microservice) {
	return new groovy.json.JsonSlurper().parseText(json).items.find{it.metadata.name == microservice} != null
}

@NonCPS
def nodeNames() {
 	return jenkins.model.Jenkins.instance.nodes.collect { node -> node.name }
}

def project = "test"
def microservice = "project-api"

node('maven') {

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
		sh "oc get is -o json -n test > is.json"
		def is = readFile ('is.json')
		def image = getImage (is, microservice)
		def template = readFile ('test-deployment-config.json').replaceAll(/\$\{image\}/, image).replaceAll(/\$\{microservice\}/, microservice)
		sh "oc get dc -o json -n test > dc.json"
		def dc = readFile ('dc.json')
		if (deploymentConfigurationExists (dc, microservice)) {
			openshiftDeploy namespace: project, depCfg: microservice
		} else {
			openshiftCreateResource namespace:project, jsonyaml:template
		}
	}
  	  
	stage("verify test container deployment") {
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "500000"	
	}
	
	stage("execute acceptance tests") {
		cleanWs()
		build job: "cicd-qa-pipeline", parameters: [ new NodeParameterValue ("TARGET_NODE", "description", env.NODE_NAME)  ]
	}
	
	stage("tag container as testing successful") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'PrepareForTesting', destinationNamespace: 'prod', destinationStream: microservice, destinationTag: 'TestingSuccessful'
	}

}

/*
node('maven') {

	stage("checkout acceptance tests") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-qa"
	}

	stage("execute acceptance tests") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-qa"
		sh "mvn clean install"
		//junit "**/target/surefire-reports/*.xml"	
		//build job: "cicd-qa-pipeline"
	}
	
	stage("tag container as testing successful") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'PrepareForTesting', destinationNamespace: 'prod', destinationStream: microservice, destinationTag: 'TestingSuccessful'
	}

}
*/

