# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first so Docker caches the dependencies
# as long as this file does not change
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B package -DskipTests

# ---------- Stage 2: run ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# The application does not run as root inside the container
RUN groupadd --system app && useradd --system --gid app app

COPY --from=build /app/target/*.jar app.jar
RUN chown app:app app.jar
USER app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
