FROM openjdk:8-jre-slim

ARG webwolf_version=v8.0.0.SNAPSHOT

RUN \
  apt-get update && apt-get install && \
  useradd --home-dir /home/webwolf --create-home -U webwolf

USER webwolf
COPY target/webwolf-${webwolf_version}.jar /home/webwolf/webwolf.jar

EXPOSE 9090
