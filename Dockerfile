ARG JAVA_VERSION=17.0.2_8
ARG LAYERS_DIR=/layers

FROM eclipse-temurin:${JAVA_VERSION}-jdk-alpine AS builder
ARG LAYERS_DIR
WORKDIR /usr/app
RUN mkdir ${LAYERS_DIR}
ADD . .
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew clean build -x check --console=plain
RUN java -Djarmode=layertools -jar build/libs/app.jar extract --destination ${LAYERS_DIR}


FROM eclipse-temurin:${JAVA_VERSION}-jre-alpine
ARG LAYERS_DIR
WORKDIR /usr/app
RUN addgroup -S java && adduser -S java -G java
USER java:java
COPY --from=builder ${LAYERS_DIR}/dependencies/ ./
COPY --from=builder ${LAYERS_DIR}/spring-boot-loader/ ./
COPY --from=builder ${LAYERS_DIR}/snapshot-dependencies/ ./
COPY --from=builder ${LAYERS_DIR}/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
