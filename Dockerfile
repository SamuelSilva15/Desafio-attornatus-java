FROM openjdk:8-jdk

COPY /target/pessoas-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8083

CMD ["java", "-jar", "/app/app.jar"]