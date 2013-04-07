#!/bin/bash

exec java \
  -Xmx1536M \
  "-XX:OnOutOfMemoryError=kill -9 %p" \
  -XX:+CMSClassUnloadingEnabled \
  -XX:+UseCodeCacheFlushing \
  -XX:ReservedCodeCacheSize=128m \
  -noverify \
  -XX:MaxPermSize=512m \
  -Xss2M \
  -jar $(dirname $0)/project/sbt-launch-0-12-3.jar \
  "$@"
