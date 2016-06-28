FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD webgoat-container-7.1-SNAPSHOT.war webgoat.jar
RUN sh -c 'touch /webgoat.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/webgoat.jar"]