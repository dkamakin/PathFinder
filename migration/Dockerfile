FROM maven:3.9.6-eclipse-temurin-21-alpine

ARG POSTGRES_JDBC_URI
ARG POSTGRES_USER
ARG POSTGRES_PASSWORD
ARG NEO4J_BOLT_URI
ARG NEO4J_USERNAME
ARG NEO4J_PASSWORD

ADD . /usr/local/liquibase
WORKDIR /usr/local/liquibase

CMD mvn liquibase:update -Dliquibase.url=${POSTGRES_JDBC_URI} -Dliquibase.username=${POSTGRES_USER} -Dliquibase.password=${POSTGRES_PASSWORD} -Dliquibase.changeLogFile=relational_changelog.xml \
    && mvn liquibase:update -Dliquibase.url=${NEO4J_BOLT_URI} -Dliquibase.username=${NEO4J_USERNAME} -Dliquibase.password=${NEO4J_PASSWORD} -Dliquibase.changeLogFile=neo4j_changelog.xml