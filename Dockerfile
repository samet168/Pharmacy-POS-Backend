# Multi-stage Docker build for Pharmacy POS Spring Boot Backend
# Optimized for Render Free Tier (512MB RAM)

# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Minimal Production Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Render dynamic port binding
ENV PORT=8081
EXPOSE ${PORT}

# Memory optimization for Render Free Tier (512MB RAM constraint)
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Xmx384m", "-Xms128m", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
