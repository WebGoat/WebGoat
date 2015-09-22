# WebGoat: A deliberately insecure Web Application

[![Build Status](https://travis-ci.org/WebGoat/WebGoat.svg)](https://travis-ci.org/WebGoat/WebGoat)

# Important Information

### This is a work in progress of the WebGoat Lesson Server, which is currently **UNDER MAJOR DEVELOMENT**

#### Current stable version and instructions can be found at:  [WebGoat-Legacy](https://github.com/WebGoat/WebGoat-Legacy)

WebGoat is a deliberately insecure web application maintained by [OWASP](http://www.owasp.org/) designed to teach web application security lessons.

This program is a demonstration of common server-side application flaws. The
exercises are intended to be used by people to learn about application security and
penetration testing techniques.

* [Home Page](http://webgoat.github.io)
* [OWASP Project Home Page](http://www.owasp.org/index.php/Category:OWASP_WebGoat_Project)
* [Source Code](https://github.com/WebGoat/WebGoat)
* [Easy-Run Download **TBD**](https://github.com/WebGoat/WebGoat/releases/**TBD**)
* [Wiki](https://github.com/WebGoat/WebGoat/wiki)
* [FAQ (old info):](http://code.google.com/p/webgoat/wiki/FAQ)
* [Project Leader - Direct to Bruce Mayhew](mailto:webgoat@owasp.org)
* [Mailing List - WebGoat Community - For most questions](mailto:owasp-webgoat@lists.owasp.org)

**WARNING 1:** *While running this program your machine will be extremely
vulnerable to attack. You should to disconnect from the Internet while using
this program.*  WebGoat's default configuration binds to localhost to minimize 
the exposure.

**WARNING 2:** *This program is for educational purposes only. If you attempt
these techniques without authorization, you are very likely to get caught. If
you are caught engaging in unauthorized hacking, most companies will fire you.
Claiming that you were doing security research will not work as that is the
first thing that all hackers claim.*

# Easy Run Instructions ( For non-developers )

**Note - Use [WebGoat-Legacy](https://github.com/WebGoat/WebGoat-Legacy) for a stable build**

Follow these instructions if you simply wish to run WebGoat

### Prerequisites:

* Java VM >= 1.6 installed ( JDK 1.7 recommended)

1. Download the executable jar file which contains all the lessons:

  https://s3.amazonaws.com/webgoat-war/webgoat-container-7.0-SNAPSHOT-war-exec.jar

2. Run it using java:

```Shell
java -jar webgoat-container-7.0-SNAPSHOT-war-exec.jar
```

3. Then navigate in your browser to: (http://localhost:8080/WebGoat)

4.(Optional) If you would like to change the port or other options, use:

```Shell
java -jar webgoat-container-7.0-SNAPSHOT-war-exec.jar --help
```

# For Developers

Follow these instructions if you wish to run Webgoat and modify the source code as well.

### Prerequisites:

* Java >= 1.6 ( JDK 1.7 recommended )
* Maven > 2.0.9
* Your favorite IDE, with Maven awareness: Netbeans/IntelliJ/Eclipse with m2e installed.
* Git, or Git support in your IDE

## The Easy Way: Developer Edition run using Linux or Mac
The __webgoat_developer_bootstrap.sh__ script will clone the necessary repositories, call the maven goals in order
launch Tomcat listening on localhost:8080

```Shell
mkdir WebGoat-Workspace
cd WebGoat-Workspace
curl -o webgoat_developer_bootstrap.sh https://raw.githubusercontent.com/WebGoat/WebGoat/master/webgoat_developer_bootstrap.sh
sh webgoat_developer_bootstrap.sh
```

## The Manual Way: Developer Edition!

#### Cloning the Lesson Server and the Lessons project:

Open a command shell/window, navigate to where you wish to download the source and type:

```Shell
git clone https://github.com/WebGoat/WebGoat.git
git clone https://github.com/WebGoat/WebGoat-Lessons.git
```

#### Now let's start by compiling the WebGoat Lessons server.

```Shell
cd WebGoat
mvn clean compile install
cd ..
```

#### Before you can run the project, we need to compile the lessons and copy them over:
** If you don't run this step, you will not have any Lessons to work with!**

```Shell
cd WebGoat-Lessons
mvn package
cp target/plugins/*.jar ../WebGoat/webgoat-container/src/main/webapp/plugin_lessons/
cd ..
```

#### Now we are ready to run the project. There are 3 options you can choose from to run the project:

Then you can run the project with one of the steps below (From the WebGoat folder not WebGoat-Lessons):

#### Option #1: Using the Maven-Tomcat Plugin
The __maven tomcat7:run-war__ goal runs the project in an embedded tomcat:

```Shell
cd WebGoat
mvn -pl webgoat-container tomcat7:run-war
```

Browse to [http://localhost:8080/WebGoat](http://localhost:8080/WebGoat) and happy hacking !

#### Option #2: Java executable JAR
The __maven package__ goal generates an executable .jar file:

```Shell
cd WebGoat
mvn package
cd webgoat-container/target
java -jar webgoat-container-7.0-SNAPSHOT-war-exec.jar http://localhost:8080/WebGoat
```

Browse to [http://localhost:8080/WebGoat](http://localhost:8080/WebGoat) and happy hacking !

#### Option #3: Deploy the WebGoat WAR file in yout local Tomcat or other Application Serve:
The __maven package__ goal generates a .war file that can deployed into an Application Server, such as Tomcat

```Shell
cd WebGoat
mvn package 
cp webgoat-container/target/webgoat-container-7.0-SNAPSHOT-war-exec.jar <your_tomcat_directory>/webapps/
```

Browse to [http://localhost:8080/WebGoat](http://localhost:8080/WebGoat) and happy hacking !


## Reloading plugins

If you want to reload all the plugin visit the following url: `http://localhost:8080/WebGoat/service/reloadplugins.mvc`
After you receive a 200/OK the page can be reloaded.