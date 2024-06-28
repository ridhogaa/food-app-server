FROM amazoncorretto:17
WORKDIR /app
COPY /target/FoodApp-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-jar", "FoodApp-0.0.1-SNAPSHOT.jar"]