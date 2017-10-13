#!/bin/bash
# Executes the drop db schema
psql -d project-api -U postgres -f drop-project-api-db.ddl
