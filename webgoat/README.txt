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

