FROM openjdk:21-jdk-slim
WORKDIR /app
EXPOSE 8080
ARG JAR_FILE
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
