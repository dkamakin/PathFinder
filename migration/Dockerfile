FROM maven:3-jdk-8-alpine

ENV LIQUIBASE_PROJECT_DIR=${LIQUIBASE_PROJECT_DIR}\
    LIQUIBASE_DATABASE_URL=${LIQUIBASE_DATABASE_URL}\
    LIQUIBASE_DATABASE_USERNAME=${LIQUIBASE_DATABASE_USERNAME}\
    LIQUIBASE_DATABASE_PASSWORD=${LIQUIBASE_DATABASE_PASSWORD}

ADD ${LIQUIBASE_PROJECT_DIR} /usr/local/alpine-jdk8-liquibase

WORKDIR /usr/local/alpine-jdk8-liquibase

CMD mvn liquibase:update -Dliquibase.url=${LIQUIBASE_DATABASE_URL} -Dliquibase.username=${LIQUIBASE_DATABASE_USERNAME} -Dliquibase.password=${LIQUIBASE_DATABASE_PASSWORD} -Dliquibase.changeLogFile=changelog.xml -Dliquibase.verbose=true