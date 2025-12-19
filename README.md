<p align="center">
    <img src="/static/images/logo.png" alt="SCA Goat">
     <br>SCA Goat<br> Navigating SCA Vulnerabilities, Empowering Mastery<br> <p align="center">
</p>
</p>


## What is SCA-Goat?

SCAGoat is an application for Software Composition Analysis (SCA) that focuses on vulnerable and compromised JAR dependencies used in development code, providing users with hands-on learning opportunities to understand potential attack scenarios. It is designed to identify vulnerabilities that may arise from using vulnerable JAR files.

## ⚠️ WARNING: Educational Purpose Only ⚠️

This project contains deliberately vulnerable and malicious code for educational purposes. The xz-java-malicious package included in this project simulates a compromised library and should NEVER be used in production environments. This package is designed solely for training security professionals and for evaluating SCA tools.

## Presented at:
- [DC32: Demo Labs](https://forum.defcon.org/node/249617)
- [Appsec Village: Arsenal](https://www.appsecvillage.com/events/dc-2024/arsenal-scagoat-661284)
- [Blackhat Europe 2024](https://www.blackhat.com/eu-24/arsenal/schedule/index.html#scagoat---exploiting-damn-vulnerable-sca-application-42139)
- [Blackhat Asia 2025](https://www.blackhat.com/asia-25/arsenal/schedule/index.html#scagoat---exploiting-damn-vulnerable-and-compromised-sca-application-43960)

## What All CVE Covered?

The CVEs covered under SCAGoat are primarily critical and high severity, which have a CVSS score of 9. This aid in understanding the vulnerable package being used and its potential for exploitation. 

In addition, there is one compromised package, that lacks a CVE, but is malicious by nature and cannot be detected with traditional SCA scanners.

| CVE                        | Package Name    | Link  | 
|----------------------------|-----------------|-------|
| CVE-2023-42282             | IP              | [https://nvd.nist.gov/vuln/detail/CVE-2023-42282](https://nvd.nist.gov/vuln/detail/CVE-2023-42282) |     
| CVE-2017-1000427           | Marked          | [https://nvd.nist.gov/vuln/detail/CVE-2017-1000427](https://nvd.nist.gov/vuln/detail/CVE-2017-1000427) |     
| CVE-2017-16114             | Marked          | [https://github.com/markedjs/marked/issues/926](https://github.com/markedjs/marked/issues/926) |
| CVE-2021-44228             | log4j           | [https://nvd.nist.gov/vuln/detail/CVE-2021-44228](https://nvd.nist.gov/vuln/detail/CVE-2021-44228)|
| CVE-2020-9547              | jackson-databind | [https://nvd.nist.gov/vuln/detail/CVE-2020-9547](https://nvd.nist.gov/vuln/detail/CVE-2020-9547)|
| CVE-2021-33623             | trim-newlines   | [https://nvd.nist.gov/vuln/detail/CVE-2021-33623](https://nvd.nist.gov/vuln/detail/CVE-2021-33623)|
| CVE-2020-13935             | spring-websocket | [https://nvd.nist.gov/vuln/detail/CVE-2020-13935](https://nvd.nist.gov/vuln/detail/CVE-2020-13935)|
| CVE-2019-10744             | lodash          | [https://nvd.nist.gov/vuln/detail/CVE-2019-10744](https://nvd.nist.gov/vuln/detail/CVE-2019-10744)|
| CVE-2019-8331              | pug             | [https://nvd.nist.gov/vuln/detail/CVE-2019-8331](https://nvd.nist.gov/vuln/detail/CVE-2019-8331)|
| CVE-2020-8116              | dot-prop        | [https://nvd.nist.gov/vuln/detail/CVE-2020-8116](https://nvd.nist.gov/vuln/detail/CVE-2020-8116)|
| Malicious Package (No CVE) | xz-java         | [https://central.sonatype.com/artifact/io.github.xz-java/xz-java](https://central.sonatype.com/artifact/io.github.xz-java/xz-java)|



## Steps to run SCAGoat
Step 1. Clone the application
```bash
git clone https://github.com/harekrishnarai/Damn-vulnerable-sca.git
```
Step 2. Go to the Directory
```bash
cd Damn-vulnerable-sca
```
Step 3. Use the following docker commands to build the image for the dockerfile and run the image to access the application:
```bash
docker compose up
```
Step 4. Visit http://localhost:3000/ to access the nodejs application and http://localhost:8080 for Springboot for log4j

## Compiling and Installing the Malicious XZ-Java Package Locally

To ensure SCAGoat functions correctly for training and SCA tool evaluations, you'll need to compile and install the xz-java-malicious package locally:

1. Navigate to the xz-java-malicious directory:
```bash
cd xz-java-malicious
```

2. Compile and install the package to your local Maven repository:
```bash
mvn clean install
```

3. Verify the installation:
```bash
mvn dependency:tree
```

4. After successful installation, add the malicious package to your .m2 repo by running following command
```bash
mvn install:install-file \
  -Dfile=target/xz-java-1.9.2.jar \
  -DgroupId=org.tukaani \
  -DartifactId=xz \
  -Dversion=1.9.2-malicious \
  -Dpackaging=jar
```

5. Return to the main project directory:
```bash
cd ..
```

6. Now you can run the full application with docker compose as mentioned above.

### Important Notes:
- The malicious package is deliberately designed to be undetectable by some SCA tools, making it an excellent training tool.
- This package doesn't contain actual harmful code but simulates patterns of compromised libraries.
- Use in isolated, educational environments only.

### SCA Goat HomePage
![SCAGoat HomePage](https://github.com/user-attachments/assets/36cf4e09-5279-4b62-89ed-4fd5160f75c0)

## Vulnerability Dashboard

SCAGoat features an interactive vulnerability dashboard that allows users to explore and understand different types of vulnerabilities:

- **Marked (CVE-2017-16114)**: Cross-Site Scripting vulnerability in the Markdown parser
- **Trim-Newlines (CVE-2021-33623)**: Regular Expression Denial of Service vulnerability
- **Lodash (CVE-2019-10744)**: Critical prototype pollution vulnerability with CVSS 9.8
- **Jackson-Databind (CVE-2020-9547)**: Deserialization vulnerability in the backend
- **XZ-Java (Malicious)**: Compromised library demonstration
- **WebSocket (CVE-2020-13935)**: Spring WebSocket vulnerability
- **Log4j (CVE-2021-44228)**: Log4Shell vulnerability demonstration
- **Pug (CVE-2019-8331)**: Denial of Service vulnerability in the template engine
- **Dot-Prop (CVE-2020-8116)**: Prototype pollution vulnerability allowing property manipulation

Each vulnerability includes an interactive demo to help security professionals, developers, and students understand how these vulnerabilities work and how they can be exploited.

## What's Coming?

Our aim is to provide you with a better understanding of vulnerable packages and JAR dependencies so that you can gain hands-on experience. We will keep you updated with the latest CVEs. Stay tuned! 

## Tutorials to exploit the vulnerability:

|  Demo Videos | CVE Exploited |
|---------------|-----------|
| [Demo 1](https://www.youtube.com/watch?v=MXAuqGiB354) |    CVE-2023-42282 |           
| [Demo 2](https://youtu.be/HgLKVtKh87w) |     CVE-2017-16114 |     
| [Demo 3](https://youtu.be/BljNgBZxbgo) |     CVE-2021-44228 |
| [Demo 4](https://youtu.be/BGGu9jAJQ1I) | CVE-2020-9547 |
| [Demo 5](https://youtu.be/sWAzUP_uC7k) | XZ-JAVA compromised |
| [Demo 6](https://youtu.be/X7Qd8jkVjAI) | CVE-2019-10744 (Lodash) |
| [Demo 8]() | CVE-2019-8331 (Pug) |
| [Demo 9]() | CVE-2020-8116 (Dot-Prop) |

## SCA Scan Reports
- [Link to SCAGoat Scan Reports](https://docs.google.com/document/d/1hJxweaRQsC3XH7t36UwOGBPbyZWX1ZjLtmOoJAI0nIc/edit?usp=sharing)
- [Detailed Dependency Check Tool Report](https://drive.google.com/file/d/1u3pfSI2_t3MOXDtwAiJXOM4Ekdxd5v8H/view?usp=sharing)

## UI Enhancements

The SCAGoat application features a modern, responsive UI with the following features:

- Interactive vulnerability dashboard with informative cards
- Dark mode interface with particle.js background
- Detailed information about each vulnerability including CVSS scores
- Real-time demonstration of exploits
- Mobile-friendly responsive design

## Want to contribute? 
[![Fork this project](https://img.shields.io/github/forks/harekrishnarai/Damn-vulnerable-sca.svg?style=social)](https://github.com/harekrishnarai/Damn-vulnerable-sca/fork)
[![Start contributing](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/harekrishnarai/Damn-vulnerable-sca/issues)
<br>Awesome! The most basic way to show your support is to star the project or raise issues.

## Contributors
Thanks to all the people who already contributed!  
[Prashant Venkatesh](https://www.linkedin.com/in/prashant-venkatesh-99018999/)    
[Nandan Gupta](https://www.linkedin.com/in/nandan-gupta-698aa11b)  
[Hare Krishna Rai](https://www.linkedin.com/in/harekrishnarai/)  
[Henrik Plate](https://www.linkedin.com/in/henrikplate/)  
[Gaurav Joshi](https://www.linkedin.com/in/gauravjoshii/)  
[Yoad Fekete](https://www.linkedin.com/in/yoadfekete) 

<a href="https://github.com/harekrishnarai/Damn-vulnerable-sca/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=harekrishnarai/Damn-vulnerable-sca" />
</a>


