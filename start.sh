#!/bin/bash

# 빌드된 JAR 파일의 경로와 이름 설정
BUILD_JAR=$(ls /home/ec2-user/build/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo "> 빌드 파일명: $JAR_NAME" >> /home/ec2-user/deploy.log

# 애플리케이션을 배포할 경로 설정
DEPLOY_PATH=/home/ec2-user/
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
DEPLOY_JAR=$DEPLOY_PATH/$JAR_NAME
echo "> DEPLOY_JAR 배포" >> /home/ec2-user/deploy.log

# 기존의 Java 애플리케이션 실행 명령
nohup java -jar $DEPLOY_JAR >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &

# 서버 실행 명령
echo "> 서버 실행" >> /home/ec2-user/deploy.log
nohup java -jar "$DEPLOY_JAR" --server.port=8080 --server.address=43.200.16.60 &
