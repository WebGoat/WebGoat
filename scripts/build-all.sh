#!/usr/bin/env bash

cd ..

nc -zv 127.0.0.1 8080 2>/dev/null
SUCCESS=$?
nc -zv 127.0.0.1 9090 2>/dev/null
SUCCESS=${SUCCESS}$?

if [[ "${SUCCESS}" -eq 0 ]] ; then
  echo "WebGoat and or WebWolf are still running, please stop them first otherwise unit tests might fail!"
  exit 127
fi


sh mvnw clean install
if [[ "$?" -ne 0 ]] ; then
  exit y$?
fi

cd -
sh build_docker.sh
if [[ "$?" -ne 0 ]] ; then
  exit y$?
fi

while true; do
    read -p "Do you want to run docker-compose?" yn
    case ${yn} in
        [Yy]* ) sh clean-run-docker-compose.sh; break;;
        [Nn]* ) exit;;
        * ) echo "Please answer yes or no.";;
    esac
done
