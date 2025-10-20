############################################
# Étape 1 — Build (Maven)
############################################
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /build

# Copie du pom.xml pour résoudre les dépendances en cache
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler et packager (sans exécuter les tests ici)
RUN mvn -B clean package -DskipTests


############################################
# Étape 2 — Exécution (Tomcat)
############################################
FROM tomcat:10.1-jdk17-corretto
LABEL maintainer="Abdellatif Rhaymi <abdellatif.rhaymi@gmail.com>"

# Créer utilisateur non-root
RUN groupadd -r app && useradd -r -g app app \
    && mkdir -p /usr/local/tomcat/webapps /opt/logs \
    && chown -R app:app /usr/local/tomcat /opt/logs

USER app

# Copier le .war construit
COPY --from=builder --chown=app:app /build/target/*.war /usr/local/tomcat/webapps/ecommerce.war

EXPOSE 8079

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8079/ecommerce/ || exit 1

