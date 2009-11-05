#!/bin/sh

SYSTEM=`uname -s`
CATALINA_HOME=./tomcat
PATH=${PATH}:./tomcat/bin
export CATALINA_HOME PATH

chmod +x ./$CATALINA_HOME/bin/*.sh
if [ $SYSTEM = "Darwin" ]; then
        JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Home
        export JAVA_HOME

else

is_java_1dot6() {
	if [ -z "$JAVA_HOME" ]; then
        	export JAVA_HOME=$(dirname $(readlink -f $(which javac)))/../
	fi

        if [ "X$JAVA_HOME" != "X" -a $JAVA_HOME ]; then
                $JAVA_HOME/bin/java -version 2>&1 | grep 'version "1.6' >/dev/null
                if [ $? -ne 0 ]; then
                        echo "The JVM in \$JAVA_HOME isn't version 1.6."
                        exit 1
                fi
        else
                echo "Please set JAVA_HOME to a Java 1.6 JDK install"
                exit 1
        fi
}

is_java_1dot6

fi

case "$1" in
	start80)
		cp -f $CATALINA_HOME/conf/server_80.xml $CATALINA_HOME/conf/server.xml 
		$CATALINA_HOME/bin/startup.sh
		printf "\n  Open http://127.0.0.1/WebGoat/attack"
		printf "\n  Username: guest"
		printf "\n  Password: guest"
		printf "\n  Or try http://guest:guest@127.0.0.1/WebGoat/attack \n\n\r"
		sleep 2
		tail -f $CATALINA_HOME/logs/catalina.out
	;;
	start8080)
		cp -f $CATALINA_HOME/conf/server_8080.xml $CATALINA_HOME/conf/server.xml 
		$CATALINA_HOME/bin/startup.sh
		printf "\n  Open http://127.0.0.1:8080/WebGoat/attack"
		printf "\n  Username: guest"
		printf "\n  Password: guest"
		printf "\n  Or try http://guest:guest@127.0.0.1:8080/WebGoat/attack \n\n\r"
		sleep 2
		tail -f $CATALINA_HOME/logs/catalina.out
	;;
	stop)
		$CATALINA_HOME/bin/shutdown.sh
	;;
	*)
		echo $"Usage: $prog {start8080|start80|stop}"
		exit 1
	;;
esac
