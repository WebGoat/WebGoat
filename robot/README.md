# Install and use Robotframework

## Install Chromedriver on Mac OS

        brew install cask chromedriver
        chromedriver --version

Then see security settings and allow the file to run

## Install

        pip3 install virtualenv --user
        python3 -m virtualenv .venv
        source .venv/bin/activate
        pip install --upgrade robotframework
        pip install --upgrade robotframework-SeleniumLibrary
        pip install --upgrade webdriver-manager
        brew upgrade
        robot --variable HEADLESS:"0" --variable ENDPOINT:"http://127.0.0.1:8080/WebGoat" goat.robot

Make sure that the Chrome version, the webdriver version and all related components are up-to-date and compatible!
