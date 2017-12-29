FROM openjdk:8-jre-slim

ARG webgoat_version=8.0-SNAPSHOT

RUN useradd --home-dir /home/webgoat --create-home -U webgoat

RUN apt-get update; apt-get install curl -y

COPY start.sh /home/webgoat/start.sh
RUN chmod +x /home/webgoat/start.sh

USER webgoat
RUN cd /home/webgoat/; mkdir -p .webgoat
COPY target/webgoat-server-${webgoat_version}.jar /home/webgoat/webgoat.jar
