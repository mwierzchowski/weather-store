FROM maven:3.8.4-eclipse-temurin-17 as uber-jar
WORKDIR /usr/build
COPY pom.xml pom.xml
RUN mvn dependency:go-offline
COPY src src
COPY .git .git
RUN mvn clean verify

FROM eclipse-temurin:17.0.2_8-jre as image
RUN groupadd java && useradd --gid java --groups java java
USER java:java
WORKDIR /usr/app
COPY --from=uber-jar /usr/build/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
