FROM openjdk:8-jre-alpine
RUN apk add bash
ARG WORKDIR="/opt/eos/dap"
ENV JAVA_OPTS="" EOS_DAP_HEAP_OPTS=""
WORKDIR ${WORKDIR}
ADD *.tar.gz ./
VOLUME ["${WORKDIR}/logs", "${WORKDIR}/config"]
ENTRYPOINT ["bin/startup.sh", "run"]
