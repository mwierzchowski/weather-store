# weather-store
[![Integration](https://github.com/mwierzchowski/weather-store/actions/workflows/integration.yaml/badge.svg)](https://github.com/mwierzchowski/weather-store/actions/workflows/integration.yaml)
[![Maintainability](https://api.codeclimate.com/v1/badges/b10f065419566a429d1c/maintainability)](https://codeclimate.com/github/mwierzchowski/weather-store/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/b10f065419566a429d1c/test_coverage)](https://codeclimate.com/github/mwierzchowski/weather-store/test_coverage)

Prerequisites
- Docker Desktop
- IntelliJ

URLs
- http://localhost:8080/actuator
- http://localhost:8080/v3/api-docs
- http://localhost:8080/api/v1/weather
- https://codeclimate.com/github/mwierzchowski/weather-store

Commands
- docker run --rm -it -v "$HOME/.m2":/root/.m2 -v "$PWD":/usr/build -w /usr/build maven:3.8.4-eclipse-temurin-17 mvn clean verify site
- RUN groupadd java && useradd --gid java --groups java java