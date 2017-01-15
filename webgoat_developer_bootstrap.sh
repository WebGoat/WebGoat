#!/bin/bash

# Bootstrap the setup of WebGoat for developer use in Linux and Mac machines
# This script will clone the necessary git repositories, call the maven goals
# in the order the are needed and launch tomcat listening on localhost:8080
# Happy hacking !

# Find out what is our terminal size
COLS="$(tput cols)"
if (( COLS <= 0 )) ; then
    COLS="${COLUMNS:-80}"
fi

# Colors
ESC_SEQ="\x1b["
COL_RESET=$ESC_SEQ"39;49;00m"
COL_RED=$ESC_SEQ"31;01m"
COL_GREEN=$ESC_SEQ"32;01m"
COL_YELLOW=$ESC_SEQ"33;01m"
COL_BLUE=$ESC_SEQ"34;01m"
COL_MAGENTA=$ESC_SEQ"35;01m"
COL_CYAN=$ESC_SEQ"36;01m"

# Horizontal Rule function
horizontal_rule() {
    local WORD

    for WORD in "#"
    do
        hr "$WORD"
    done
}

hr() {
    local WORD="$1"
    if [[ -n "$WORD" ]] ; then
        local LINE=''
        while (( ${#LINE} < COLS ))
        do
            LINE="$LINE$WORD"
        done

        echo -e "${LINE:0:$COLS}"
    fi
}

## test if command exists
ftest() {
  echo -e "$COL_CYAN  info: Checking if ${1} is installed $COL_RESET"
  if ! type "${1}" > /dev/null 2>&1; then
    return 1
  else
    return 0
  fi
}


## feature tests
features() {
  for f in "${@}"; do
    ftest "${f}" || {
      echo -e >&2 "***$COL_RED ERROR: Missing \`${f}'! Make sure it exists and try again. $COL_RESET"
      return 1
    }
  done
  return 0
}

tomcat_started () {
    STAT=`netstat -na | grep 8080 | awk '{print $6}'`
    if [ "$STAT" = "LISTEN" ]; then
        echo -e "$COL_GREEN WebGoat has started successfully! Browse to the following address. $COL_RESET"
        echo -e "$COL_CYAN Happy Hacking! $COL_RESET"
        return 0

    elif [ "$STAT" = "" ]; then
        echo -e "$COL_RED WebGoat failed to start up.... please wait run the following command for debugging : $COL_RESET"
        echo -e "$COL_MAGENTA  mvn -q -file WebGoat/pom.xml -pl webgoat-container tomcat7:run-war"
    fi
    return 1
}


## main setup
developer_bootstrap() {
    horizontal_rule
    echo -e "$COL_RED
    ██╗    ██╗███████╗██████╗  ██████╗  ██████╗  █████╗ ████████╗
    ██║    ██║██╔════╝██╔══██╗██╔════╝ ██╔═══██╗██╔══██╗╚══██╔══╝
    ██║ █╗ ██║█████╗  ██████╔╝██║  ███╗██║   ██║███████║   ██║
    ██║███╗██║██╔══╝  ██╔══██╗██║   ██║██║   ██║██╔══██║   ██║
    ╚███╔███╔╝███████╗██████╔╝╚██████╔╝╚██████╔╝██║  ██║   ██║
     ╚══╝╚══╝ ╚══════╝╚═════╝  ╚═════╝  ╚═════╝ ╚═╝  ╚═╝   ╚═╝
    $COL_RESET"
    horizontal_rule
    echo -e "Welcome to the WebGoat Developer Bootstrap script for Linux/Mac."
    echo -e "Now checking if all the required software to run WebGoat is already installed."
    echo -e "FYI: This Developer Bootstrap Script for WebGoat requires: Git, Java JDK and Maven accessible on the path"

    ## test for require features
    features git mvn java || return $?

    # Clone WebGoat from github
    if [ ! -d "WebGoat" ]; then
        echo -e "Cloning the WebGoat container repository"
        git clone https://github.com/WebGoat/WebGoat.git
    else
        horizontal_rule
        (
            echo -e "$COL_YELLOW The WebGoat container repo has already been clonned before, pulling upstream changes. $COL_RESET"
            cd WebGoat || {
                echo -e >&2 "$COL_RED *** ERROR: Could not cd into the WebGoat Directory. $COL_RESET"
                return 1
            }
            git pull origin develop
        )
    fi

    # Start the embedded Tomcat server
    echo -e "$COL_MAGENTA"
    horizontal_rule
    horizontal_rule
    horizontal_rule
    horizontal_rule
    echo "$COL_MAGENTA"
    echo "$COL_CYAN ***** Starting WebGoat using the embedded Tomcat ***** $COL_RESET"
    echo " Please be patient.... The startup of the server takes about 5 seconds..."
    echo " WebGoat will be ready for you when you see the following message on the command prompt:"
    echo "$COL_YELLOW INFO: Starting ProtocolHandler ["http-bio-8080"] $COL_RESET"
    echo "$COL_CYAN When you see the message above, open a web browser and navigate to http://localhost:8080/WebGoat/ $COL_RESET"
    echo " To stop the WebGoat and Tomcat Execution execution, press CTRL + C"
    echo "$COL_RED If you close this terminal window, Tomcat and WebGoat will stop running $COL_RESET"
    echo "$COL_MAGENTA"
    horizontal_rule
    horizontal_rule
    horizontal_rule
    horizontal_rule
    echo -e "$COL_RESET"
    sleep 5

    # Starting WebGoat
    mvn -q -pl webgoat-container spring-boot:run
}

# Start main script
developer_bootstrap
