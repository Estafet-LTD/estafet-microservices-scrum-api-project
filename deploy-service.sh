mvn clean install -P local
cp target/estafet-microservices-scrum-api-project-*.war $WILDFLY_INSTALL/standalone/deployments
