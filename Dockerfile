FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/Accommodation.jar /app

EXPOSE 8080

CMD ["java", "-jar", "Accommodation.jar"]