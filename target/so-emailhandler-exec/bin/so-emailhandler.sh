#! /bin/bash

# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
#title           :so-emailhandler.sh
#description     :This script manages so-emailhandler Process.
#author          :SIFT SO Team
#version         :0.0.1    
#usage           :bash so-emailhandler.sh
#handler_version :0.0.1-release
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

#source ./so-env.sh

: "${JAVA_HOME?JAVA_HOME not set}"
: "${SO_HOME?SO_HOME not set}"

# ***********************************************
SCRIPT_HOME=$(dirname "$0")

echo "so-emailhandler-0.0.1"
export API_CONF='/opt/knowesis/sift/orchestrator/conf'
export LOG_HOME='/opt/knowesis/sift/orchestrator/log'
export LOGBACK_XML=$API_CONF'/so-emailhandler-logback.xml'
export FLOW_LOC='/opt/knowesis/sift/orchestrator/flow'
export CAMEL_ENCRYPTION_PASSWORD=Sift@Knowesis_2020
export SO_ENCRYPT_ALGORITHM=PBEWITHMD5ANDDES

CP=$(echo ../lib/*.jar | tr ' ' ':')
export CLASSPATH=$API_CONF':'$CP':'$FLOW_LOC

ARGS='-Dlogback.configurationFile='$LOGBACK_XML' -DCONFIG_HOME='$API_CONF' -DLOG_HOME='$LOG_HOME' -jar '$FLOW_LOC'/so-emailhandler-0.0.1.jar'
DAEMON=/usr/lib/jvm/java-8-openjdk/bin/java

case "$1" in
start)
    (
		pid=`pgrep -f '.+so-emailhandler-.+.jar'`
		if [ ! -z $pid ]; then 
			echo "process found with pid "$pid
			echo "use $0 stop"
		else 
			echo 'Starting...'
    		$DAEMON $ARGS #> $LOG_HOME/so-emailhandler_sysout.log 2>&1
			echo $!
			
		fi
	) #& 
;;

status)
	pid=`pgrep -f '.+so-emailhandler-.+.jar'`
	if [ ! -z $pid ]; then 
		echo "process found with pid "$pid
	else 
		echo "process not found"
	fi
;;

stop)
	pid=`pgrep -f '.+so-emailhandler-.+.jar'`
	if [ ! -z $pid ]; then 
		echo "stopping ..."$pid
		pkill -f '.+so-emailhandler-.+.jar'
	else 
		echo "process not found"
	fi
;;

kill)
	pid=`pgrep -f '.+so-emailhandler-.+.jar'`
	if [ ! -z $pid ]; then 
		echo "killing ..."$pid
		pkill -9 -f '.+so-emailhandler-.+.jar'
	else 
		echo "process not found"
	fi
;;

log)
	tail -f $LOG_HOME/so-emailhandler.log
;;

restart)
    $0 stop
    $0 start
;;

*)
    echo "Usage: $0 {status|start|stop}"
    exit 1
esac
unset CAMEL_ENCRYPTION_PASSWORD

sh /opt/knowesis/sift/orchestrator/bin/so-env.sh

