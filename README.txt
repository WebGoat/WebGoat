**********          WebGoat 5.3
**********          November/10/2000
**********
**
**  Source Code:  http://code.google.com/p/webgoat
**  Download:     http://sourceforge.net/project/showfiles.php?group_id=64424&package_id=61824
**  Download:     http://code.google.com/p/webgoat/downloads/list   (Does not have Developer release)
**  User Guide:   http://www.owasp.org/index.php/WebGoat_User_and_Install_Guide_Table_of_Contents
**  Home Page:    http://www.owasp.org/index.php/Category:OWASP_WebGoat_Project
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
http://code.google.com/p/webgoat


--------------
Prerequisites (Skip to Option 3 for unzip and click to run configruation): 
--------------

These tools must be installed independent of the webgoat download.
- Java 1.6
    Java can ne downloaded at http://java.sun.com/javase/downloads/index.jsp
	You only need to download and install the "Java SE Development Kit (JDK)"
- Maven > 2.0.9
	Maven can be downloaded at: http://maven.apache.org/
	At Ubuntu it can be installed with:
	> apt-get install maven2
- WebGoat source code
    WebGoat source code can be downloaded at: http://webgoat.googlecode.com/files/WebGoat-OWASP_Standard-5.3.zip
	This is the directory structure for the commands below.  You can also work with the latest 
	WebGoat by checking out the WebGoat source at: http://code.google.com/p/webgoat/source/checkout

	
--------------------
Building the project
--------------------

> cd webgoat
> mvn compile

copy it to the local repository
> mvn install

delete artifacts from previous build:
> mvn clean


----------------------------------
Building the Eclipse project files
----------------------------------

> mvn eclipse:eclipse

Afterward the project can be imported within Eclipse:
File -> Import -> General -> Existing Projects into Workspace

Don't forget to declare a classpath variable named M2_REPO, pointing to ~/.m2/repository, otherwise many links to existing jars will be broken.
You can declare new variables in Eclipse in Windows -> Preferences... and selecting Java -> Build Path -> Classpath Variables


---------------------------------------------------
Option 1: Run the project on Tomcat within Eclipse
---------------------------------------------------

1. Install a local Tomcat server
2. Open Eclipse -> File -> New -> Other -> Server -> Apache -> Tomcat -> Next 
-> Insert your Tomcat Installation directory
-> Click next and add "webgoat" to the list of configured applications
-> Finish
3. Adapt the conf/tomcat-users.xml file of your Tomcat server:
    <?xml version="1.0" encoding="UTF-8"?>
    <tomcat-users>
      <role rolename="webgoat_basic"/>
      <role rolename="webgoat_admin"/>
      <role rolename="webgoat_user"/>
      <role rolename="tomcat"/>
      <user password="webgoat" roles="webgoat_admin" username="webgoat"/>
      <user password="basic" roles="webgoat_user,webgoat_basic" username="basic"/>
      <user password="tomcat" roles="tomcat" username="tomcat"/>
      <user password="guest" roles="webgoat_user" username="guest"/>
    </tomcat-users>

4. Right Click on the webgoat project within eclipse -> Run As -> Run on server 
5. http://localhost:8080/webgoat/attack


----------------------------------------------
Option 2: Run the project on Tomcat with Maven
----------------------------------------------

1. mvn tomcat:run-war
2. http://localhost:8080/webgoat/attack


--------------------------------------------------------
Option 3: Run from the WebGoat 5.3 Standard distribution
--------------------------------------------------------
1. Download the WebGoat-OWASP_Standard-X.X.zip file from http://code.google.com/p/webgoat/downloads/list
2. Unzip the file
3. Double click webgoat.bat
4. Browse to http://localhost/webgoat/attack

