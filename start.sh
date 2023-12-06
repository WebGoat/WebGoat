#!/bin/bash
cd /home/ec2-user/build/
java -jar webgoat-2023.6-SNAPSHOT.jar —server.port=9090 —server.address=43.200.16.60 
