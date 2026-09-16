# Use Java 21 runtime
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy the Spring Boot JAR
COPY target/careertrack-0.0.1-SNAPSHOT.jar app.jar

# Application port
EXPOSE 8080

# Start CareerTrack
ENTRYPOINT ["java", "-jar", "app.jar"]