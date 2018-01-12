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

# Run Instructions:

## 1. Run using Docker

From time to time we publish a new development preview of WebGoat 8 on Docker HUB, you can download this version
[https://hub.docker.com/r/webgoat/webgoat-8.0/](https://hub.docker.com/r/webgoat/webgoat-8.0/).
First install Docker, then open a command shell/window and type:

```Shell
docker pull webgoat/webgoat-8.0
docker run -p 8080:8080 -it webgoat/webgoat-8.0 /home/webgoat/start.sh 
```

Wait for the Docker container to start, and run `docker ps` to verify it's running.

- If you are using `docker-machine`, verify the machine IP using `docker-machine env`
- If you are using `boot2docker` on OSX, verify the IP by running `docker network inspect bridge`
- Otherwise, the host will be bound to localhost

Once you have the IP and port, you'll want to navigate to the `/WebGoat` path in the URL. For example:

```
http://192.168.99.100:8080/WebGoat
```

Here you'll be able to register a new user and get started.

_Please note: this version may not be completely in sync with the develop branch._

## 2. Standalone 

Download the latest WebWolf release from [https://github.com/WebGoat/WebGoat/releases](https://github.com/WebGoat/WebGoat/releases)

```Shell
java -jar webgoat-server-<<version>>.jar
```

By default WebGoat starts at port 8080 in order to change this use the following property:

```Shell
java -jar webgoat-server-<<version>>.jar --server.port=9090
```

You can specify one of the following arguments when starting WebGoat:

```Shell
java -jar webgoat-server-<<version>>.jar --server.port=9090 --server.address=x.x.x.x
```

This will start WebGoat on a different port and/or different address.


## 3. Run from the sources

### Prerequisites:

* Java 8
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

# Vagrant

We supply a complete development environment using Vagrant, to run WebGoat with Vagrant you must first have Vagrant and Virtualbox installed.

```shell
   $ cd WebGoat/webgoat-images/vagrant-training
   $ vagrant up
```

Once the provisioning is complete login to the Virtualbox with username vagrant and password vagrant.
The source code will be available in the home directory.


# Building a new Docker image

NOTE: Travis will create a new Docker image automatically when making a new release.

WebGoat now has Docker support for x86 and ARM (raspberry pi).
### Docker on x86
On x86 you can build a container with the following commands:

```Shell
cd WebGoat/
mvn install
cd webgoat-server
docker build -t webgoat/webgoat-8.0 .
docker tag webgoat/webgoat-8.0 webgoat/webgoat-8.0:8.0
docker login
docker push webgoat/webgoat-8.0
```

### Docker on ARM (Raspberry Pi)
On a Raspberry Pi (it has yet been tested with a Raspberry Pi 3 and the hypriot Docker image) you need to build JFFI for
ARM first. This is needed by the docker-maven-plugin ([see here](https://github.com/spotify/docker-maven-plugin/issues/233)):

```Shell
sudo apt-get install build-essential
git clone https://github.com/jnr/jffi.git
cd jffi
ant jar
cd build/jni
sudo cp libjffi-1.2.so /usr/lib
```

When you have done this you can build the Docker container using the following commands:

```Shell
cd WebGoat/
mvn install
cd webgoat-server
mvn docker:build -Drpi=true
docker tag webgoat/webgoat-8.0 webgoat/webgoat-8.0:8.0
docker login
docker push webgoat/webgoat-8.0
```
