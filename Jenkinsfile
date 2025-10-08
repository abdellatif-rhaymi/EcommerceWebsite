pipeline {
    agent any

    environment {
        // Base de données réelle
        DB_URL = "jdbc:mysql://mysql:3306/ecommerce"
        DB_USER = "root"
        DB_PASS = "root"

        // Dossier partagé avec Tomcat
        TOMCAT_WEBAPPS = "/var/jenkins_home/tomcat_webapps"
        MAVEN_OPTS = "-Dmaven.repo.local=/root/.m2/repository"
    }

    stages {

        stage('Checkout') {
            steps {
                echo "📦 Récupération du code source..."
                git branch: 'main', url: 'https://github.com/abdellatif-rhaymi/EcommerceWebsite.git'
            }
        }

        stage('Parallel Build & Tests') {
            parallel {
                stage('Build (cached)') {
                    steps {
                        echo "🏗️ Compilation avec cache Maven..."
                        // Le cache Maven est déjà géré par le volume ~/.m2 dans Docker
                        sh 'mvn clean package -DskipTests'
                    }
                }

                stage('Unit Tests') {
                    steps {
                        echo "🧩 Exécution des tests unitaires..."
                        sh 'mvn test -Dtest=UtilisateurUnitTest'
                    }
                }

                stage('Unit Tests (H2)') {
                    steps {
                        echo "🧪 Exécution des tests JUnit avec base H2..."
                        sh 'mvn test -DTEST_ENV=true -Dtest=SampleTest'
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            environment {
                scannerHome = tool 'sonar-scanner'
            }
            steps {
                echo "🔍 Analyse de la qualité du code avec SonarQube..."
                withSonarQubeEnv('SonarQube') {
                    sh """
                        ${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=ecommerce \
                        -Dsonar.projectName="Ecommerce Website" \
                        -Dsonar.projectVersion=1.0 \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.junit.reportPaths=target/surefire-reports \
                        -Dsonar.host.url=http://sonarqube:9000
                    """
                }
            }
        }

        stage('Publish Test Report') {
            steps {
                echo "📊 Publication du rapport JUnit..."
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Incremental Deploy to Tomcat') {
            when {
                changeset "**/*.java"
            }
            steps {
                echo "🚀 Déploiement incrémental sur Tomcat (seulement si du code a changé)..."
                script {
                    sh "mkdir -p ${TOMCAT_WEBAPPS}"
                    sh "rm -f ${TOMCAT_WEBAPPS}/ecommerce.war"
                    sh "cp target/*.war ${TOMCAT_WEBAPPS}/ecommerce.war"
                    sh 'sleep 25'
                }
            }
        }
    }

    post {
        always {
            echo "⏱️ Pipeline terminé — consulte les temps d’exécution des stages pour identifier les plus coûteux."
        }
        success {
            echo "✅ Déploiement terminé avec succès !"
            echo "🌍 Application disponible sur : http://localhost:8085/ecommerce/"
        }
        failure {
            echo "❌ Pipeline échoué ! Consulte les logs Jenkins pour les erreurs."
        }
    }
}
