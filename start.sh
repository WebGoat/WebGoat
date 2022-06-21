#!/bin/bash

cd /home/webgoat 

function webgoat() {
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
   --add-opens java.base/java.util=ALL-UNNAMED \
   -Dwebgoat.host=0.0.0.0 -Dwebwolf.host=0.0.0.0 -Dwebgoat.port=8080 -Dwebwolf.port=9090 \
   -jar webgoat.jar > webgoat.log
}

function write_start_message() {
  until $(curl --output /dev/null --silent --head --fail http://0.0.0.0:8080/WebGoat/health); do
    sleep 2
  done
  echo "
    __          __       _        _____                   _
    \ \        / /      | |      / ____|                 | |
     \ \  /\  / /  ___  | |__   | |  __    ___     __ _  | |_
      \ \/  \/ /  / _ \ | '_ \  | | |_ |  / _ \   / _' | | __|
       \  /\  /  |  __/ | |_) | | |__| | | (_) | | (_| | | |_
        \/  \/    \___| |_.__/   \_____|  \___/   \__,_|  \__|
  " >> webgoat.log
  echo $'WebGoat successfully started...\n' >> webgoat.log
  echo "NOTE: port numbers mentioned below may vary depending on your port mappings while starting the Docker container" >> webgoat.log
  echo "Browse to http://localhost:8080/WebGoat to get started." >> webgoat.log
}

function tail_log_file() {
  touch webgoat.log
  tail -300f webgoat.log
}

commandline_args=("$@")

webgoat &
write_start_message &
tail_log_file



