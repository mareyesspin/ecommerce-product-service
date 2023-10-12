#! /bin/bash

DYNATRACE_ENABLED=$1
DEBUG_OPTS=$2

# start CloudHSM client
echo -n "* Starting CloudHSM client ... "
/opt/cloudhsm/bin/cloudhsm_client /opt/cloudhsm/etc/cloudhsm_client.cfg &> /tmp/cloudhsm_client_start.log &

# wait for startup
while true
do
	if grep 'libevmulti_init: Ready !' /tmp/cloudhsm_client_start.log &> /dev/null
	then
		echo "[OK]"
		break
	fi
	sleep 0.5
done
echo -e "\n* CloudHSM client started successfully ... \n"

if [ $DYNATRACE_ENABLED = "true" ]
then
  export LD_PRELOAD=/opt/dynatrace/oneagent/agent/lib64/liboneagentproc.so
  echo -e "\n* LD_PRELOAD=${LD_PRELOAD}\n"
  echo -e "\n* DYNATRACE AGENT ENABLED \n"
else
  echo -e "\n* DYNATRACE AGENT DISABLED \n"
fi

echo -e "\n* Running application with args ${DEBUG_OPTS}\n"
java -javaagent:/home/gradle/app/trend_app_protect-4.4.6.jar -Djava.library.path=/opt/cloudhsm/lib -Djava.security.egd=file:/dev/./urandom ${DEBUG_OPTS} -jar app.jar
echo -e "\n* Application completed successfully ... \n"
