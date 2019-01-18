#!/usr/bin/env bash

cd ..
docker-compose rm -f
docker-compose -f docker-compose-local.yml up
