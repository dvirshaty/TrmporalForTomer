FROM eclipse-temurin:21-jre-jammy as builder
WORKDIR application
COPY target/*.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:21-jre-jammy

RUN apt-get update \
      && apt-get -y dist-upgrade \
      && apt-get -y install \
      && apt-get autoremove -y --purge \
      && apt-get -y clean \
      && apt-get -y autoclean \
      && rm -rf /var/lib/apt/lists/* \
      && find /var/cache/debconf -type f -print0 | xargs -0 rm -f \
      && find /var/cache/apt -type f -print0 | xargs -0 rm -f

WORKDIR application
ARG USER_NAME=maestro
ARG GROUP_NAME=maestro
RUN groupadd -g 1001 $GROUP_NAME \
    && useradd -u 1000 $USER_NAME -g $GROUP_NAME \
    && chown $USER_NAME:$GROUP_NAME ./
USER $USER_NAME

COPY --chown=$USER_NAME:$GROUP_NAME ./applicationinsights-agent-*.jar ./applicationinsights-agent.jar
COPY --from=builder --chown=$USER_NAME:$GROUP_NAME application/dependencies/ ./
COPY --from=builder --chown=$USER_NAME:$GROUP_NAME application/spring-boot-loader/ ./
COPY --from=builder --chown=$USER_NAME:$GROUP_NAME application/snapshot-dependencies/ ./
COPY --from=builder --chown=$USER_NAME:$GROUP_NAME application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]