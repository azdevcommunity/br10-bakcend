FROM gradle:8.4.0-jdk21 as build

WORKDIR /app

COPY build.gradle settings.gradle /app/      
COPY gradlew* /app/ 
COPY src /app/src

RUN gradle build --no-daemon && ls -l /app/build/libs

FROM openjdk:21-jdk
 
WORKDIR /app 

COPY --from=build /app/build/libs/br10-1.0.0.jar /app/app.jar

VOLUME /app/uploads

EXPOSE 8082

CMD ["sh", "-c", "java -jar app.jar --spring.profiles.active=$BR10_PROFILE"]
