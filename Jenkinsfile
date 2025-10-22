pipeline {
    agent any

    environment {
        // Variables de connexion MySQL (si tu veux les utiliser dans ton app)
        DB_URL = "jdbc:mysql://mysql:3306/ecommerce"
        DB_USER = "user"
        DB_PASS = "password"

        // Dossier partagé avec Tomcat (défini dans docker-compose.yml)
        TOMCAT_WEBAPPS = "/var/jenkins_home/tomcat_webapps"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/abdellatif-rhaymi/EcommerceWebsite.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test -Dtest=UtilisateurUnitTest'
            }
        }

        stage('Integration Tests') {
            steps {
                sh 'mvn test -DTEST_ENV=true -Dtest=SampleTest'
            }
        }
    
        stage('Coverage Report') {
            steps {
                echo "📈 Génération du rapport de couverture JaCoCo..."
                sh 'mvn clean test jacoco:report'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                scannerHome = tool 'sonar-scanner'
            }
            steps {
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
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                        -Dsonar.host.url=http://sonarqube:9000
                    """
                }
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                script {
                    sh "mkdir -p ${TOMCAT_WEBAPPS}"
                    // Supprime l’ancienne version si elle existe
                    sh "rm -f ${TOMCAT_WEBAPPS}/ecommerce.war"
                    // Copie et renomme le WAR
                    sh "cp target/*.war ${TOMCAT_WEBAPPS}/ecommerce.war"
                }
            }
        }
        
    }

    post {
        success {
            echo "✅ Déploiement terminé avec succès !"
            echo "🌍 Accéder à l’application : http://localhost:8085/ecommerce/"
        }
        failure {
            echo "❌ Pipeline échoué ! Vérifie les logs."
        }
    }
}
