FROM eclipse-temurin:17_35-jdk-focal

RUN apt-get update
RUN useradd -ms /bin/bash webgoat
RUN apt-get -y install apt-utils nginx
RUN chgrp -R 0 /home/webgoat
RUN chmod -R g=u /home/webgoat

USER webgoat

COPY --chown=webgoat nginx.conf /etc/nginx/nginx.conf
COPY --chown=webgoat index.html /usr/share/nginx/html/
COPY --chown=webgoat target/webgoat-server-*.jar /home/webgoat/webgoat.jar
COPY --chown=webgoat target/webwolf-*.jar /home/webgoat/webwolf.jar
COPY --chown=webgoat start.sh /home/webgoat
RUN chmod +x /home/webgoat/start.sh

EXPOSE 8080
EXPOSE 9090

WORKDIR /home/webgoat
ENTRYPOINT ["./start.sh"]
