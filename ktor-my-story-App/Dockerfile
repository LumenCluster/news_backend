# Step 1: Build Stage
FROM gradle:8.5-jdk17 AS build
WORKDIR /workspace
COPY ktor-my-story-App/ .
RUN chmod +x gradlew && ./gradlew installDist

# Step 2: Run Stage
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the *contents* of the built distro into /app
COPY --from=build /workspace/build/install/ktor-my-story-App/ . 

# Expose the HTTP port
EXPOSE 8080

# Start the Ktor app
CMD ["bin/ktor-my-story-App"]
