FROM eclipse-temurin:17-jre-alpine
COPY app/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
