pipeline {
    agent any
    environment {
        DB_URL = "jdbc:mysql://localhost:3306/ecommerce"
        DB_USER = "root"
        DB_PASS = "password"
        PATH = "/var/jenkins_home/maven/bin:$PATH"
        TOMCAT_WEBAPPS = "/Users/abdellatif/tomcat_webapps"
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
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                script {
                    // Supprimer l'ancien dossier si existe
                    sh "rm -rf ${TOMCAT_WEBAPPS}/ecommerce-website"

                    // Copier le WAR dans le dossier webapps
                    sh "cp target/*.war ${TOMCAT_WEBAPPS}/ecommerce-website.war"

                    // Redémarrer le conteneur Tomcat
                    sh "docker restart mytomcat"
                }
            }
        }
    }
    post {
        failure {
            echo "Pipeline échoué ! Vérifie les logs."
        }
        success {
            echo "Pipeline terminé avec succès ! L'application devrait être disponible sur http://localhost:8091/ecommerce-website"
        }
    }
}
