# Multi-stage Docker build for Pharmacy POS Spring Boot Backend
# Optimized for Render Free Tier (512MB RAM)

# Stage 1: Build the JAR
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy source code and build executable JAR
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B && cp target/pharmacy-pos-*.jar target/app.jar

# Stage 2: Minimal Production Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy built JAR from builder stage
COPY --from=builder /app/target/app.jar app.jar

# Render dynamic port binding
ENV PORT=8081
EXPOSE 8081 10000

# Render dynamic port binding with shell expansion
ENTRYPOINT ["sh", "-c", "java -XX:+UseContainerSupport -Xmx384m -Xms128m -XX:+UseG1GC -Dserver.port=${PORT:-8081} -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
