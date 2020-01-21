#!/bin/bash

cd /home/webgoat 
service nginx start
sleep 1
java -Dfile.encoding=UTF-8 -jar webgoat.jar --webgoat.build.version=$1 --server.address=0.0.0.0  > webgoat.log &

sleep 10
 
java -Dfile.encoding=UTF-8 -jar webwolf.jar --webgoat.build.version=$1 --server.address=0.0.0.0 > webwolf.log &

tail -300f webgoat.log
