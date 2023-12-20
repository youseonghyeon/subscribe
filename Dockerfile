FROM --platform=linux/amd64 gradle:jdk17 as build
COPY . /app
WORKDIR /app
RUN gradle build -x test

FROM --platform=linux/amd64 openjdk:17-slim
COPY --from=build /app/build/libs/subscribfy-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
