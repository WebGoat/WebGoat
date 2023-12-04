#!/bin/bash

# 빌드된 JAR 파일의 경로와 이름 설정
BUILD_JAR=$(ls /home/ec2-user/build/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> 빌드 파일명: $JAR_NAME" >> /home/ec2-user/deploy.log

# 애플리케이션을 배포할 경로 설정
DEPLOY_PATH=/home/ec2-user/WebGoat
echo "> 빌드 파일 복사" >> /home/ec2-user/deploy.log
cp $BUILD_JAR $DEPLOY_PATH

# 현재 실행 중인 애플리케이션의 프로세스 ID 확인
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z $CURRENT_PID ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy.log
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

# 배포된 JAR 파일의 경로와 이름 설정
DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo "> DEPLOY_JAR 배포" >> /home/ec2-user/deploy.log

# OWASP ZAP 실행
echo "> OWASP ZAP 실행" >> /home/ec2-user/deploy.log
/home/ec2-user/ZAP_2.10.0/zap.sh -quickurl http://127.0.0.1:8080/WebGoat/start.mvc?username=guest1#lesson/WebGoatIntroduction.lesson

# 기존의 Java 애플리케이션 실행 명령
nohup java -jar $DEPLOY_JAR >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &

# WebGoat 서버 실행 명령
echo "> WebGoat 서버 실행" >> /home/ec2-user/deploy.log
nohup $JAVA_HOME/bin/java -jar $DEPLOY_PATH/webgoat-server-8.2.2.jar >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &

