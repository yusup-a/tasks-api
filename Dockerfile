# Use Maven image to build the app
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies first (for caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package the app
COPY src ./src
RUN mvn clean package -DskipTests

# Use smaller runtime image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy built JAR file from previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Spring Boot defaults to 8080)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
