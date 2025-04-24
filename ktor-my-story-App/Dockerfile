# Stage 1: Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
# Copy entire project (not just subdirectory)
COPY . .  # Changed from 'COPY ktor-my-story-App/ .'
RUN chmod +x gradlew && \
    ./gradlew --no-daemon installDist

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-jammy  # Changed from alpine

WORKDIR /app
COPY --from=build /app/build/install/ktor-my-story-App /app

# Security & dependencies
RUN apt-get update && apt-get install -y curl && \
    adduser --disabled-password --no-create-home ktor-user && \
    chown -R ktor-user:ktor-user /app && \
    chmod +x /app/bin/ktor-my-story-App  # Explicit permission

USER ktor-user
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/health || exit 1
ENTRYPOINT ["./bin/ktor-my-story-App"]  # Correct case
