# WebWolf

## Introduction

During workshops one of the feedback items was that in some lesson it was not clear what you controlled 
as an attacker and what was part of the lesson. To make this separation more distinct we created 
WebWolf which is completely controlled by you as the attacker and runs as a separate application. 

Instead of using your own machine which would involve WebGoat being connected to your local network
or internet (remember WebGoat is a vulnerable webapplication) we created WebWolf which is the the 
environment for you as an attacker.

At the moment WebWolf offers support for:

- Receiving e-mails
- Serving files
- Logging of incoming requests (cookies etc)

# Run instructions

## 1. Run using Docker

If you use the Docker image of WebGoat this application will automatically be available. Use the following 
URL: http://localhost:9090/WebWolf

## 2. Standalone

```Shell
cd WebGoat
git checkout develop
mvn clean install
```

Now we are ready to run the project. WebGoat 8.x is using Spring-Boot.

```Shell
mvn -pl webwolf spring-boot:run
```
... you should be running WebWolf on localhost:9090/WebWolf momentarily



### Mapping

The web application runs on '/' and the controllers and Thymeleaf templates are hardcoded to '/WebWolf' we need
to have '/' available which acts as a landing page for incoming requests.