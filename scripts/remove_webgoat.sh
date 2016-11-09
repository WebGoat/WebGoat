#! /bin/bash
set -x
DIR=/opt/apache-tomcat-6.0.47/webapps/
rm -rf $DIR/WebGoat
rm -rf $DIR/webgoat-container-7.0.1.war
echo "Removed $WEBGOAT"
