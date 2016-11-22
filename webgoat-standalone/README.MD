# WebGoat standalone runner

## Introduction
This project is aimed to be the replacement for the exec-war, it contains
a simple Main.class which will start an embedded Tomcat server.
This makes it easier to change the server address and the portnumber for example.
It kind of works in the same way Spring Boot starts an embedded Tomcat server.

## Usage

For the first time make sure you run a complete build:

```Shell
mvn clean install
```

Open up your favourite IDE and run the Main.class which will start the
embedded Tomcat server.

Or in a shell:

```Shell
java -jar webgoat-standalone-<<version>>-exec.jar
```

The following command line options are available:

```
-a, --address Specify the server address, like 192.168.0.1 (default localhost)
-p, --port    Specify on which port the server should run (default 6047)
```