FROM openjdk:8-jre-slim

ARG webwolf_version=v8.0.0.SNAPSHOT

RUN \
  apt-get update && apt-get install && \
  useradd --home-dir /home/webwolf --create-home -U webwolf

USER webwolf
COPY target/webwolf-${webwolf_version}.jar /home/webwolf/webwolf.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/home/webwolf/webwolf.jar", "--server.port=9090", "--server.address=0.0.0.0"]

EXPOSE 9090
