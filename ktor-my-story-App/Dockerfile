# --- Build Stage ---
FROM gradle:8.5-jdk17 AS build

# Set the working directory inside the container for the build process.
# All subsequent commands in this stage will be relative to /workspace.
WORKDIR /workspace

# Copy the entire Ktor project source code into the container.
# IMPORTANT: This assumes your Dockerfile is located in the parent directory
# of your 'ktor-my-story-App' folder, and that 'gradlew' and the 'gradle' directory
# are also in that parent directory.
COPY gradlew .
COPY gradle/ gradle/ # Copy the gradle wrapper's support files
COPY ktor-my-story-App/ .

# Ensure the Gradle wrapper is executable.
RUN chmod +x gradlew

# Build the application using the 'installDist' Gradle task.
# Added '--stacktrace' and '--info' flags to get more detailed error messages
# and build information from Gradle, which should reveal the underlying cause.
RUN ./gradlew installDist --no-daemon -x test --stacktrace --info

# --- Run Stage ---
# Use a minimal JRE (Java Runtime Environment) base image for the final runtime image.
FROM eclipse-temurin:17-jre-focal

# Set the working directory for the final application.
WORKDIR /app

# Copy the compiled application distribution from the 'build' stage.
# Replace 'ktor-my-story-App' below with the actual name of your Ktor project.
COPY --from=build /workspace/build/install/ktor-my-story-App/ .

# Expose the port your Ktor application listens on.
EXPOSE 8080

# Define the command to start the Ktor application when the container launches.
# Replace 'ktor-my-story-App' with your actual Ktor project's name.
CMD ["bin/ktor-my-story-App"]









# # Step 1: Build Stage
# FROM gradle:8.5-jdk17 AS build
# WORKDIR /workspace
# COPY ktor-my-story-App/ .
# RUN chmod +x gradlew && ./gradlew installDist

# # Step 2: Run Stage
# FROM eclipse-temurin:17-jdk
# WORKDIR /app

# # Copy the *contents* of the built distro into /app
# COPY --from=build /workspace/build/install/ktor-my-story-App/ . 

# # Expose the HTTP port
# EXPOSE 8080

# # Start the Ktor app
# CMD ["bin/ktor-my-story-App"]
