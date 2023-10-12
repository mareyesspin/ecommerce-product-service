#! /bin/bash

DYNATRACE_ENABLED=$1
DEBUG_OPTS=${@:2}

if [ $DYNATRACE_ENABLED = "true" ]
then
  export LD_PRELOAD=/opt/dynatrace/oneagent/agent/lib64/liboneagentproc.so
  echo -e "\n* LD_PRELOAD=${LD_PRELOAD}\n"
  echo -e "\n* DYNATRACE AGENT ENABLED \n"
else
  echo -e "\n* DYNATRACE AGENT DISABLED \n"
fi

echo -e "\n* Running application with args ${DEBUG_OPTS}\n"
java -javaagent:/home/gradle/app/trend_app_protect-4.4.6.jar -Djava.security.egd=file:/dev/./urandom ${DEBUG_OPTS} -jar app.jar
echo -e "\n* Application completed successfully ... \n"