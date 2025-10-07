#!/bin/sh
set -eu

ZAP_DIR=${ZAP_HOME:-/config/zap}

if [ ! -d "$ZAP_DIR" ]; then
  echo "ZAP directory $ZAP_DIR not found" >&2
  exit 1
fi

ZAP_JAR=$(find "$ZAP_DIR" -maxdepth 1 -name 'zap-*.jar' -print | head -n1)

if [ -z "${ZAP_JAR:-}" ]; then
  echo "Unable to locate ZAP jar under $ZAP_DIR" >&2
  exit 1
fi

exec /config/java-jdk/bin/java -jar "$ZAP_JAR"
