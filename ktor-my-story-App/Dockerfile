# Step 1: Build Stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY gradlew .
COPY gradlew.bat . # If you have it
COPY settings.gradle.kts . # Or your settings file
COPY build.gradle.kts .    # Or your build file
COPY ktor-my-story-App/src ./src
# Add any other top-level files or directories your project needs at the root

RUN chmod +x gradlew
RUN ./gradlew installDist

# Step 2: Run Stage
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/build/install/ktor-my-story-App /app
RUN chmod +x /app/bin/ktor-my-story-App
EXPOSE 8080
CMD ["./bin/ktor-my-story-App"]
