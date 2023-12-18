FROM --platform=linux/amd64 openjdk:17-jdk-alpine
ENV spring.profiles.active=docker
COPY build/libs/subscribfy-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


# docker build -t epfzja/subscribe:latest ./
# docker push epfzja/subscribe:latest

# docker-compose up -d java_app
# --platform=linux/amd64
