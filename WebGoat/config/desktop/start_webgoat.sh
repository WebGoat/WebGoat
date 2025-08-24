#!/bin/sh

/config/java-jdk/bin/java \
  -Duser.home=/config \
  -Dfile.encoding=UTF-8 \
  -DTZ=Europe/Amsterdam \
  --add-opens java.base/java.lang=ALL-UNNAMED \
  --add-opens java.base/java.util=ALL-UNNAMED \
  --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
  --add-opens java.base/java.text=ALL-UNNAMED \
  --add-opens java.desktop/java.beans=ALL-UNNAMED \
  --add-opens java.desktop/java.awt.font=ALL-UNNAMED \
  --add-opens java.base/sun.nio.ch=ALL-UNNAMED \
  --add-opens java.base/java.io=ALL-UNNAMED \
  --add-opens java.base/java.util=ALL-UNNAMED \
  -Drunning.in.docker=false \
  -jar /config/webgoat.jar
