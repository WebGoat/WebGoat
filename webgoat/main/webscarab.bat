@echo off


@REM Run webscarab
@REM    - Assumes webscarab.properties file is in webscarab directory
cd webscarab
..\java\bin\javaw -Duser.home=.\ -jar webscarab.jar
