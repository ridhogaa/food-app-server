FROM ubuntu:latest
LABEL authors="ridhogymnastiar"

# Use a base image with JDK 17
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file into the container
COPY target/scheduler-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (the port your Spring Boot application runs on)
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
