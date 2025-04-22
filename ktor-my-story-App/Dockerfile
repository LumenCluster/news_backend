# Step 1: Build Stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY . .
RUN ./gradlew installDist

# Step 2: Run Stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/install/ktor-my-story-App /app
EXPOSE 8080
CMD ["./bin/ktor-my-story-App"]
