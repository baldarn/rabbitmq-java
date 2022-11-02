#!/bin/bash

./mvnw install -DskipTests
java -javaagent:opentelemetry-javaagent.jar \
    -Dotel.service.name=spring-boot-app \
    -Dotel.exporter.otlp.endpoint=http://localhost:4317 \
    -jar target/rabbitmq-0.0.1-SNAPSHOT.jar
