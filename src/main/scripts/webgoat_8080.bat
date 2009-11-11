@echo on


@REM Clear the lib env var as it can hose tomcat
SET lib= 

@REM Make sure the webgoat DB is writable
attrib -R .\tomcat\webapps\WebGoat\database\*.*

@REM Set env vars for tomcat and java, use PWD as some machines don't have
@REM \. on their path
set PWD=%cd%
set CATALINA_HOME=%PWD%\tomcat
set JAVA_HOME=%PWD%\java

del .\tomcat\conf\server.xml
copy .\tomcat\conf\server_8080.xml .\tomcat\conf\server.xml

@REM Run tomcat: must have quotes incase var has spaces in it
call "%CATALINA_HOME%\bin\startup.bat" start

echo 
echo If the Tomcat DOS shell quit immediately, it is likely that 
echo there is another service listening on port 80.
echo
