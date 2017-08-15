FROM openjdk:8-jre

RUN useradd --home-dir /home/webgoat --create-home -U webgoat

USER webgoat

RUN mkdir -p /home/webgoat/.embedmongo/linux
RUN curl -o /home/webgoat/.embedmongo/linux/mongodb-linux-x86_64-3.2.2.tgz https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-3.2.2.tgz
RUN cd /home/webgoat/; mkdir -p .webgoat
COPY webgoat-server-8.0-SNAPSHOT.jar /home/webgoat/webgoat.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/webgoat/webgoat.jar"]