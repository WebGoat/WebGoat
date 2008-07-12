ECHO OFF
IF NOT EXIST WebGoat_5_2_workspace GOTO UNPACK
set JAVAHOME=java
set PATH=%JAVAHOME%\bin;%PATH%
set ECLIPSE_HOME=eclipse
SET JAVA_OPTS=%JAVA_OPTS% -Xms128m -Xmx768m

%ECLIPSE_HOME%\eclipse.exe -data .\WebGoat_5_2_workspace
GOTO END

:UNPACK
ECHO *
ECHO *
ECHO *
ECHO *
ECHO *   ERROR -- eclipse workspace is missing
ECHO *
ECHO *
ECHO *
ECHO *
ECHO *   Use winzip to unzip Eclipse-Workspace.zip
ECHO *
ECHO *
ECHO *
PAUSE

:END

