# WebGoat: A deliberately insecure Web Application

[![Build Status](https://travis-ci.org/WebGoat/WebGoat.svg?branch=develop)](https://travis-ci.org/WebGoat/WebGoat)
[![Coverage Status](https://coveralls.io/repos/WebGoat/WebGoat/badge.svg?branch=master&service=github)](https://coveralls.io/github/WebGoat/WebGoat?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/b69ee3a86e3b4afcaf993f210fccfb1d)](https://www.codacy.com/app/dm/WebGoat)
[![Dependency Status](https://www.versioneye.com/user/projects/562da95ae346d7000e0369aa/badge.svg?style=flat)](https://www.versioneye.com/user/projects/562da95ae346d7000e0369aa)
[![OWASP Labs](https://img.shields.io/badge/owasp-labs-orange.svg)](https://www.owasp.org/index.php/OWASP_Project_Inventory#tab=Labs_Projects)

# Important

This is the development version of WebGoat 8, if you are looking for a released stable version please go to: https://github.com/WebGoat/WebGoat/wiki/Running-WebGoat


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
docker run -p 8080:8080 webgoat/webgoat-8.0
```

Wait for the Docker container to start and go to step 3.
Please note: this version may not be completely in sync with the develop branch.

## 2. Run from the sources

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
git checkout develop
mvn clean package
```

Now we are ready to run the project. WebGoat 8.x is using Spring-Boot.

```Shell
mvn -pl webgoat-container spring-boot:run
```
... you should be running webgoat on localhost:8080/WebGoat momentarily


# Vagrant

We supply a complete development environment using Vagrant, to run WebGoat with Vagrant you must first have Vagrant and Virtualbox installed.

```shell
   $ cd WebGoat/webgoat-images/vagrant-users
   $ vagrant up
```

Once the provisioning is complete login to the Virtualbox with username vagrant and password vagrant.
The source code will be available in the home directory.


# Building a new Docker image

WebGoat now has Docker support for x86 and arm (raspberry pi).
### Docker on x86
On x86 you can build a container with the following commands:

```Shell
cd WebGoat/
mvn package
cd webgoat-container
mvn package
mvn docker:build
docker tag webgoat/webgoat-8.0 webgoat/webgoat-8.0:8.0
docker login
docker push webgoat/webgoat-container
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
mvn package
cd webgoat-container
mvn package
mvn docker:build -Drpi=true
docker tag webgoat/webgoat-8.0 webgoat/webgoat-8.0:8.0
docker login
docker push webgoat/webgoat-container
```

With the following command you are able to run the Docker container on your local machine:

```Shell
docker run -p 8080:8080 -t webgoat/webgoat-8.0
docker ps
```

With the last command you are able to determine ip address to connect to.
