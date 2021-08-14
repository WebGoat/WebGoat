FROM openjdk:16-slim

ARG webgoat_version=8.2.1-SNAPSHOT
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

WORKDIR /home/webgoat
ENTRYPOINT /bin/bash /home/webgoat/start.sh $webgoat_version_env
