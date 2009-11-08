Prerequisites: 

- Java 1.6
- Maven > 2.0.9
	Maven can be downloaded at: http://maven.apache.org/
	At Ubuntu it can be installed with:
	> apt-get install maven2

Building the project
------------------
> cd webgoat
> mvn compile

copy it to the local repository
> mvn install

delete artifacts from previous build:
> mvn clean


Building the Eclipse project files
-------------------------------
> mvn eclipse:eclipse

Afterward the project can be imported within Eclipse:
File -> Import -> General -> Existing Projects into Workspace

Don't forget to declare a classpath variable named M2_REPO, pointing to ~/.m2/repository, otherwise many links to existing jars will be broken.
You can declare new variables in Eclipse in Windows -> Preferences... and selecting Java -> Build Path -> Classpath Variables


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


Option 2: Run the project on Tomcat with Maven
---------------------------------------------------
1. mvn tomcat:run-war
2. http://localhost:8080/webgoat/attack


