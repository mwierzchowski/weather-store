ARG JAVA_VERSION=17.0.2_8
ARG LAYERS_DIR=/layers

FROM eclipse-temurin:${JAVA_VERSION}-jdk-alpine AS builder
ARG LAYERS_DIR
RUN mkdir ${LAYERS_DIR}
WORKDIR /app
RUN --mount=type=cache,target=/root/.gradle \
    --mount=type=bind,source=.git,target=/app/.git \
    --mount=type=bind,source=gradle,target=/app/gradle \
    --mount=type=bind,source=src,target=/app/src \
    --mount=type=bind,source=build.gradle,target=/app/build.gradle \
    --mount=type=bind,source=gradle.properties,target=/app/gradle.properties \
    --mount=type=bind,source=gradlew,target=/app/gradlew \
    --mount=type=bind,source=lombok.config,target=/app/lombok.config \
    ./gradlew clean build -x check --console=plain --no-daemon --info
RUN java -Djarmode=layertools -jar build/libs/app.jar extract --destination ${LAYERS_DIR}

FROM eclipse-temurin:${JAVA_VERSION}-jre-alpine
ARG LAYERS_DIR
WORKDIR /app
RUN addgroup -S java && adduser -S java -G java
USER java:java
COPY --from=builder ${LAYERS_DIR}/dependencies/ ./
COPY --from=builder ${LAYERS_DIR}/spring-boot-loader/ ./
COPY --from=builder ${LAYERS_DIR}/snapshot-dependencies/ ./
COPY --from=builder ${LAYERS_DIR}/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
