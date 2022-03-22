FROM maven:3.8.4-eclipse-temurin-17-alpine as maven-base
WORKDIR /usr/build
COPY pom.xml pom.xml
COPY src src
COPY .git .git

FROM maven-base as maven-verify
RUN --mount=type=cache,target=/root/.m2 \
    mvn verify surefire-report:report-only --no-transfer-progress

FROM maven-base as maven-build
RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests --no-transfer-progress

FROM eclipse-temurin:17.0.2_8-jre-alpine as docker-build
RUN addgroup -S java && adduser -S java -G java
USER java:java
WORKDIR /usr/app
COPY --from=maven-build /usr/build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
