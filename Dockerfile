FROM ubuntu:latest
LABEL authors="ridhogymnastiar"

# Use a base image with JDK 17
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /opt/api

# Copy the Spring Boot JAR file into the container (adjust the path and filename as necessary)
COPY target/*.jar app.jar

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "-Xms64m", "app.jar"]

