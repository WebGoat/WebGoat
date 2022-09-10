#!/bin/sh

DATA_DIRECTORY="$PWD/data"
REPORT_DIRECTORY="$PWD/reports"

if [ ! -d "$DATA_DIRECTORY" ]; then
  echo "Initially creating persistent directories"
  mkdir -p "$DATA_DIRECTORY"
  chmod -R 777 "$DATA_DIRECTORY"

  mkdir -p "$REPORT_DIRECTORY"
  chmod -R 777 "$REPORT_DIRECTORY"
fi

cd webgoat-container

# Make sure we are using the latest version
docker pull owasp/dependency-check

# mvn install -Dmaven.test.skip=true
docker run --rm \
  --volume $(pwd):/src \
  --volume "$DATA_DIRECTORY":/usr/share/dependency-check/data \
  --volume "$REPORT_DIRECTORY":/report \
  owasp/dependency-check \
  --scan /src \
  --format "CSV" \
  --project "Webgoat" \
  --failOnCVSS 8 \
  --out /report
