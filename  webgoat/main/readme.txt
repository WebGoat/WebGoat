**********          WebGoat 5.0
**********          01.31.2007
**********
**
**  Source Code:  http://code.google.com/p/webgoat
**  Download:     http://sourceforge.net/project/showfiles.php?group_id=64424&package_id=61824
**  User Guide:   http://www.owasp.org/index.php/WebGoat_User_and_Install_Guide_Table_of_Contents
**  Home Page:    http://www.owasp.org/index.php/Category:OWASP_WebGoat_Project
**  Contact Info: webgoat@g2-inc.com
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

CREDITS (Latest release)

	Bruce Mayhew (http://www.g2-inc.com)
	Sherif Koussa (http://www.macadamian.com)
	Rogan Dawes (http://dawes.za.net/rogan)
	Eric Sheridan (http://www.aspectsecurity.com) 
	Carlo Pelliccioni
	The many people who have sent comments and suggestions...
        
WHAT'S NEW

	* WebGoat is now current at Google code. (http://code.google.com/p/webgoat)
	* HTTP Splitting 
	* Cross-Site Request Forgery 
	* XPATH Injection 
	* AJAX Security 
	* Log Spoofing 
	* Cache Poisoning 
	* Back Doors via SQL Injection 
	* Many upgrades and minor fixes

INSTALLATION

Windows - (Download, Extract, Double Click Release)

1. unzip the Windows_WebGoat-x.x_Release.zip to your working environment 
2. To start Tomcat, browse to the WebGoat directory unzipped above and 
     double click "webgoat.bat"
3. start your browser and browse to... (Notice the capital 'W' and 'G')
	 http://localhost/WebGoat/attack
4. login in as: user = guest, password = guest
5. To stop WebGoat, simply close the window you launched it from.

Note: When intercepting request with IE7.  You must add a '.' to the
      end of localhost.  i.e. 
          http://localhost./WebGoat/attack        or
          http://localhost.8080/WebGoat/attack    if using a non standard port


Linux

1. Download and install Java JDK 1.5 from Sun (http://java.sun.com)
2. Unzip the Unix_WebGoat-x.x_Release.zip to your working directory
3. Set JAVA_HOME to point to your JDK1.5 installation
4. chmod +x webgoat.sh 
5. Since the latest version runs on a privileged port, you will need to start/stop WebGoat as root.
	sudo sh webgoat.sh start
	sudo sh webgoat.sh stop
6. start your browser and browse to... (Notice the capital 'W' and 'G')
	http://localhost/WebGoat/attack
7. login in as: user = guest, password = guest


OS X (Tiger 10.4+)

1. Unzip the Unix_WebGoat-x.x_Release.zip to your working directory
2. chmod +x webgoat.sh
3. Since the latest version runs on a privileged port, you will need to start/stop WebGoat as root.
	sudo sh webgoat.sh start
	sudo sh webgoat.sh stop
4. start your browser and browse to... (Notice the capital 'W' and 'G')
	http://localhost/WebGoat/attack
5. login in as: user = guest, password = guest


DEVELOPER INSTALLATION

1. Download WebGoat-x.x_developer.zip source distribution
2. Unzip the WebGoat-x.x_developer.zip to your working directory
3. Follow the directions in HOW TO create the WebGoat workspace.txt


HOW WEBGOAT WORKS

TROUBLESHOOTING/FAQs:
Q. I put the OWASP downloaded war file in my tomcat/webapps directory and the 
   http://localhost/WebGoat/attack url doesn't work.
A. Rename the downloaded war file to WebGoat.war.  Delete the existing tomcat/webapps/*WebGoat* directories.

Q. I dropped the WebGoat war file into my non-Tomcat application server and WebGoat doesn't seem to work.
A. WebGoat uses some of the internal Tomcat classes for user management.  Unfortunately, this makes 
   WebGoat dependent on Tomcat.  Hopefully, this will be addressed in a future release.

Q. Having problems with the ant file working properly. How do I configure my ant environment 
   so that I don't receive errors such as:
	- "Specified VM install not found: type Standard VM, name j2sdk1.4.2.06"
A. This usually indicates an Eclipse environment setting misconfiguration. Here are some possible solutions:
	i. Ant Runtime Configuration
		- Window > Preferences
		- Ant > Runtime
		- Under Classpath Tab check the "Global Entries"
		- Remove any jre "tools.jar" references
		- Add the "\tomcat\servers\lib\catalina-ant.jar" file.
		- Click Apply, Click OK.
		- Return to the Ant View and refresh.

Q. When I start up WebGoat it dies very quickly.
A. WebGoat is a Java application that runs on Tomcat using port 80.  If you have another 
   application listening on port 80 (like IIS), you will need to change WebGoat's port 
   (to 8080 or something) in the tomcat_root/conf/server.xml file.

Q. When I deploy the war file to the Tomcat wepapps directory, I can't login to WebGoat
A. You need to add the webgoat users and roles to tomcat/conf/tomcat-users.xml

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


Please send questions, comments, suggestions, bugs, etc to webgoat@g2-inc.com