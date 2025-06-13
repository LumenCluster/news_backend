# --- Build Stage ---
# Use a Gradle image that includes Gradle 8.5 and JDK 17.
# This is suitable for building projects that use Gradle 8.10.
FROM gradle:8.5-jdk17 AS build

# Set the working directory inside the container for the build process.
# All subsequent commands in this stage will be relative to /workspace.
WORKDIR /workspace

# Copy the Gradle wrapper script (gradlew).
# This assumes 'gradlew' is in the same directory as your Dockerfile.
COPY gradlew .

# Create the directory structure for the Gradle wrapper support files inside the container.
# This ensures that 'gradle/wrapper' exists before we try to copy files into it.
RUN mkdir -p gradle/wrapper

# Copy the specific Gradle wrapper files into the container.
# These paths (gradle/wrapper/gradle-wrapper.jar and gradle/wrapper/gradle-wrapper.properties)
# are relative to the location of your Dockerfile on your host machine.
# Ensure these files actually exist in your GitHub repository at these paths.
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/
COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/

# Copy the entire Ktor project source code.
# IMPORTANT: This assumes your Dockerfile is located in the parent directory
# of your 'ktor-my-story-App' folder.
# If your Dockerfile is *inside* 'ktor-my-story-App', you should change this to 'COPY . .' instead.
COPY ktor-my-story-App/ .

# Give execute permissions to the Gradle wrapper script.
# This is crucial for './gradlew' commands to work inside the container.
RUN chmod +x gradlew

# Build the application using the 'installDist' Gradle task.
# 'installDist' creates a runnable distribution of your application.
# --no-daemon: Recommended for Docker to prevent issues with long-running Gradle daemons.
# -x test: Skips running tests during the build, which speeds up the Docker image creation.
#          Remove '-x test' if you need tests to be executed as part of your Docker build.
# --stacktrace --info: These flags provide verbose logging from Gradle, which is
#                      extremely helpful for debugging if the build fails again.
RUN ./gradlew installDist --no-daemon -x test --stacktrace --info

# --- Run Stage ---
# Use a minimal JRE (Java Runtime Environment) base image for the final runtime image.
# This makes the final Docker image significantly smaller and more secure
# compared to using a full JDK for runtime, as the JDK includes development tools
# not needed for running a compiled application. JDK 17 is compatible with Gradle 8.10.
FROM eclipse-temurin:17-jre-focal

# Set the working directory for the final application inside the container.
WORKDIR /app

# Copy the compiled application distribution from the 'build' stage into the 'run' stage.
# The 'installDist' task typically places the output in /workspace/build/install/<project-name>.
# The '.' at the end means copy the *contents* of that directory into '/app' in the run stage.
# Replace 'ktor-my-story-App' below with the actual name of your Ktor project
# (as defined in your settings.gradle.kts or settings.gradle file if it differs).
COPY --from=build /workspace/build/install/ktor-my-story-App/ .

# Expose the port your Ktor application listens on.
# Ktor applications commonly run on port 8080 by default.
# Ensure this matches the port configured in your Ktor application (e.g., in application.conf).
EXPOSE 8080

# Define the default command to run the application when the container launches.
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
