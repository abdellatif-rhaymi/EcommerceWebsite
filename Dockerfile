############################################
# Étape 1 — Build (Maven)
############################################
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /build

# Copier le pom.xml pour résoudre les dépendances en cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler et packager (sans exécuter les tests)
RUN mvn -B clean package -DskipTests

############################################
# Étape 2 — Exécution (Tomcat)
############################################
FROM tomcat:10.1.68-jdk17-openjdk-slim
LABEL maintainer="Abdellatif Rhaymi <abdellatif.rhaymi@gmail.com>"

USER root

# Installer utilitaires pour créer des utilisateurs et exécuter curl pour le healthcheck
RUN apt-get update && apt-get install -y \
    sudo \
    passwd \
    shadow \
    curl \
    procps \
    && rm -rf /var/lib/apt/lists/*

# Créer un utilisateur non-root pour exécuter Tomcat
RUN groupadd -r app && useradd -r -g app app \
    && mkdir -p /usr/local/tomcat/webapps /opt/logs \
    && chown -R app:app /usr/local/tomcat /opt/logs

# Passer à l'utilisateur non-root
USER app


# Healthcheck pour vérifier que l’application est démarrée
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8079/ecommerce/ || exit 1
