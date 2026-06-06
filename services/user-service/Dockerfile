# Multi-stage Docker build for optimized image size
# Stage 1: Build with Maven
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy parent pom and common module first (for caching)
COPY pom.xml ./
COPY common/pom.xml ./common/
COPY common/src ./common/src

# Copy service pom
COPY services/user-service/pom.xml ./services/user-service/

# Download dependencies (cached layer)
RUN apk add --no-cache maven && \
    mvn dependency:go-offline -pl services/user-service -am -B

# Copy service source
COPY services/user-service/src ./services/user-service/src

# Build the application
RUN mvn package -pl services/user-service -am -DskipTests -B

# Extract layered JAR for optimized Docker layers
WORKDIR /app/services/user-service
RUN java -Djarmode=layertools -jar target/*.jar extract

# Stage 2: Runtime image
FROM eclipse-temurin:21-jre-alpine

# Add non-root user for security
RUN addgroup -g 1001 -S trimq && \
    adduser -u 1001 -S trimq -G trimq

WORKDIR /app

# Copy layered application
COPY --from=builder /app/services/user-service/dependencies/ ./
COPY --from=builder /app/services/user-service/spring-boot-loader/ ./
COPY --from=builder /app/services/user-service/snapshot-dependencies/ ./
COPY --from=builder /app/services/user-service/application/ ./

# Set ownership
RUN chown -R trimq:trimq /app

# Switch to non-root user
USER trimq

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8081/actuator/health || exit 1

# JVM options for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]

