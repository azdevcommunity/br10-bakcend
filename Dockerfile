FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/br10-1.0.0.jar
COPY ${JAR_FILE} app.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
