FROM openjdk:15.0.2-slim

ARG webgoat_version=8.2.0-SNAPSHOT
ENV webgoat_version_env=${webgoat_version}

RUN apt-get update
RUN useradd -ms /bin/bash webgoat
RUN apt-get -y install apt-utils nginx

USER webgoat

COPY --chown=webgoat nginx.conf /etc/nginx/nginx.conf
COPY --chown=webgoat index.html /usr/share/nginx/html/
COPY --chown=webgoat target/webgoat-server-${webgoat_version}.jar /home/webgoat/webgoat.jar
COPY --chown=webgoat target/webwolf-${webgoat_version}.jar /home/webgoat/webwolf.jar
COPY --chown=webgoat start.sh /home/webgoat

EXPOSE 8080
EXPOSE 9090

ENV WEBGOAT_PORT 8080
ENV WEBGOAT_SSLENABLED false

ENV GOATURL https://127.0.0.1:$WEBGOAT_PORT
ENV WOLFURL http://127.0.0.1:9090


WORKDIR /home/webgoat
ENTRYPOINT /bin/bash /home/webgoat/start.sh $webgoat_version_env
