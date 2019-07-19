# WebGoat 8: A deliberately insecure Web Application

[![Build Status](https://travis-ci.org/WebGoat/WebGoat.svg?branch=develop)](https://travis-ci.org/WebGoat/WebGoat)
[![Coverage Status](https://coveralls.io/repos/WebGoat/WebGoat/badge.svg?branch=develop&service=github)](https://coveralls.io/github/WebGoat/WebGoat?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/b69ee3a86e3b4afcaf993f210fccfb1d)](https://www.codacy.com/app/dm/WebGoat)
[![Dependency Status](https://www.versioneye.com/user/projects/562da95ae346d7000e0369aa/badge.svg?style=flat)](https://www.versioneye.com/user/projects/562da95ae346d7000e0369aa)
[![OWASP Labs](https://img.shields.io/badge/owasp-lab%20project-f7b73c.svg)](https://www.owasp.org/index.php/OWASP_Project_Inventory#tab=Labs_Projects) 
[![GitHub release](https://img.shields.io/github/release/WebGoat/WebGoat.svg)](https://github.com/WebGoat/WebGoat/releases/latest) 

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

# Installation Instructions:

## 1. Standalone 

Download the latest WebGoat release from [https://github.com/WebGoat/WebGoat/releases](https://github.com/WebGoat/WebGoat/releases)

```Shell
java -jar webgoat-server-8.0.0.VERSION.jar [--server.port=8080] [--server.address=localhost]
```

The latest version of WebGoat needs Java 11. By default WebGoat starts on port 8080 with `--server.port` you can specify a different port. With `server.address` you
can bind it to a different address (default localhost)


## 2. Run using Docker

Every release is also published on [DockerHub]((https://hub.docker.com/r/webgoat/webgoat-8.0/)).

### Using docker-compose

The easiest way to start WebGoat as a Docker container is to use the `docker-compose.yml` [file](https://raw.githubusercontent.com/WebGoat/WebGoat/develop/docker-compose.yml) 
from our Github repository. This will start both containers and it also takes care of setting up the
connection between WebGoat and WebWolf.

```shell
curl https://raw.githubusercontent.com/WebGoat/WebGoat/develop/docker-compose.yml | docker-compose -f - up
```

**Important**: the current directory on your host will be mapped into the container for keeping state.

Using the `docker-compose` file will simplify getting WebGoat and WebWolf up and running.


## 3. Run from the sources

### Prerequisites:

* Java 11
* Maven > 3.2.1
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
mvn clean install
```

Now we are ready to run the project. WebGoat 8.x is using Spring-Boot.

```Shell
mvn -pl webgoat-server spring-boot:run
```
... you should be running webgoat on localhost:8080/WebGoat momentarily


To change IP address add the following variable to WebGoat/webgoat-container/src/main/resources/application.properties file

```
server.address=x.x.x.x
```

# Building a new Docker image

NOTE: Travis will create a new Docker image automatically when making a new release.

```Shell
cd WebGoat/
mvn install
cd webgoat-server
docker build -t webgoat/webgoat-8.0 .
docker tag webgoat/webgoat-8.0 webgoat/webgoat-8.0:8.0
docker login
docker push webgoat/webgoat-8.0
```

# Run Instructions:

Once installed connect to http://localhost:8080/WebGoat and http://localhost:9090/WebWolf
