# Multi-stage Docker build for optimized image size
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml ./
COPY common/pom.xml ./common/
COPY common/src ./common/src
COPY services/shop-service/pom.xml ./services/shop-service/

RUN apk add --no-cache maven && \
    mvn dependency:go-offline -pl services/shop-service -am -B

COPY services/shop-service/src ./services/shop-service/src

RUN mvn package -pl services/shop-service -am -DskipTests -B

WORKDIR /app/services/shop-service
RUN java -Djarmode=layertools -jar target/*.jar extract

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -g 1001 -S trimq && \
    adduser -u 1001 -S trimq -G trimq

WORKDIR /app

COPY --from=builder /app/services/shop-service/dependencies/ ./
COPY --from=builder /app/services/shop-service/spring-boot-loader/ ./
COPY --from=builder /app/services/shop-service/snapshot-dependencies/ ./
COPY --from=builder /app/services/shop-service/application/ ./

RUN chown -R trimq:trimq /app
USER trimq

EXPOSE 8082

HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]

