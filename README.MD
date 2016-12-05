# WebGoat: A deliberately insecure Web Application

[![Build Status](https://travis-ci.org/WebGoat/WebGoat.svg?branch=develop)](https://travis-ci.org/WebGoat/WebGoat)
[![Coverage Status](https://coveralls.io/repos/WebGoat/WebGoat/badge.svg?branch=master&service=github)](https://coveralls.io/github/WebGoat/WebGoat?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/b69ee3a86e3b4afcaf993f210fccfb1d)](https://www.codacy.com/app/dm/WebGoat)
[![Dependency Status](https://www.versioneye.com/user/projects/562da95ae346d7000e0369aa/badge.svg?style=flat)](https://www.versioneye.com/user/projects/562da95ae346d7000e0369aa)
[![OWASP Labs](https://img.shields.io/badge/owasp-labs-orange.svg)](https://www.owasp.org/index.php/OWASP_Project_Inventory#tab=Labs_Projects)

# Important Information

### The WebGoat Lesson Server, is currently **UNDER MAJOR DEVELOMENT**.
As of November 2016, 7.1 is the current stable release and 8.x is under development ('develop') branch here.

#### Older/Legacy version of WebGoat an be found at: [WebGoat-Legacy](https://github.com/WebGoat/WebGoat-Legacy)

WebGoat is a deliberately insecure web application maintained by [OWASP](http://www.owasp.org/) designed to teach web
application security lessons.

This program is a demonstration of common server-side application flaws. The
exercises are intended to be used by people to learn about application security and
penetration testing techniques.

* [Home Page](http://webgoat.github.io)
* [OWASP Project Home Page](http://www.owasp.org/index.php/Category:OWASP_WebGoat_Project)
* [Source Code](https://github.com/WebGoat/WebGoat)
* [Easy-Run Download](https://s3.amazonaws.com/webgoat-war/webgoat-container-7.0.1-war-exec.jar)
* [Wiki](https://github.com/WebGoat/WebGoat/wiki)
* [FAQ (old info):](http://code.google.com/p/webgoat/wiki/FAQ)
* [Project Leader - Direct to Bruce Mayhew](mailto:webgoat@owasp.org)
* [Mailing List - WebGoat Community - For most questions](mailto:owasp-webgoat@lists.owasp.org)
* [Artifacts generated from Continuous Integration](http://webgoat-war.s3-website-us-east-1.amazonaws.com/)
* [Output from our Travis.CI Build server](https://travis-ci.org/WebGoat/WebGoat)

**WARNING 1:** *While running this program your machine will be extremely
vulnerable to attack. You should to disconnect from the Internet while using
this program.*  WebGoat's default configuration binds to localhost to minimize 
the exposure.

**WARNING 2:** *This program is for educational purposes only. If you attempt
these techniques without authorization, you are very likely to get caught. If
you are caught engaging in unauthorized hacking, most companies will fire you.
Claiming that you were doing security research will not work as that is the
first thing that all hackers claim.*

# Easy Run ( For non-developers )

Every successful build of the WebGoat Lessons Container and the WebGoat Lessons in our Continuous Integration Server
creates an "Easy Run" Executable WAR file, which contains the WebGoat Lessons Server, the lessons and a embedded Tomcat server.

You can check for the "Last Modified" date of our "Easy Run" war file [HERE](http://webgoat-war.s3-website-us-east-1.amazonaws.com/)

The "Easy Run" JAR file offers a no hassle approach to testing and running WebGoat. Follow these instructions if you
wish to simply try/test/run the current development version of WebGoat

### Prerequisites:
* Java VM 1.8 or Docker installed

## Easy Run Instructions:

#### 1. Docker image

The latest version of WebGoat is available at DockerHub, see [https://hub.docker.com/r/webgoat/webgoat-container/](https://hub.docker.com/r/webgoat/webgoat-container/).
First install Docker, then open a command shell/window and type:

```Shell
docker pull webgoat/webgoat-container
docker run -p 8080:8080 webgoat/webgoat-container
```

Wait for the Docker container to start and go to step 3.

#### 2. Download the easy run executable jar file which contains all the lessons and a embedded Tomcat server:

https://s3.amazonaws.com/webgoat-war/webgoat-standalone-7.1-SNAPSHOT-exec.jar

#### 3. Run it using java:

Open a command shell/window, browse to where you downloaded the easy run jar and type:

```Shell
java -jar webgoat-standalone-7.0.1-exec.jar [-p | --p <port>] [-a | --address <address>]
```

Using the `--help` option will show the allowed command line arguments.

#### 3. Browse to the url shown in the console and happy hacking !

## Vagrant

To run WebGoat with Vagrant you must first have Vagrant and Virtualbox installed.

```shell
   $ cd WebGoat/webgoat-images/vagrant-users
   $ vagrant up
```

Once you see the message 'Browse to http://localhost:9999/WebGoat and happy hacking! you can open a
browser.


# For Developers

## Vagrant

For an easy development experience you can use Vagrant. Note you should have Vagrant and Virtualbox installed on your system.

```shell
   $ cd WebGoat/webgoat-images/vagrant-developers
   $ vagrant up
```

Once the provisioning is complete login to the Virtualbox with username vagrant and password vagrant.
The source code will be available in the home directory.

## Set up manual

Follow these instructions if you wish to run Webgoat and modify the source code as well.

### Prerequisites:

* Java 8
* Maven > 3.2.1
* Your favorite IDE, with Maven awareness: Netbeans/IntelliJ/Eclipse with m2e installed.
* Git, or Git support in your IDE

## The Easy Way: Developer Edition run using Linux or Mac
The __webgoat_developer_bootstrap.sh__ script will clone the necessary repositories, call the maven goals in order
launch Tomcat listening on localhost:8080

```Shell
mkdir WebGoat-Workspace
cd WebGoat-Workspace
curl -o webgoat_developer_bootstrap.sh https://raw.githubusercontent.com/WebGoat/WebGoat/master/webgoat_developer_bootstrap.sh
./webgoat_developer_bootstrap.sh
```

## The Manual Way: Developer Edition!

#### Cloning the Lesson Server and the Lessons project:

Open a command shell/window, navigate to where you wish to download the source and type:
mvn clean package
mvn –pl webgoat-container spring-boot:run

```Shell
git clone git@github.com:WebGoat/WebGoat.git
```

#### Now let's start by compiling the project.

*NOTE*: the -DskipTests below is temporary, until we root out an issue (but we didn't want to delay updating these instructions anymore)
```Shell
cd WebGoat
git checkout develop
mvn clean package -DskipTests=true
```

#### Now we are ready to run the project. WebGoat 8.x is using Spring-Boot.
```Shell
mvn -pl webgoat-container spring-boot:run
```
... you should be running webgoat on localhost:8080/WebGoat momentarily

## Building a new Docker image

WebGoat now has Docker support you can build a container with the following commands:

```Shell
cd WebGoat/
mvn package
cd webgoat-container
mvn docker:build
docker login
docker push webgoat/webgoat-container
```

With the following command you are able to run the Docker container on your local machine:

```Shell
docker run -p 8080:8080 -t webgoat/webgoat-container
docker ps
```

With the last command you are able to determine ip address to connect to.
