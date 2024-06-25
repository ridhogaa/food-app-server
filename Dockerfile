FROM ubuntu:latest
LABEL authors="ridhogymnastiar"

# Use a base image with JDK 17
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file into the container (adjust the path and filename as necessary)
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

