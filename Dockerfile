ARG BUILD_HOME=/usr/build

FROM maven:3.8.4-eclipse-temurin-17-alpine AS builder
ARG BUILD_HOME
WORKDIR ${BUILD_HOME}
COPY pom.xml pom.xml
COPY src src
COPY .git .git

FROM builder AS quality-check
CMD mvn verify surefire-report:report-only --no-transfer-progress

FROM builder AS uber-jar
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests --no-transfer-progress

FROM eclipse-temurin:17.0.2_8-jre-alpine AS build
ARG BUILD_HOME
WORKDIR /usr/app
RUN addgroup -S java && adduser -S java -G java
COPY --from=uber-jar ${BUILD_HOME}/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar app.jar
