#!/bin/bash

cd /home/webgoat 
service nginx start
sleep 1
echo "Starting WebGoat..."
java -Duser.home=/home/webgoat -Dfile.encoding=UTF-8 -jar webgoat.jar --webgoat.build.version=$1 --server.address=0.0.0.0  > webgoat.log &

sleep 10

echo "Starting WebWolf..."
java -Duser.home=/home/webgoat -Dfile.encoding=UTF-8 -jar webwolf.jar --webgoat.build.version=$1 --server.address=0.0.0.0 > webwolf.log &

echo "Browse to http://localhost" to get started >> webgoat.log

tail -300f webgoat.log
