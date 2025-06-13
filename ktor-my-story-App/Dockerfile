# --- Build Stage ---
# Use a Gradle image with a specific JDK version for compilation.
FROM gradle:8.5-jdk17 AS build

# Set the working directory inside the container for the build process.
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
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/
COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/

# Copy the entire Ktor project source code.
# This assumes your Dockerfile is located in the parent directory of 'ktor-my-story-App'.
# If your Dockerfile is *inside* 'ktor-my-story-App', you should change this to 'COPY . .' instead.
COPY ktor-my-story-App/ .

# Give execute permissions to the Gradle wrapper script.
# This is crucial for './gradlew' commands to work.
RUN chmod +x gradlew

# Build the application using the 'installDist' Gradle task.
# Added '--stacktrace' and '--info' flags for verbose logging to help debug future issues.
# '--no-daemon' prevents issues with Gradle daemon in Docker environments.
# '-x test' skips running tests during the build for faster image creation.
RUN ./gradlew installDist --no-daemon -x test --stacktrace --info

# --- Run Stage ---
# Use a minimal JRE (Java Runtime Environment) base image for the final runtime image.
# This makes the final Docker image much smaller and more secure than using a full JDK.
FROM eclipse-temurin:17-jre-focal

# Set the working directory for the final application inside the container.
WORKDIR /app

# Copy the compiled application distribution from the 'build' stage into the 'run' stage.
# The 'installDist' task typically creates a 'build/install/<project-name>' directory.
# Replace 'ktor-my-story-App' below with the actual name of your Ktor project
# (as defined in your settings.gradle.kts or settings.gradle file if it differs).
COPY --from=build /workspace/build/install/ktor-my-story-App/ .

# Expose the port your Ktor application listens on.
# Ktor applications commonly run on port 8080 by default.
# Ensure this matches the port configured in your Ktor application (e.g., in application.conf).
EXPOSE 8080

# Define the default command to run the application when the container starts.
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
