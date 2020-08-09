FROM openjdk:11.0.1-jre-slim-stretch

ARG webgoat_version=v8.0.0-SNAPSHOT
ENV webgoat_version_env=${webgoat_version}

RUN apt-get update && apt-get install 
RUN useradd --home-dir /home/webgoat --create-home -U webgoat
RUN cd /home/webgoat/; 
RUN chgrp -R 0 /home/webgoat
RUN chmod -R g=u /home/webgoat
RUN apt-get -y install apt-utils nginx

USER webgoat

COPY nginx.conf /etc/nginx/nginx.conf
COPY index.html /usr/share/nginx/html/
COPY webgoat-server-${webgoat_version}.jar /home/webgoat/webgoat.jar
COPY webwolf-${webgoat_version}.jar /home/webgoat/webwolf.jar
COPY start.sh /home/webgoat

EXPOSE 8080
EXPOSE 9090

ENV WEBGOAT_PORT 8080
ENV WEBGOAT_SSLENABLED false

ENV GOATURL https://127.0.0.1:$WEBGOAT_PORT
ENV WOLFURL http://127.0.0.1:9090


WORKDIR /home/webgoat
ENTRYPOINT /bin/bash /home/webgoat/start.sh $webgoat_version_env
