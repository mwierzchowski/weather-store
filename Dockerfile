ARG BUILD_HOME=/usr/build

FROM maven:3.8.4-eclipse-temurin-17-alpine AS maven-builder
WORKDIR ${BUILD_HOME}
RUN --mount=type=cache,target=/root/.m2 \
    --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=bind,source=src,target=src \
    --mount=type=bind,source=.git,target=.git \
    mvn package -DskipTests --no-transfer-progress

FROM eclipse-temurin:17.0.2_8-jre-alpine
WORKDIR /usr/app
RUN addgroup -S java && adduser -S java -G java
COPY --from=maven-builder ${BUILD_HOME}/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar app.jar
