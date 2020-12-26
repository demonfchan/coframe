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

BOOT_JAR=`echo "${EOS_DAP_HOME}"/*.jar`
$BOOT_JAR stop "$@"

