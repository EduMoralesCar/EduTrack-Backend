# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies to cache them
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/EduTrack-0.0.1-SNAPSHOT.jar app.jar
# Create uploads directory for academic materials
RUN mkdir -p uploads
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
