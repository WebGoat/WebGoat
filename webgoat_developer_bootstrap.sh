#!/bin/bash
# Bootstrap the setup of WebGoat for developer use in Linux/Mac machines
# This script will clone the necessary git repositories, call the maven goals
# in the order the are needed and launch tomcat listening on localhost:8080
# Happy hacking !

# Clone WebGoat and WebGoat-lessons from GitHub if they don't exist
if [ ! -d "Webgoat" ]; then
  git clone https://github.com/WebGoat/WebGoat.git
fi
if [ ! -d "Webgoat-Lessons" ]; then
  git clone https://github.com/WebGoat/WebGoat-Lessons.git
fi

# Compile and Install the WebGoat lesson server
mvn -file WebGoat/pom.xml clean compile install

# Compile and package the WebGoat Lessons
mvn -file WebGoat-Lessons/pom.xml package

# Copy the Lessons into the WebGoat-Container
cp -fa ./WebGoat-Lessons/target/plugins/*.jar ./WebGoat/webgoat-container/src/main/webapp/plugin_lessons/

# Start WebGoat using the maven tomcat7:run-war goal
printf "\n"
printf "\n"
printf "\n"
printf "\n"
printf "\n"
echo "-----------------------------------------------------------------------------------"
echo "____________________ Starting WebGoat using the embbebed Tomcat ___________________"
echo "###################################################################################"
echo "### Open a web broser an navigate to http://localhost:8080/WebGoat/"
echo "### STDOUT and STDERR logs are captured in ./webgoat_developer_bootstrap.log"
echo "### To stop the Tomcat execution, press CTRL + C"
echo "### If you close this terminal window, Tomcat and WebGoat will stop running"
echo "##################################### HAPPY HACKING! ##############################"
echo "-----------------------------------------------------------------------------------"
printf "\n"
printf "\n"
printf "\n"
printf "\n"
printf "\n"
mvn -file WebGoat/pom.xml -pl webgoat-container tomcat7:run-war &> webgoat_developer_bootstrap.log
