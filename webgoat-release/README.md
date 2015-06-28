# Releasing WebGoat

## Introduction

This project will create a release for WebGoat ready for distribution.
This project creates a war with all the lessons included.

## Details

The following steps happen during the release:

* Download the webgoat-container.war from the repository
* Unpack the war
* Download the dist-plugin.zip from the repository
* Unpack the lessons
* Build the war again (webgoat-release-${version}.war)
* Create the executable jar (webgoat-release-${version}-war-exec.jar)

