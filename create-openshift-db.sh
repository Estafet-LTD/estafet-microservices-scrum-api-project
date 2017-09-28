#!/bin/bash
# Executes the create db schema
psql -h $POSTGRESQL_SERVICE_HOST -p $POSTGRESQL_SERVICE_PORT -U postgres project-api < create-project-api-db.ddl
