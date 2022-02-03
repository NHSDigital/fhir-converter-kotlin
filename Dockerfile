FROM openjdk:11-slim as build
WORKDIR /workspace/app

# copying the gradle wrapper script
COPY gradlew .
# copy the gradle wrapper folder - has the info about gradle distribution that script will use
COPY gradle gradle
# copy file that has dependencies
COPY build.gradle.kts .
# copy source code
COPY src src
# run the gradle build
RUN ./gradlew build
# WORKDIR build
# WORKDIR libs
# CMD ["ls"]

FROM openjdk:11-slim
#EXPOSE 9000
# copy the jar file from the previous stage
COPY --from=build /workspace/app/build/libs/app-0.0.1-SNAPSHOT.jar .
# executable
ENTRYPOINT ["java", "-jar", "app-0.0.1-SNAPSHOT.jar"]