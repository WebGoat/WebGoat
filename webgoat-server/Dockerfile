FROM openjdk:8-jre-slim

RUN useradd --home-dir /home/webgoat --create-home -U webgoat

RUN apt-get update; apt-get install curl -y


COPY start.sh /home/webgoat/start.sh
RUN chmod +x /home/webgoat/start.sh

USER webgoat
RUN mkdir -p /home/webgoat/.embedmongo/linux
RUN curl -o /home/webgoat/.embedmongo/linux/mongodb-linux-x86_64-3.2.2.tgz https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-3.2.2.tgz
RUN cd /home/webgoat/; mkdir -p .webgoat
COPY target/webgoat-server-8.0-SNAPSHOT.jar /home/webgoat/webgoat.jar
