#!/bin/bash

# Script to start WebWolf, it needs a valid database connection from WebGoat so we wait 8 seconds before starting
# WebWolf application

echo " Waiting for database to be available..."
sleep 8 && java -Djava.security.egd=file:/dev/./urandom -jar /home/webwolf/webwolf.jar $@