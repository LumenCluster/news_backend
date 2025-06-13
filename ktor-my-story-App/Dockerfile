# --- Build Stage ---
# Use a Gradle image with a specific JDK version for compilation.
# 'gradle:8.5-jdk17' is a good choice as it includes Gradle and JDK 17.
FROM gradle:8.5-jdk17 AS build

# Set the working directory inside the container for the build process.
# All subsequent commands in this stage will be relative to /workspace.
WORKDIR /workspace

# Copy the entire Ktor project source code into the container.
# IMPORTANT: This assumes your Dockerfile is located in the parent directory
# of your 'ktor-my-story-App' folder.
# If your Dockerfile is *inside* 'ktor-my-story-App', use 'COPY . .' instead.
COPY ktor-my-story-App/ .

# Ensure the Gradle wrapper is executable.
# This is necessary before you can run any './gradlew' commands.
RUN chmod +x gradlew

# Build the application using the 'installDist' Gradle task.
# 'installDist' creates a runnable distribution in 'build/install/<project-name>'.
# --no-daemon: Recommended for Docker to prevent issues with long-running Gradle daemons.
# -x test: Skips running tests during the build, which speeds up the Docker build process.
#          Remove '-x test' if you want tests to run as part of your Docker image build.
RUN ./gradlew installDist --no-daemon -x test

# --- Run Stage ---
# Use a minimal JRE (Java Runtime Environment) base image for the final runtime image.
# This makes the final Docker image significantly smaller and more secure
# compared to using a full JDK for runtime, as the JDK includes development tools
# not needed for running a compiled application.
FROM eclipse-temurin:17-jre-focal

# Set the working directory for the final application.
WORKDIR /app

# Copy the compiled application distribution from the 'build' stage into the 'run' stage.
# The 'installDist' task typically places the output in /workspace/build/install/<project-name>.
# The '.' at the end means copy the *contents* of that directory into '/app'.
# Replace 'ktor-my-story-App' below with the actual name of your Ktor project (as defined
# in your settings.gradle.kts or settings.gradle file if it differs).
COPY --from=build /workspace/build/install/ktor-my-story-App/ .

# Expose the port your Ktor application listens on.
# Ktor applications commonly run on port 8080 by default.
# Ensure this matches the port configured in your Ktor application (e.g., in application.conf).
EXPOSE 8080

# Define the command to start the Ktor application when the container launches.
# This command assumes the 'installDist' task created an executable script
# in a 'bin' subdirectory directly within the copied application distribution.
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
