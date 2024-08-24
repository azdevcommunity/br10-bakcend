FROM gradle:8.4.0-jdk17 as build

WORKDIR /app

COPY build.gradle settings.gradle /app/     
COPY gradlew* /app/ 
COPY src /app/src

RUN gradle build --no-daemon

FROM openjdk:17-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

VOLUME /app/uploads

EXPOSE 8082

CMD ["sh", "-c", "java -jar app.jar --spring.profiles.active=$BR10_PROFILE"]
