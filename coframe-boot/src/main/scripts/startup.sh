#!/bin/sh

PRG="$0"
saveddir=`pwd`
EOS_DAP_HOME=`dirname "$PRG"`/..
EOS_DAP_HOME=`cd "$EOS_DAP_HOME" && pwd`
cd "$saveddir"

export APP_NAME=eos-coframe
export MODE=service
export LOG_FOLDER="${EOS_DAP_HOME}"/logs
export PID_FOLDER="${LOG_FOLDER}"
export LOG_FILENAME="${APP_NAME}.out" # log console for background mode running

mkdir -p $LOG_FOLDER/$APP_NAME

export JAVA_OPTS="$JAVA_OPTS -server -Djava.net.preferIPv4Stack=true -Duser.timezone=Asia/Shanghai -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"
export JAVA_OPTS="$JAVA_OPTS $EOS_DAP_HEAP_OPTS"
export JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError"
# export JAVA_OPTS="$JAVA_OPTS -XX:+UseParNewGC -XX:ParallelGCThreads=4 -XX:MaxTenuringThreshold=9 -XX:+UseConcMarkSweepGC"

action=$1
if [[ "$action" == "run" ]] || [[ "$action" == "start" ]]; then
  shift
else
  action="start"
fi
BOOT_JAR=`echo "${EOS_DAP_HOME}"/*.jar`
$BOOT_JAR $action "$@"

