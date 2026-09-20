# Stage 1: Build stage
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml and download dependencies to optimize caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and package application
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Minimal runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Run as non-root user for security best practices
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy jar built in Stage 1
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Dynamically bind Spring Boot's port to Render's PORT env variable
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} app.jar"]