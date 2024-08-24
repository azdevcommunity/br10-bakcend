# Stage 1: Build the application
FROM gradle:8.4.0-jdk17 as build

# Set the working directory inside the container
WORKDIR /app

# Copy only the necessary files for dependency resolution
COPY build.gradle settings.gradle /app/

# Copy the Gradle wrapper scripts (if any)
COPY gradlew* /app/

# Copy the source code
COPY src /app/src

# Build the application
RUN gradle build --no-daemon

# Stage 2: Create the final image
FROM openjdk:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Set environment variables
ENV BR10_PROFILE=dev \
    BR10_MAX_FILE_SIZE=10MB \
    BR10_MAX_REQUEST_SIZE=10MB \
    BR10_DB_URL=jdbc:postgresql://109.199.110.107:5433/br10db \
    BR10_DB_USERNAME=postgres \
    BR10_DB_PASSWORD=123456789 \
    BR10_REDIS_HOST=109.199.110.107 \
    BR10_REDIS_PORT=6379 \
    BR10_UPLOAD_DIR=./uploads \
    CORS_ALLOWED_ORIGINS=http://109.199.110.107 \
    BR10_JWT_SECRET=MZl1GC5OHL+km2NKuTrUNH7PcnqMyvbUBzeUVsPGAlqY7BqAGjFcEJgAZTd4ziYB \
    BR10_JWT_REFRESH_EXPIRATION=86400000 \
    BR10_JWT_EXPIRATION=86400000 \
    PORT=8082

# Expose the specified port
EXPOSE $PORT

# Run the application
CMD ["sh", "-c", "java -jar app.jar --spring.profiles.active=$BR10_PROFILE --server.port=$PORT"]
