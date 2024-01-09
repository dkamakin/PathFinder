FROM eclipse-temurin:21.0.1_12-jre-jammy

ARG MODULE_NAME=Indexer
ARG JAR_FILE_NAME=$MODULE_NAME.jar
ARG JAR_FILE_PATH=build/libs/$JAR_FILE_NAME
ENV JAR_FILE_ENV=$JAR_FILE_NAME

COPY $JAR_FILE_PATH .
RUN mkdir -p /root/.ssh \
    && chmod 700 /root/.ssh \
    && apt-get -yq update \
    && apt-get -yqq install ssh \
    && ssh-keyscan github.com > /root/.ssh/known_hosts \
    && apt-get clean
ENTRYPOINT java -jar "$JAR_FILE_ENV"