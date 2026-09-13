#!/usr/bin/env sh
set -eu
url="${1:-http://127.0.0.1:8080/WebGoat/}"
retries="${2:-60}"
i=1
while [ "$i" -le "$retries" ]; do
  if curl -fsS "$url" >/dev/null 2>&1; then
    echo "WebGoat is ready: $url"
    exit 0
  fi
  echo "Waiting for WebGoat ($i/$retries)..."
  i=$((i+1))
  sleep 5
done
echo "WebGoat did not become ready" >&2
exit 1
