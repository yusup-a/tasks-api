# syntax=docker/dockerfile:1

# -------- Build stage --------
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy Maven wrapper & POM first (caches dependencies)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -B -DskipTests dependency:go-offline

# Now copy source and build
COPY src ./src
RUN ./mvnw -q -B -DskipTests package

# -------- Runtime stage --------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Render maps traffic to your container's exposed port.
# We'll default to 8080 but also honor $PORT if Render sets it.
ENV JAVA_OPTS=""
ENV PORT=8080
EXPOSE 8080

# Copy jar built in the first stage
COPY --from=build /app/target/*.jar app.jar

# Start the app
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
