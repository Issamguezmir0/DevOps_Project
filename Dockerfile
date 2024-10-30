# Étape 1 : Utiliser l'image JDK pour construire l'application
FROM openjdk:17-jdk-alpine

# Étape 2 : Créer un répertoire de travail pour l'application
WORKDIR /app

# Étape 3 : Copier le fichier JAR de l'application dans l'image
COPY target/*.jar app.jar

# Étape 4 : Exposer le port sur lequel votre application va tourner (8081 dans ce cas)
EXPOSE 8081

# Étape 5 : Lancer l'application avec la commande java
ENTRYPOINT ["java", "-jar", "/app/app.jar"]