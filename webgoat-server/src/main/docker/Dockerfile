FROM openjdk:8-jre

RUN useradd --home-dir /home/webgoat --create-home -U webgoat

USER webgoat
RUN cd /home/webgoat/; mkdir -p .webgoat
COPY webgoat-server-8.0-SNAPSHOT.jar /home/webgoat/webgoat.jar
COPY webwolf-8.0-SNAPSHOT.jar /home/webgoat/webwolf.jar
COPY startup.sh /home/webgoat/startup.sh
RUN sudo chmod +x /home/webgoat/startup.sh

CMD ["/home/webgoat/startup.sh"]