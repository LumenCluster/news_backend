# Stage 1: Build
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY ktor-my-story-App/ .
RUN chmod +x gradlew && \
    ./gradlew --no-daemon installDist

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/build/install/ktor-my-story-App /app

# Security best practices
RUN adduser --disabled-password --no-create-home ktor-user && \
    chown -R ktor-user:ktor-user /app
USER ktor-user

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget -qO- http://localhost:8080/health || exit 1

ENTRYPOINT ["./bin/ktor-my-story-app"]
