FROM maven:3.8.4-eclipse-temurin-17-alpine as build-maven
WORKDIR /usr/build
COPY pom.xml pom.xml
RUN mvn clean dependency:go-offline --no-transfer-progress
COPY src src
COPY .git .git
RUN mvn verify surefire-report:report-only --no-transfer-progress

FROM eclipse-temurin:17.0.2_8-jre-alpine as build-docker
RUN addgroup -S java && adduser -S java -G java
USER java:java
WORKDIR /usr/app
COPY --from=build-maven /usr/build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
