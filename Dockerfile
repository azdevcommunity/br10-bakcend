# First stage: build the application
FROM gradle:8.4.0-jdk17 as build

WORKDIR /app

COPY build.gradle settings.gradle /app/      
COPY gradlew* /app/ 
COPY src /app/src

RUN gradle build --no-daemon && ls -l /app/build/libs

# Second stage: use the built jar in a minimal image
FROM openjdk:17-jdk
 
WORKDIR /app 

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/br10-1.0.0.jar /app/app.jar

VOLUME /app/uploads

EXPOSE 8082

CMD ["sh", "-c", "java -jar app.jar --spring.profiles.active=$BR10_PROFILE"]
