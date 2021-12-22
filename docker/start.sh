#!/bin/bash

cd /home/webgoat 

function should_start_nginx() {
  if [[ -v "${SKIP_NGINX}" ]]; then
    return 1
  else
    for i in "${commandline_args[@]}" ; do [[ $i == "skip-nginx" ]] && return 1 ; done
  fi
  return 0
}

function nginx() {
  if should_start_nginx; then
    echo "Starting nginx..."
    service nginx start
  fi
}

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
   -jar webgoat.jar --server.address=0.0.0.0 > webgoat.log
}

function webwolf() {
  echo "Starting WebWolf..."
  java -Duser.home=/home/webgoat -Dfile.encoding=UTF-8 -jar webwolf.jar --server.address=0.0.0.0 > webwolf.log
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
  echo $'WebGoat and WebWolf successfully started...\n' >> webgoat.log
  echo "NOTE: port numbers mentioned below may vary depending on your port mappings while starting the Docker container" >> webgoat.log
  pidof nginx >/dev/null && echo "Browse to http://localhost to get started" >> webgoat.log || echo "Browse to http://localhost:8080/WebGoat or http://localhost:9090/WebWolf to get started." >> webgoat.log
}

function tail_log_file() {
  touch webgoat.log
  tail -300f webgoat.log
}

commandline_args=("$@")

nginx
webgoat &
webwolf &
write_start_message &
tail_log_file



