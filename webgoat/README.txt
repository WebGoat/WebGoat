**********
**********          WebGoat 5.4
**********          April/27/2012
**********
**
**  Home Page:    http://code.google.com/p/webgoat
**  Home Page:    http://www.owasp.org/index.php/Category:OWASP_WebGoat_Project
**  Source Code:  http://code.google.com/p/webgoat/source/checkout
**  Download:     http://code.google.com/p/webgoat/downloads/list   
**  Download:     http://sourceforge.net/project/showfiles.php?group_id=64424&package_id=61824 (older stuff)
**  User Guide:   http://www.owasp.org/index.php/WebGoat_User_and_Install_Guide_Table_of_Contents
**  Wiki:         http://code.google.com/p/webgoat/w/list
**  FAQ:          http://code.google.com/p/webgoat/wiki/FAQ
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


----------------------------------------------------------------------------------------
Prerequisites for Developers (Skip to Option 3 for unzip and click to run configruation) 
----------------------------------------------------------------------------------------

These tools must be installed independent of the webgoat download.
- Java 1.6
    Java can ne downloaded at http://java.sun.com/javase/downloads/index.jsp
	You only need to download and install the "Java SE Development Kit (JDK)"
- Maven > 2.0.9
	Maven can be downloaded at: http://maven.apache.org/
	In Ubuntu it can be installed with:
	> apt-get install maven2
- WebGoat source code
    WebGoat source code can be downloaded at: 
          http://code.google.com/p/webgoat/source/checkout
    Use an svn client (ex: Tortoise svn) to checkout the code in the trunk.
    

	
---------------------------------
Building the project (Developers)
---------------------------------

Using a command shell/window:

> cd webgoat
> mvn compile

copy it to the local repository
> mvn install

delete artifacts from previous build:
> mvn clean


-----------------------------------------------
Building the Eclipse project files (Developers)
-----------------------------------------------

> mvn eclipse:clean
> mvn eclipse:eclipse

Afterward the project can be imported within Eclipse:
File -> Import -> General -> Existing Projects into Workspace
and select the webgoat directory as the "root directory." A webgoat should appear in the Projects section of your dialogue window.

Don't forget to declare a classpath variable named M2_REPO, pointing to ~/.m2/repository, otherwise many links to existing jars will be broken.
This folder is located in your username root folder, the same folder where "my documents" and "my pictures" are located.
You can declare new variables in Eclipse in Windows -> Preferences... and selecting Java -> Build Path -> Classpath Variables


-------------------------------------------------------------------
Option 1: (Developers) Run the project on Tomcat within Eclipse
-------------------------------------------------------------------

Install a local Tomcat server (We use Tomcat 7)
1. Download and unzip Apache Tomcat from http://tomcat.apache.org. 
2. Adapt the conf/tomcat-users.xml file of your Tomcat server:
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
3. Open Eclipse (WTP version) -> File -> New -> Other -> Server -> Apache
4. Choose your Tomcat version
-> Click next "browse" to your tomcat install.
-> Make sure the "JRE" dropdown is pointing to your jdk. If it isn't listed, press
"Installed JREs" and add it.
-> Click next and add "webgoat" to the list of configured applications
-> Finish


3. Right Click on the webgoat project within eclipse -> Run As -> Run on server 

Point your browser to http://localhost:8080/webgoat/attack
** Note - When running in eclipse, the default url will be lowercase "webgoat"


-----------------------------------------------------------
Option 2: (Developers) Run the project on Tomcat with Maven
-----------------------------------------------------------

1. mvn tomcat:run-war
2. http://localhost:8080/WebGoat/attack


------------------------------------------------------------------
Option 3: Run from the WebGoat 5.X Standard distribution (Windows)
------------------------------------------------------------------

1. Download the WebGoat-5.X-OWASP_Standard_Win32.zip file from:
        - http://code.google.com/p/webgoat/downloads/list
2. Unzip the file
3. Double click webgoat.bat
4. Browse to http://localhost/WebGoat/attack

** Note: if you receive a bind address error use:

3. Double click webgoat8080.bat
4. Browse to http://localhost:8080/WebGoat/attack


------------------------------------------------------------------
Option 4: Run from the WebGoat 5.X Standard distribution (Ubuntu)
------------------------------------------------------------------

1. Download the WebGoat-5.X-OWASP_Standard_Ubuntu32.zip file from:
        - http://code.google.com/p/webgoat/downloads/list
2. Unzip the file
3. run sudu ./webgoat.sh start80
4. Browse to http://localhost/WebGoat/attack

** Note: if you receive a bind address or privilege error:

3. run ./webgoat.sh start8080
4. Browse to http://localhost:8080/WebGoat/attack

shutdown the server with:
./webgoat.sh stop

------------------------------------------------------------------
Option 5: Using the  WebgGoat-5.X.war
------------------------------------------------------------------

Windows:

1. Download and install Java 1.6 and Tomcat 7 if needed
2. Download the WebgGoat-5.X.war and README-5.X file from:
    - http://code.google.com/p/webgoat/downloads/list
3. Rename WebgGoat-5.X.war to WebgGoat.war
4. Copy WebGoat.war to <tomcat>/webapps/WebGoat.war
5. Modify the <tomcat>/conf/tomcat-users.xml to add in WebGoat users and roles
    - see the FAQ for directions 
6. Start the tomcat server (default is usually port 8080)
7. Browse to http://localhost:8080/WebGoat/attack

Ubuntu:

1. Install Java 1.6 and Tomcat 7 if needed
    - Install java using: sudo apt-get install openjdk-7-jre
    - Download Tomcat 7 from http://tomcat.apache.org/download-70.cgi (core tar.gz)
2. Download the WebgGoat-5.X.war and README-5.X file from:
    - http://code.google.com/p/webgoat/downloads/list
3. Rename WebgGoat-5.X.war to WebgGoat.war
4. Copy WebGoat.war to <tomcat>/webapps/WebGoat.war
5. Modify the <tomcat>/conf/tomcat-users.xml to add in WebGoat users and roles
    - see the FAQ for directions 
6. Start the tomcat server (default is usually port 8080)
7. Browse to http://localhost:8080/WebGoat/attack