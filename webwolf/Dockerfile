FROM openjdk:8-jre-slim

RUN useradd --home-dir /home/webwolf --create-home -U webwolf

USER webwolf
RUN cd /home/webwolf/
COPY target/webwolf-8.0-SNAPSHOT.jar /home/webwolf/webwolf.jar
COPY start.sh /home/webwolf/start.sh