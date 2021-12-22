# WebGoat 8: A deliberately insecure Web Application

[![Pull request build](https://github.com/WebGoat/WebGoat/actions/workflows/pr_build.yml/badge.svg?branch=develop)](https://github.com/WebGoat/WebGoat/actions/workflows/pr_build.yml)
[![java-jdk](https://img.shields.io/badge/java%20jdk-17-green.svg)](https://jdk.java.net/)
[![OWASP Labs](https://img.shields.io/badge/OWASP-Lab%20project-f7b73c.svg)](https://owasp.org/projects/)
[![GitHub release](https://img.shields.io/github/release/WebGoat/WebGoat.svg)](https://github.com/WebGoat/WebGoat/releases/latest)
[![Gitter](https://badges.gitter.im/OWASPWebGoat/community.svg)](https://gitter.im/OWASPWebGoat/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![Discussions](https://img.shields.io/github/discussions/WebGoat/WebGoat)](https://github.com/WebGoat/WebGoat/discussions)

# Introduction

WebGoat is a deliberately insecure web application maintained by [OWASP](http://www.owasp.org/) designed to teach web
application security lessons.

This program is a demonstration of common server-side application flaws. The
exercises are intended to be used by people to learn about application security and
penetration testing techniques.

**WARNING 1:** *While running this program your machine will be extremely
vulnerable to attack. You should disconnect from the Internet while using
this program.*  WebGoat's default configuration binds to localhost to minimize
the exposure.

**WARNING 2:** *This program is for educational purposes only. If you attempt
these techniques without authorization, you are very likely to get caught. If
you are caught engaging in unauthorized hacking, most companies will fire you.
Claiming that you were doing security research will not work as that is the
first thing that all hackers claim.*

# Installation instructions:

For more details check [the Contribution guide](/CONTRIBUTING.md)

## 1. Run using Docker

Every release is also published on [DockerHub](https://hub.docker.com/r/webgoat/goatandwolf).

The easiest way to start WebGoat as a Docker container is to use the all-in-one docker container. This is a docker image that has WebGoat and WebWolf running inside.

```shell

docker run -it -p 127.0.0.1:80:8888 -p 127.0.0.1:8080:8080 -p 127.0.0.1:9090:9090 -e TZ=Europe/Amsterdam webgoat/goatandwolf:v8.2.2
```

The landing page will be located at: http://localhost  
WebGoat will be located at: http://localhost:8080/WebGoat  
WebWolf will be located at: http://localhost:9090/WebWolf

**Important**: *Change the ports if necessary, for example use `127.0.0.1:7777:9090` to map WebWolf to `http://localhost:7777/WebGoat`*  

**Important**: *Choose the correct timezone, so that the docker container and your host are in the same timezone. As it is important for the validity of JWT tokens used in certain exercises.*


## 2. Standalone

Download the latest WebGoat and WebWolf release from [https://github.com/WebGoat/WebGoat/releases](https://github.com/WebGoat/WebGoat/releases)

```shell
java -Dfile.encoding=UTF-8 -Dserver.port=8080 -Dserver.address=localhost -Dhsqldb.port=9001 -jar webgoat-server-8.2.2.jar 
java -Dfile.encoding=UTF-8 -Dserver.port=9090 -Dserver.address=localhost -jar webwolf-8.2.2.jar
```

WebGoat will be located at: http://localhost:8080/WebGoat and   
WebWolf will be located at: http://localhost:9090/WebWolf (change ports if necessary)

## 3. Run from the sources

### Prerequisites:

* Java 17
* Your favorite IDE
* Git, or Git support in your IDE

Open a command shell/window:

```Shell
git clone git@github.com:WebGoat/WebGoat.git
```

Now let's start by compiling the project.

```Shell
cd WebGoat
git checkout <<branch_name>>
# On Linux/Mac:
./mvnw clean install 
# On Windows:
./mvnw.cmd clean install
```

Now we are ready to run the project. WebGoat 8.x is using Spring-Boot.

```Shell
# On Linux/Mac:
./mvnw -pl webgoat-server spring-boot:run
# On Widows:
./mvnw.cmd -pl webgoat-server spring-boot:run

```
... you should be running WebGoat on localhost:8080/WebGoat momentarily


To change the IP address add the following variable to the `WebGoat/webgoat-container/src/main/resources/application.properties file`:

```
server.address=x.x.x.x
```

## 4. Run with custom menu

For specialist only. There is a way to set up WebGoat with a personalized menu. You can leave out some menu categories or individual lessons by setting certain environment variables.

For instance running as a jar on a Linux/macOS it will look like this:
```Shell
export EXCLUDE_CATEGORIES="CLIENT_SIDE,GENERAL,CHALLENGE"
export EXCLUDE_LESSONS="SqlInjectionAdvanced,SqlInjectionMitigations"
java -jar webgoat-server/target/webgoat-server-v8.2.2-SNAPSHOT.jar
```
Or in a docker run it would (once this version is pushed into docker hub) look like this:
```Shell
docker run -d -p 80:8888 -p 8080:8080 -p 9090:9090 -e TZ=Europe/Amsterdam -e EXCLUDE_CATEGORIES="CLIENT_SIDE,GENERAL,CHALLENGE" -e EXCLUDE_LESSONS="SqlInjectionAdvanced,SqlInjectionMitigations" webgoat/goatandwolf
```
