#!/bin/bash

MINECRAFT_VERSION=1.20.1
MEMORY=4G

PLUGINS=(
  'https://github.com/monun/auto-reloader/releases/download/0.0.6/auto-reloader-0.0.6.jar'
  'https://github.com/dmulloy2/ProtocolLib/releases/download/5.1.0/ProtocolLib.jar'
)

set -x

mkdir -p .server
cd .server

if [[ ! -f paper.jar ]]; then
  build_info=$(curl https://api.papermc.io/v2/projects/paper/versions/$MINECRAFT_VERSION/builds | jq '.builds[-1]')
  build_id=$(echo $build_info | jq '.build')
  build_filename=$(echo $build_info | jq -r '.downloads.application.name')
  curl -o paper.jar https://api.papermc.io/v2/projects/paper/versions/$MINECRAFT_VERSION/builds/$build_id/downloads/$build_filename

  mkdir -p plugins
  cd plugins
  for PLUGIN in ${PLUGINS[@]}; do
    wget $PLUGIN
  done
  cd ..
fi

java -Xms${MEMORY} -Xmx${MEMORY} -XX:+UseG1GC -XX:+ParallelRefProcEnabled -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -XX:+AlwaysPreTouch -XX:G1NewSizePercent=30 -XX:G1MaxNewSizePercent=40 -XX:G1HeapRegionSize=8M -XX:G1ReservePercent=20 -XX:G1HeapWastePercent=5 -XX:G1MixedGCCountTarget=4 -XX:InitiatingHeapOccupancyPercent=15 -XX:G1MixedGCLiveThresholdPercent=90 -XX:G1RSetUpdatingPauseTimePercent=5 -XX:SurvivorRatio=32 -XX:+PerfDisableSharedMem -XX:MaxTenuringThreshold=1 -Dusing.aikars.flags=https://mcflags.emc.gs -Daikars.new.flags=true -jar paper.jar --nogui
