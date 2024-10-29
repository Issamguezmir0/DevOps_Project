# Utiliser une image de base Java (OpenJDK 17)
FROM openjdk:17-jdk-alpine

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR généré dans le conteneur
COPY target/Foyer-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port 8081 (ou le port utilisé par votre application)
EXPOSE 8081


# Démarrer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]