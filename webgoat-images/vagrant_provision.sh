#!/usr/bin/env bash
set -e

echo "Setting locale..."
sudo update-locale LC_ALL=en_US.UTF-8 LANG=en_US.UTF-8

sudo kill -9 $(lsof -t /var/lib/dpkg/lock) || true
sudo apt-get update
sudo apt-get install -y git

echo "Installing required packages..."
sudo apt-get install -y -q build-essential autotools-dev automake pkg-config expect


## Chrome
wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list'
sudo apt-get update
sudo apt-get install -y google-chrome-stable

## Java 8
echo "Provisioning Java 8..."
mkdir -p /home/vagrant/java
cd /home/vagrant/java
test -f /tmp/jdk-8-linux-x64.tar.gz || curl -q -L --cookie "oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u144-b01/090f390dda5b47b9b721c7dfaa008135/jdk-8u144-linux-x64.tar.gz -o /tmp/jdk-8-linux-x64.tar.gz

sudo mkdir -p /usr/lib/jvm
sudo tar zxf /tmp/jdk-8-linux-x64.tar.gz -C /usr/lib/jvm

sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk1.8.0_144/bin/java" 1
sudo update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk1.8.0_144/bin/javac" 1
sudo update-alternatives --install "/usr/bin/javaws" "javaws" "/usr/lib/jvm/jdk1.8.0_144/bin/javaws" 1

sudo chmod a+x /usr/bin/java
sudo chmod a+x /usr/bin/javac
sudo chmod a+x /usr/bin/javaws
sudo chown -R root:root /usr/lib/jvm/jdk1.8.0_144

echo "export JAVA_HOME=/usr/lib/jvm/jdk1.8.0_144" >> /home/vagrant/.bashrc

## Maven
echo "Installing Maven.."
sudo apt-get install -y maven

## ZAP
echo "Provisioning ZAP..."
cd /home/vagrant
mkdir tools
cd tools
wget https://github.com/zaproxy/zaproxy/releases/download/2.5.0/ZAP_2.5.0_Linux.tar.gz
tar xvfx ZAP_2.5.0_Linux.tar.gz
rm -rf ZAP_2.5.0_Linux.tar.gz

## IntelliJ
cd /home/vagrant/tools
wget https://download.jetbrains.com/idea/ideaIC-2016.1.4.tar.gz
tar xvfz ideaIC-2016.1.4.tar.gz
rm -rf ideaIC-2016.1.4.tar.gz

## Eclipse
sudo apt-get -y install eclipse

