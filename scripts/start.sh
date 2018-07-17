#!/usr/bin/env bash

DATABASE_PORT=9001

checkDatabaseAvailable(){

  #for i in $(seq 1 5); do command && s=0 && break || s=$? && sleep 15; done; (exit $s)
  local started = $(netstat -lnt | grep ${DATABASE_PORT})
  echo $?
}

#java -Djava.security.egd=file:/dev/./urandom -jar home/webgoat/webgoat.jar --server.address=0.0.0.0
$(checkDatabaseAvailable)


#java -Djava.security.egd=file:/dev/./urandom -jar /home/webwolf/webwolf.jar --server.port=9090 --server.address=0.0.0.0


