FROM openjdk:8-jre

RUN useradd --home-dir /home/webgoat --create-home -U webgoat

USER webgoat
RUN cd /home/webgoat/; mkdir -p .webgoat
COPY webgoat-server-8.0-SNAPSHOT.jar /home/webgoat/webgoat.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/webgoat/webgoat.jar"]