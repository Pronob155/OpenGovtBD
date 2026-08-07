# Stage 1: Build the Maven application
FROM maven:3.8.6-openjdk-11-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the Java application
FROM openjdk:11-jre-slim
WORKDIR /app
# Adjust the jar filename match to what your pom.xml generates (usually target/your-app-name-0.0.1-SNAPSHOT.jar)
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
