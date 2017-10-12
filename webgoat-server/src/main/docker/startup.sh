#!/bin/sh

java -Djava.security.egd=file:/dev/./urandom -jar /home/webgoat/webgoat.jar &
echo "Waiting for WebGoat to start..."
sleep 20
java -Djava.security.egd=file:/dev/./urandom -jar /home/webgoat/webwolf.jar
