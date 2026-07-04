# Step 1: Build the application using Maven and Java 17
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Step 2: Run the application using a lightweight Java 17 image
FROM openjdk:17-jdk-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]