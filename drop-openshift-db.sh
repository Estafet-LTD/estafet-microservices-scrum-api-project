#!/bin/bash
# Executes the drop db schema
psql -h $POSTGRESQL_SERVICE_HOST -p $POSTGRESQL_SERVICE_PORT -U postgres project-api < drop-project-api-db.ddl
