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

          stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                script {
                    // On vérifie que le volume existe
                    sh "mkdir -p ${TOMCAT_WEBAPPS}"
                    // Copie du fichier WAR généré
                    sh "cp target/*.war ${TOMCAT_WEBAPPS}/"
                }
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline terminé avec succès, WAR déployé dans Tomcat !"
        }
        failure {
            echo "❌ Pipeline échoué ! Vérifie les logs."
        }
    }
}
