FROM openjdk:11.0.1-jre-slim-stretch

ARG webgoat_version=v8.0.0-SNAPSHOT

RUN \
  apt-get update && apt-get install && \
  useradd --home-dir /home/webgoat --create-home -U webgoat

USER webgoat
RUN cd /home/webgoat/; mkdir -p .webgoat-${webgoat_version}
COPY target/webgoat-server-${webgoat_version}.jar /home/webgoat/webgoat.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/home/webgoat/webgoat.jar"]
CMD ["--server.port=8080", "--server.address=0.0.0.0"]