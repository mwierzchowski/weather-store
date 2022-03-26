ARG BUILD_DIR=/usr/build
ARG LAYERS_DIR=${BUILD_DIR}/target/layers

FROM maven:3.8.4-eclipse-temurin-17-alpine AS maven-builder
ARG BUILD_DIR
ARG LAYERS_DIR
WORKDIR ${BUILD_DIR}
RUN --mount=type=cache,target=/root/.m2 \
    --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=bind,source=src,target=src \
    --mount=type=bind,source=.git,target=.git \
    mvn package -DskipTests
RUN mkdir -p ${LAYERS_DIR}
RUN java -Djarmode=layertools -jar target/*.jar extract --destination ${LAYERS_DIR}

FROM eclipse-temurin:17.0.2_8-jre-alpine
ARG LAYERS_DIR
WORKDIR /usr/app
RUN addgroup -S java && adduser -S java -G java
USER java:java
COPY --from=maven-builder ${LAYERS_DIR}/dependencies/ ./
COPY --from=maven-builder ${LAYERS_DIR}/spring-boot-loader/ ./
COPY --from=maven-builder ${LAYERS_DIR}/snapshot-dependencies/ ./
COPY --from=maven-builder ${LAYERS_DIR}/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
