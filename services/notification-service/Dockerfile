# ========================
# STAGE 1: Build
# ========================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and POM files
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
COPY common/pom.xml common/
COPY services/notification-service/pom.xml services/notification-service/

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -pl services/notification-service -am -B

# Copy source code
COPY common/src common/src
COPY services/notification-service/src services/notification-service/src

# Build the application
RUN ./mvnw package -pl services/notification-service -am -DskipTests -B

# Extract layered JAR
WORKDIR /app/services/notification-service/target
RUN java -Djarmode=layertools -jar *.jar extract

# ========================
# STAGE 2: Runtime
# ========================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Add labels
LABEL maintainer="TrimQ Team"
LABEL service="notification-service"
LABEL version="1.0.0"

# Create non-root user
RUN addgroup -g 1000 trimq && \
    adduser -u 1000 -G trimq -s /bin/sh -D trimq

WORKDIR /app

# Copy layers from builder
COPY --from=builder /app/services/notification-service/target/dependencies/ ./
COPY --from=builder /app/services/notification-service/target/spring-boot-loader/ ./
COPY --from=builder /app/services/notification-service/target/snapshot-dependencies/ ./
COPY --from=builder /app/services/notification-service/target/application/ ./

# Set ownership
RUN chown -R trimq:trimq /app

# Switch to non-root user
USER trimq

# Expose port
EXPOSE 8085

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8085/actuator/health || exit 1

# JVM settings for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0"

# Run application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]

