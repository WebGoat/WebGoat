#!/bin/sh
set -eu

ZAP_DIR=${ZAP_HOME:-/config/zap}
ZAP_PROXY_PORT=${ZAP_PROXY_PORT:-8090}
WEBGOAT_PAC_URL=${WEBGOAT_PAC_URL:-http://127.0.0.1:8080/WebGoat/proxy.pac}

if [ ! -d "$ZAP_DIR" ]; then
  echo "ZAP directory $ZAP_DIR not found" >&2
  exit 1
fi

ZAP_JAR=$(find "$ZAP_DIR" -maxdepth 1 -name 'zap-*.jar' -print | head -n1)

if [ -z "${ZAP_JAR:-}" ]; then
  echo "Unable to locate ZAP jar under $ZAP_DIR" >&2
  exit 1
fi

KWRITE_CONFIG=$(command -v kwriteconfig6 || command -v kwriteconfig5 || true)
if [ -n "$KWRITE_CONFIG" ]; then
  "$KWRITE_CONFIG" --file kioslaverc --group "Proxy Settings" --key ProxyType 2
  "$KWRITE_CONFIG" --file kioslaverc --group "Proxy Settings" --key "Proxy Config Script" "$WEBGOAT_PAC_URL"
  if command -v dbus-send >/dev/null 2>&1; then
    dbus-send --type=signal /KIO/Scheduler org.kde.KIO.Scheduler.reparseSlaveConfiguration string:'' || true
  fi
else
  echo "Unable to configure KDE proxy settings automatically" >&2
fi

FIREFOX_BIN=$(command -v firefox || true)
export _JAVA_AWT_WM_NONREPARENTING=1
if [ -n "$FIREFOX_BIN" ]; then
  exec /config/java-jdk/bin/java \
    "-Dselenium.firefoxBinary=$FIREFOX_BIN" \
    -jar "$ZAP_JAR" \
    -port "$ZAP_PROXY_PORT" \
    -config quickstart.launch.startPage=http://localhost:8080/WebGoat
fi
echo "Unable to locate Firefox; ZAP will start without a configured browser binary" >&2
exec /config/java-jdk/bin/java \
  -jar "$ZAP_JAR" \
  -port "$ZAP_PROXY_PORT" \
  -config quickstart.launch.startPage=http://localhost:8080/WebGoat
