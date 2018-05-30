#!/bin/bash

WEBGOAT_HOME=$(pwd)/../

cd ${WEBGOAT_HOME}/webgoat-server
docker build -t webgoat/webgoat-8.0 .

cd ${WEBGOAT_HOME}/webwolf
docker build -t webgoat/webwolf .

