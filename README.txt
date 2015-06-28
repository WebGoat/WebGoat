**********
* June 9 2015
* #####  WebGoat Container Migration from AngularJS to Backbone #####
* Why: I believe AngularJS is a little heavy (and acutally a little too secure, imagine that) for the purposes of WebGoat
* When: Now
* What: Porting current functionality in the 6.0.1 release into the webgoat-container build
* How: If you'd like to help.  Fork this repository and contact me (jason.white@owasp.org) as to the current priorities/needs. Once you fork it,
* After forking and cloning this repo, you should also fork/clone the lessons repository (https://github.com/WebGoat/WebGoat-Lessons). More on that in a second
* in the core/container WebGoat (this) project
* $ mvn clean install
* Then either package and put the package in a local tomcat OR ...
* $ mvn tomcat:run
* Then you'll need to switch to the lessons directory and
* $ mvn clean packa
* Then copy some of the lesson jars into your $TOMCAT_HOME/webapps/{webgoat-container}/plugin_lessons
* Now you should have something to work against for the UI work
**********

##### Original WebGoat ReadMe follows #####

**********
**********          WebGoat 6.0
**********          August 23, 2014
**********
**
**  Home Page:              http://webgoat.github.io
**  Home Page:              http://www.owasp.org/index.php/Category:OWASP_WebGoat_Project
**  Source Code:            https://github.com/WebGoat/WebGoat
**  Easy-Run Download:      https://webgoat.atlassian.net/builds/browse/WEB-DAIL/latestSuccessful/artifact/JOB1/WebGoat-Embedded-Tomcat/WebGoat-6.0-SNAPSHOT-war-exec.jar  
**  User Guide:             http://www.owasp.org/index.php/WebGoat_User_and_Install_Guide_Table_of_Contents
**  Wiki:                   http://code.google.com/p/webgoat/w/list
**  FAQ:                    http://code.google.com/p/webgoat/wiki/FAQ
**  Contact Info: webgoat@owasp.org (Direct to Bruce Mayhew)
**  Mailing List: owasp-webgoat@lists.owasp.org (WebGoat Community - For most questions)
**
**********

Thank you for downloading WebGoat!

This program is a demonstration of common server-side
application flaws.  The exercises are intended to
be used by people to learn about application penetration
testing techniques.


WARNING 1: While running this program your machine will be 
extremely vulnerable to attack. You should to disconnect
from the Internet while using this program.

WARNING 2: This program is for educational purposes only. If you
attempt these techniques without authorization, you are very
likely to get caught.  If you are caught engaging in unauthorized
hacking, most companies will fire you. Claiming that you were
doing security research will not work as that is the first thing
that all hackers claim.

You can find more information about WebGoat at:
https://github.com/WebGoat/

----------------------------------------------------------------------------------------
Easy Run Instructions ( For non-developers )
----------------------------------------------------------------------------------------
Follow these instructions if you simply wish to run WebGoat

    Prerequisites:  Java VM >= 1.6 installed ( JDK 1.7 recommended)
    Download the executable jar file to any location of your choice from:
https://webgoat.atlassian.net/builds/browse/WEB-WGM/latestSuccessful/artifact/shared/WebGoat-Embedded-Tomcat/WebGoat-6.0.1-war-exec.jar
    Run it using java:
        java -jar WebGoat-6.0-exec-war.jar

    Then navigate in your browser to:
    http://localhost:8080/WebGoat

    If you would like to change the port or other options, use:
    java -jar WebGoat-6.0-exec-war.jar --help

----------------------------------------------------------------------------------------
For Developers 
----------------------------------------------------------------------------------------
Follow These instructions if you wish to run Webgoat and modify the source code as well.

    Prerequisites:
        * Java >= 1.6 ( JDK 1.7 recommended )
        * Maven > 2.0.9
        *Your favorite IDE, with Maven awareness: Netbeans/IntelliJ/Eclipse with m2e installed
        * Git, or Git support in your IDE
        
        WebGoat source code
            WebGoat source code can be downloaded at: 
                  https://github.com/WebGoat/WebGoat

        If you are setting up an IDE, Netbeans 8.0 contains the Maven and Git support you need:
            https://netbeans.org/downloads/
	
---------------------------------
Building the project (Developers)
---------------------------------

Using a command shell/window:

> cd webgoat-classloader
> mvn clean install
> cd ..
> mvn clean package

Building the webgoat-classloader is only necessary once, the classloader needs to be present in your local repository.
After opening the project in Netbeans or Eclipse, you can easily run the project using: 

1. Maven-Tomcat Plugin
Using a command shell/window:

> mvn tomcat:run-war

Maven will run the project in an embedded tomcat.


2. Java JAR
the package phase also builds an executable jar file. You can run it using:
cd target
java -jar WebGoat-6.0-exec-war.jar
http://localhost:8080/WebGoat


3. Tomcat
the package phase also builds a war file. You can deploy it using:
cp target/WebGoat-6.0-exec-war.war <tomcat>/webapps/
Then also clone https://github.com/WebGoat/WebGoat-Lessons run:
cd WebGoat-Lessons
mvn package
cp plugins/* <tomcat>/webapps/WebGoat-6.0-exec-war/plugin_lessons/
