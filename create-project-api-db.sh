#!/bin/bash
# Executes the create db schema
psql -d project-api -U postgres -f create-project-api-db.ddl
