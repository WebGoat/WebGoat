#! /bin/bash
set -x
WEBGOAT=/opt/apache-tomcat-6.0.47/webapps/WebGoat
rm -rf $WEBGOAT
echo "Removed $WEBGOAT"
