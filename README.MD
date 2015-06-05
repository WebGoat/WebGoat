# Important Information

Thank you for downloading WebGoat!  This is the WebGoat Legacy version which is essentially the WebGoat 5 with a new UI. 

This program is a demonstration of common server-side application flaws. The
exercises are intended to be used by people to learn about application
penetration testing techniques.

* [Home Page](http://webgoat.github.io)
* [OWASP Project Home Page](http://www.owasp.org/index.php/Category:OWASP_WebGoat_Project)
* [Source Code](https://github.com/WebGoat/WebGoat-Legacy)
* [Easy-Run Download](https://webgoat.atlassian.net/builds/browse/WEB-WGM/latestSuccessful/artifact/JOB1/WebGoat-Embedded-Tomcat/WebGoat-6.0-SNAPSHOT-war-exec.jar  )      
* [Wiki](https://github.com/WebGoat/WebGoat-Legacy/wiki)
* [FAQ (old info):](http://code.google.com/p/webgoat/wiki/FAQ)
* [Contact Info - Direct to Bruce Mayhew](webgoat@owasp.org)
* [Mailing List - WebGoat Community - For most questions](owasp-webgoat@lists.owasp.org) 

**WARNING 1:** *While running this program your machine will be extremely
vulnerable to attack. You should to disconnect from the Internet while using
this program.*

**WARNING 2:** *This program is for educational purposes only. If you attempt
these techniques without authorization, you are very likely to get caught. If
you are caught engaging in unauthorized hacking, most companies will fire you.
Claiming that you were doing security research will not work as that is the
first thing that all hackers claim.*

You can find more information about WebGoat at:
(https://github.com/WebGoat/)


# Easy Run Instructions ( For non-developers )

Follow these instructions if you simply wish to run WebGoat

**Prerequisites:** 

Java VM >= 1.6 installed ( JDK 1.7 recommended)

1. Download the executable jar file to any location of your choice:

    (https://webgoat.atlassian.net/builds/browse/WEB-WGM/latestSuccessful/artifact/shared/WebGoat-Embedded-Tomcat/WebGoat-6.0.1-war-exec.jar)

2. Run it using java:

    > java -jar WebGoat-6.0-exec-war.jar

3. Then navigate in your browser to: (http://localhost:8080/WebGoat)

4. If you would like to change the port or other options, use:

    > java -jar WebGoat-6.0-exec-war.jar --help


# Standard Run Instructions (For Developers)

Follow These instructions if you wish to run Webgoat and modify the source code
as well.

**Prerequisites:**

* Java >= 1.6 ( JDK 1.7 recommended )
* Maven > 2.0.9
* Your favorite IDE, with Maven awareness: Netbeans/IntelliJ/Eclipse with m2e
  installed. If you are setting up an IDE, Netbeans 8.0 contains the Maven and
  Git support you need: (https://netbeans.org/downloads/)
* Git, or Git support in your IDE
        
**Note:** WebGoat source code can be downloaded at: (https://github.com/WebGoat/WebGoat-Legacy). 
	

1. Building the project (Developers) using a command shell/window:

    > cd webgoat
    > mvn clean package

2. After opening the project in Netbeans or Eclipse, you can easily run the
project using maven:

    > mvn tomcat:run-war

3. Maven will run the project in an embedded tomcat. The package phase also builds an executable jar file. You can run it using:

    > cd target
    > java -jar WebGoat-6.0-exec-war.jar http://localhost:8080/WebGoat
