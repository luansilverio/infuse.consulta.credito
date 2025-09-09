# Build stage - Java 8
FROM maven:3.9-eclipse-temurin-8 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -B -U dependency:go-offline
COPY src ./src
RUN mvn -q -e -B -U -DskipTests package org.springframework.boot:spring-boot-maven-plugin:2.7.18:repackage

# Run stage - Java 8
FROM eclipse-temurin:8-jre
WORKDIR /app
COPY --from=build /app/target/*SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
