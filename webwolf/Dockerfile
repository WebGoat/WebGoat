FROM openjdk:8-jre-slim

ARG webwolf_version=8.0-SNAPSHOT

RUN useradd --home-dir /home/webwolf --create-home -U webwolf

RUN apt-get update; apt-get install curl -y

COPY start.sh /home/webwolf/start.sh
RUN chmod +x /home/webwolf/start.sh

USER webwolf
COPY target/webwolf-${webwolf_version}.jar /home/webwolf/webwolf.jar
