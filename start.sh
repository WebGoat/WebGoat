#!/bin/bash

JARFILE=$(ls /home/ec2-user/build/target/*.jar)
echo "filename : $JARFILE" >> /home/ec2-user/build/startsh.log

CURRENT_PID=$(pgrep -f $JARFILE)
echo "currentpid : $CURRENT_PID" >> /home/ec2-user/build/startsh.log
kill -9 $CURRENT_PID 2>/dev/null
sleep 5

nohup java -jar $JARFILE > /dev/null 2> /dev/null < /dev/null &
