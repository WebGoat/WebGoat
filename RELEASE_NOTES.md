# WebGoat release notes 

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




