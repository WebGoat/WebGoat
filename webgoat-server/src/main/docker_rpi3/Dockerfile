# Baseimage specially for raspberry pi usage
FROM resin/rpi-raspbian:jessie
VOLUME /tmp
# Installing openjdk-8-headless like in the standard Webgoat Docker container
RUN apt-get update && apt-get install -y \
      openjdk-8-jre-headless
RUN cd /root; mkdir -p .webgoat
ADD webgoat-server-8.0-SNAPSHOT.jar webgoat.jar
RUN sh -c 'touch /webgoat.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/webgoat.jar"]