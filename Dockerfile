######################################################
### Release Build Stage (staging and production)
#####   #################################################

ARG BASE_IMAGE

FROM $BASE_IMAGE AS release

# Run as UID for nobody
USER 65534

ARG SERVICE_NAME
ARG SERVICE_HASH
ARG SERVICE_PORT
ARG DYNATRACE_ENABLED
ARG DEBUG_OPTS
ENV DYNATRACE_ENABLED_ENV=${DYNATRACE_ENABLED}
ENV DEBUG_OPTS_ENV=${DEBUG_OPTS}

WORKDIR /home/gradle/app
COPY ./build/libs/${SERVICE_NAME}-${SERVICE_HASH}.jar /home/gradle/app/app.jar
COPY ./build/resources/main/libs/trend_app_protect-4.4.6.jar /home/gradle/app/trend_app_protect-4.4.6.jar
COPY ./build/tmp/scripts/runService.sh /home/gradle/app/runService.sh
EXPOSE $SERVICE_PORT 8007

USER root

RUN chmod +x /home/gradle/app/runService.sh && chmod +w /opt/dynatrace/oneagent

USER 65534

ENTRYPOINT [ "sh", "-c", "/home/gradle/app/runService.sh ${DYNATRACE_ENABLED_ENV} ${DEBUG_OPTS_ENV}" ]

######################################################
### Debug Build Stage (dev and qa)
######################################################

FROM release AS debug

USER root

# Install curl, bash, and additional binaries for debugging / troubleshotting
RUN apk --update add curl bash && \
	rm -rf /var/cache/apk/*

USER 65534
