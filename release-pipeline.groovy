@NonCPS
def getImage(json, microservice) {
	def item = new groovy.json.JsonSlurper().parseText(json).items.find{it.metadata.name == microservice}
	return item.status.dockerImageRepository
}

@NonCPS
boolean deploymentConfigurationExists(json, microservice) {
	return new groovy.json.JsonSlurper().parseText(json).items.find{it.metadata.name == microservice} != null
}

def project = "test"
def microservice = "project-api"

node {

	stage("checkout") {
		git branch: "master", url: "https://github.com/Estafet-LTD/estafet-microservices-scrum-api-project"
	}

	stage("update database") {
		sh "oc get pods --selector app=postgresql -o json -n ${project} > pods.json"
		def json = readFile('pods.json')
		def pod = new groovy.json.JsonSlurper().parseText(json).items[0].metadata.name
		sh "oc rsync --no-perms=true --include=\"*.ddl\" --exclude=\"*\" ./ ${pod}:/tmp -n ${project}"	
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/drop-${microservice}-db.ddl\""
		sh "oc exec ${pod}  -n ${project} -- /bin/sh -i -c \"psql -d ${microservice} -U postgres -f /tmp/create-${microservice}-db.ddl\""
	}
	
	stage("deploy container") {
		def is = sh "oc get is -o json -n ${project} > is.json"
		def is = readFile('is.json')
		def image = getImage (is, microservice)
		def template = readFile ('test-deployment-config.json').replaceAll(/\$\{image\}/, image).replaceAll(/\$\{microservice\}/, microservice)
		sh "oc get dc -o json -n test > dc.json"
		def dc = readFile ('dc.json')
		if (deploymentConfigurationExists (dc, microservice)) {
			openshiftDeploy namespace: project, depCfg: microservice
		} else {
			openshiftCreateResource namespace:project, jsonyaml:template
		}
		openshiftVerifyDeployment namespace: project, depCfg: microservice, replicaCount:"1", verifyReplicaCount: "true", waitTime: "600000"
	}
  	  
	stage("run acceptance tests") {
		try {
			build job: "cicd-qa-pipeline"
		} catch (Exception e) {
			sleep time: 20
		}
		openshiftVerifyBuild namespace: "cicd", bldCfg: "cicd-qa-pipeline", waitTime: "600000"
	}
		
	stage("tag container as 'TestingSuccessful'") {
		openshiftTag namespace: project, srcStream: microservice, srcTag: 'PrepareForTesting', destinationNamespace: 'prod', destinationStream: microservice, destinationTag: 'TestingSuccessful'
	}

}



