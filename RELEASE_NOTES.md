# WebGoat release notes

## Version 2023.8

### 🚀 New functionality

- Consistent environment values and url references (#1677)
- Show directly requested file in requests overview
- Show creating time in file upload overview

### 🐞 Bug fixes

- Fix startup message (#1687)
- Fix/state of software supply chain links (#1683)
- Fix WebWolf UI (#1686)

### 🔄 Technical tasks

- bump actions/setup-java from 3 to 4 (#1690)
- bump commons-io:commons-io from 2.14.0 to 2.15.1 (#1689)
- bump com.diffplug.spotless:spotless-maven-plugin (#1688)

## Version 2023.5

### New functionality

- Implement JWT jku example (#1552)
- Java 21 initial support (#1622)
- improve MFAC lesson hint texts for a better user experience (#1424)
- upgrade to Spring Boot version 3 (#1477)

### Bug fixes

- typo in WebGoad.txt (#1667)
- search box moved and jwt encode/decode with little delay (#1664)
- skip validation for JWT (#1663)
- fixed issue in JWT test tool and added robot test (#1658)
- Password reset link test condition more strict and move all WebWolf links to /WebWolf  (#1645)
- fix servers id (#1619)
- potential NPE in the stored XSS assignment
- crypto basics broken links
- fixes the default change in trailing slash matching and address the affected assignments
- hint that was breaking the template, causing hints from different assignments to mix (#1424)
- HijackSession lesson template deprecated Tymeleaf attribute
- Fix NPE in IDOR lesson
- Add new assignment IT tests
- XSS mitigation
- Stored Cross-Site Scripting Lesson
- Add Assignment7 Tests
- Fix IDOR lesson
- remove steps from release script (#1509)
- robotframework fails due to updated dependencies (#1508)
- fix Java image inside Docker file The image now downloads the correct Java version based on the architecture.
- Fix typo of HijackSession_content0.adoc
- Restrict SSRF Regexes
- update challenge code - Flags are now wired through a Spring config - Introduced Flag class - Removed Flags from the FlagController

## Version 2023.4

### New functionality

- [#1422 Add Docker Linux Desktop variant with all tools installed](https://github.com/WebGoat/WebGoat/issues/1422). Thanks to the [OWASP WrongSecrets project](https://owasp.org/www-project-wrongsecrets/) we now have a Docker Linux desktop image with all the tools installed. No need to install any tools locally only run the new Docker image. See README.md for details on how to start it.
- [#1411 JWT: looks that buy as Tom also works with alg:none](https://github.com/WebGoat/WebGoat/issues/1411).

### Bug fixes

- [#1410 WebWolf: JWT decode is broken](https://github.com/WebGoat/WebGoat/issues/1410).
- [#1396 password_reset return 500 Error](https://github.com/WebGoat/WebGoat/issues/1396).
- [#1379 Move XXE to A05:2021-Security Misconfiguration](https://github.com/WebGoat/WebGoat/issues/1379).

## Version 2023.3

With great pleasure, we present you with a new release of WebGoat **2023.3**. Finally, it has been a while. This year starts with a new release of WebGoat. This year we will undoubtedly release more often. From this release on, we began to use a new versioning scheme (https://calver.org/#scheme).

A big thanks to René Zubcevic and Àngel Ollé Blázquez for keeping the project alive this last year, and hopefully, we can make
many more releases this year.

### New functionality

- New year's resolution(2022): major refactoring of WebGoat to simplify the setup and improve building times.
- Move away from multi-project setup:
  * This has a huge performance benefit when building the application. Build time locally is now `Total time:  42.469 s` (depends on your local machine of course)
  * No longer add Maven dependencies in several places
  * H2 no longer needs to run as separate process, which solves the issue of WebWolf sharing and needing to configure the correct database connection.
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
- Updated introduction lesson to WebWolf.
- Added language switch for support for multiple languages.
- Removed logic to start WebGoat on a random port when port `8080` is taken. We would loop until we found a free port. We simplified this to just start on the specified port.
- Add Google formatter for all our code, a PR now checks whether the code adheres to the standard.
- Renaming of all packages and folders.
- [#1039 New OWASP Top 10](https://github.com/WebGoat/WebGoat/issues/1093)
- [#1065 New lesson about logging](https://github.com/WebGoat/WebGoat/issues/1065)

### Bug fixes

- [#1193 Vulnerable component lesson - java.desktop does not "opens java.beans" to unnamed module](https://github.com/WebGoat/WebGoat/issues/1193)
- [#1176 Minor: XXE lesson 12 patch not reset by 'lesson reset' while it IS reset by leaving/returning to lesson](https://github.com/WebGoat/WebGoat/issues/1176)
- [#1134 "Exploiting XStream" assignment does not work](https://github.com/WebGoat/WebGoat/issues/1134)
- [#1130 Typo: Using Indrect References](https://github.com/WebGoat/WebGoat/issues/1130)
- [#1101 SQL lesson not correct](https://github.com/WebGoat/WebGoat/issues/1101)
- [#1079 startup.sh issues of WebWolf - cannot connect to the WebGoat DB](https://github.com/WebGoat/WebGoat/issues/1079)
- [#1379 Move XXE to A05:2021-_Security_ Misconfiguration](https://github.com/WebGoat/WebGoat/issues/1379)
- [#1298 SocketUtils is deprecated and will be removed in Spring Security 6](https://github.com/WebGoat/WebGoat/issues/1298)
- [#1248 Rewrite the WebWolf Introduction Lesson with the new changes](https://github.com/WebGoat/WebGoat/issues/1248)
- [#1200 Type cast error in sample code at JWT token section](https://github.com/WebGoat/WebGoat/issues/1200)
- [#1173 --server.port=9000 is not respected on Windows (both cmd as Powershell)](https://github.com/WebGoat/WebGoat/issues/1173)
- [#1103 (A1) path traversel lesson 7 seems broken](https://github.com/WebGoat/WebGoat/issues/1103)
- [#986 - User registration not persistant](https://github.com/WebGoat/WebGoat/issues/986)

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
