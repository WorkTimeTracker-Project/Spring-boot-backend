# Basis-Image mit OpenJDK 23
FROM openjdk:23-jdk-slim

# Arbeitsverzeichnis im Container erstellen
WORKDIR /app

# Kopiere die pom.xml und das Verzeichnis mit den Quellcode-Dateien
COPY pom.xml .
COPY src ./src

COPY mvnw .
COPY .mvn .mvn

# Stelle sicher, dass mvnw ausführbar ist
RUN chmod +x mvnw

# Baue das Projekt
RUN ./mvnw clean install

# Exponiere den Port, auf dem die Anwendung läuft
EXPOSE 8080

# Startbefehl für das Spring Boot-Projekt
CMD ["java", "-jar", "target/workTimeTracker-0.0.1-SNAPSHOT.jar"]
