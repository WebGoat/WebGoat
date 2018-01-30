FROM openjdk:8-jre-slim

ARG webgoat_version=8.0-SNAPSHOT

RUN \
  apt-get update && apt-get install && \
  useradd --home-dir /home/webgoat --create-home -U webgoat && \
  cd /home/webgoat/; mkdir -p .webgoat

USER webgoat
COPY target/webgoat-server-${webgoat_version}.jar /home/webgoat/webgoat.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/home/webgoat/webgoat.jar", "--server.address=0.0.0.0"]

EXPOSE 8080