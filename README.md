# weather-store

Prerequisites
- Docker Desktop
- IntelliJ

URLs
- http://localhost:8080/actuator
- http://localhost:8080/v3/api-docs
- http://localhost:8080/api/v1/weather

Commands
- docker run --rm -it -v "$HOME/.m2":/root/.m2 -v "$PWD":/usr/build -w /usr/build maven:3.8.4-eclipse-temurin-17 mvn clean verify site
- RUN groupadd java && useradd --gid java --groups java java