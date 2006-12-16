set JAVAHOME= C:\Program Files\Java\jdk1.5.0_08
set PATH=%JAVAHOME%\bin;%PATH%
set ECLIPSE_HOME= C:\webgoat\tools\eclipse
SET JAVA_OPTS=%JAVA_OPTS% -Xms128m -Xmx768m

%ECLIPSE_HOME%\eclipse.exe -data .\workspace

