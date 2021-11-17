#!/bin/bash

cd /home/webgoat 
service nginx start
sleep 1
echo "Starting WebGoat...."

java \
 -Duser.home=/home/webgoat \
 -Dfile.encoding=UTF-8 \
 --add-opens java.base/java.lang=ALL-UNNAMED \
 --add-opens java.base/java.util=ALL-UNNAMED \
 --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
 --add-opens java.base/java.text=ALL-UNNAMED \
 --add-opens java.desktop/java.beans=ALL-UNNAMED \
 --add-opens java.desktop/java.awt.font=ALL-UNNAMED \
 --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
 --add-opens java.base/java.io=ALL-UNNAMED \
 -jar webgoat.jar --server.address=0.0.0.0 > webgoat.log &

echo "Starting WebWolf..."
java -Duser.home=/home/webgoat -Dfile.encoding=UTF-8 -jar webwolf.jar --server.address=0.0.0.0 > webwolf.log &

echo "Browse to http://localhost to get started" >> webgoat.log

exec tail -300f webgoat.log
