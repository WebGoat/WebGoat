# WebGoat release notes 

## Unreleased

### New functionality

- New year's resolution: major refactoring of WebGoat to simplify the setup and improve building times.
- Move away from multi-project setup:
  - This has a huge performance benefit when building the application. Build time locally is now `Total time:  42.469 s` (depends on your local machine of course)
  - No longer add Maven dependencies in several places
  - H2 no longer needs to run as separate process, which solves the issue of WebWolf sharing and needing to configure the correct database connection.
- More explicit paths in html files to reference `adoc` files, less magic.
- Integrate WebWolf in WebGoat, the setup was way too complicated and needed configuration which could lead to mistakes and a not working application. This also simplifies the Docker configuration as there is only 1 Docker image.
- Add WebWolf button in WebGoat
- Move all lessons into `src/main/resources` 
- WebGoat selects a port dynamically when starting. It will still start of port 8080 it will try another port to ease the user experience. 
- WebGoat logs URL after startup: `Please browse to http://127.0.0.1:8080/WebGoat to get started...`
- Simplify `Dockerfile` as we no longer need a script to start everything
- Maven build now start WebGoat jar with Maven plugin to make sure we run against the latest build.
- Added `Initializable` interface for a lesson, an assignment can implement this interface to set it up for a specific user and to reset the assignment back to its original state when a reset lesson occurs. See `BlindSendFileAssignment` for an example.
- Integration tests now use the same user. This saves a lot of time as before every test used a different user which triggered the Flyway migration to set up the database schema for the user. This migration took a lot of time.

## Version 8.2.2

### New functionality

- Docker image now supports nginx when browsing to http://localhost a landing page is shown. 

### Bug fixes

- [#1039 jwt-7-Code review](https://github.com/WebGoat/WebGoat/issues/1039)
- [#1031 SQL Injection (intro) 5: Data Control Language (DCL) the wiki's solution is not correct](https://github.com/WebGoat/WebGoat/issues/1031)
- [#1027 Webgoat 8.2.1 Vulnerable_Components_12 Shows internal server error](https://github.com/WebGoat/WebGoat/issues/1027)


## Version 8.2.1

### New functionality

- New Docker image for arm64 architecture is now available (for Apple M1)


## Version 8.2.0

### New functionality

- Add new zip slip lesson (part of path traversal)
- SQL lessons are now separate for each user, database are now per user and no longer shared across users
- Moved to Java 15 & Spring Boot 2.4 & moved to JUnit 5

### Bug fixes

- [#974 SQL injection Intro 5 not solvable](https://github.com/WebGoat/WebGoat/issues/974)
- [#962 SQL-Lesson 5 (Advanced) Solvable with wrong anwser](https://github.com/WebGoat/WebGoat/issues/962)
- [#961 SQl-Injection lesson 4 not deleting created row](https://github.com/WebGoat/WebGoat/issues/961)
- [#949 Challenge: Admin password reset always solvable](https://github.com/WebGoat/WebGoat/issues/949)
- [#923 - Upgrade to Java 15](https://github.com/WebGoat/WebGoat/issues/923)
- [#922 - Vulnerable components lesson](https://github.com/WebGoat/WebGoat/issues/922)
- [#891 - Update the OWASP website with the new all-in-one Docker container](https://github.com/WebGoat/WebGoat/issues/891)
- [#844 - Suggestion: Update navigation](https://github.com/WebGoat/WebGoat/issues/844)
- [#843 - Bypass front-end restrictions: Field restrictions - confusing text in form](https://github.com/WebGoat/WebGoat/issues/843)
- [#841 - XSS - Reflected XSS confusing instruction and success messages](https://github.com/WebGoat/WebGoat/issues/841)
- [#839 - SQL Injection (mitigation) Order by clause confusing](https://github.com/WebGoat/WebGoat/issues/839)
- [#838 - SQL mitigation (filtering) can only be passed by updating table](https://github.com/WebGoat/WebGoat/issues/838)

## Contributors

Special thanks to the following contributors providing us with a pull request:

- nicholas-quirk
- VijoPlays
- aolle
- trollingHeifer
- maximmasiutin
- toshihue
- avivmu
- KellyMarchewa 
- NatasG
- gabe-sky


## Version 8.1.0

### New functionality

- Added new lessons for cryptography and path-traversal
- Extra content added to the XXE lesson
- Explanation of the assignments will be part of WebGoat, in this release we added detailed descriptions on how to solve the XXE lesson. In the upcoming releases new explanations will be added. If you want to contribute please create a pull request on Github.
- Docker improvements + docker stack for complete container with nginx 
- Included JWT token decoding and generation, since jwt.io does not support None anymore 

### Bug fixes

- [#743 - Character encoding errors](https://github.com/WebGoat/WebGoat/issues/743)
- [#811 -  Flag submission fails](https://github.com/WebGoat/WebGoat/issues/811)
- [#810 - Scoreboard for challenges shows csrf users](https://github.com/WebGoat/WebGoat/issues/810)
- [#788 - strange copy in constructor](https://github.com/WebGoat/WebGoat/issues/788) 
- [#760 - Execution of standalone jar fails (Flyway migration step](https://github.com/WebGoat/WebGoat/issues/760)
- [#766 - Unclear objective of vulnerable components practical assignment](https://github.com/WebGoat/WebGoat/issues/766)
- [#708 - Seems like the home directory of WebGoat always use @project.version@](https://github.com/WebGoat/WebGoat/issues/708)
- [#719 - WebGoat: 'Contact Us' email link in header is not correctly set](https://github.com/WebGoat/WebGoat/issues/719)
 - [#715 - Reset lesson doesn't reset the "HTML lesson" => forms stay succesful](https://github.com/WebGoat/WebGoat/issues/715)
 - [#725 - Vulnerable Components lesson 12 broken due to too new dependency](https://github.com/WebGoat/WebGoat/issues/725)
 - [#716 -  On M26 @project.version@ is not "interpreted" #7](https://github.com/WebGoat/WebGoat/issues/716)
 - [#721 couldn't be able to run CSRF lesson 3: Receive Whitelabel Error Page](https://github.com/WebGoat/WebGoat/issues/721)
 - [#724 - Dead link in VulnerableComponents lesson 11](https://github.com/WebGoat/WebGoat/issues/724)
 
 ## Contributors
 
Special thanks to the following contributors providing us with a pull request:

- Satoshi SAKAO
- Philippe Lafoucrière
- Cotonne
- Tiago Mussi
- thegoodcrumpets
- Atharva Vaidya
- torleif
- August Detlefsen
- Choe Hyeong Jin

And everyone who provided feedback through Github.


Team WebGoat




