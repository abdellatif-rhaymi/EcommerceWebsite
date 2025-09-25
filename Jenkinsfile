pipeline {
    agent any

    environment {
        TOMCAT_URL = "http://admin:admin123@localhost:8091/manager/text"
        WAR_NAME = "ecommerce-website.war"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/abdellatif-rhaymi/EcommerceWebsite.git'
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
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                // Copier le .war dans le dossier monté sur Mac
                sh "cp target/${WAR_NAME} ~/tomcat_webapps/"

                // Déployer via le Tomcat Manager
                sh "curl --upload-file ~/tomcat_webapps/${WAR_NAME} \"${TOMCAT_URL}/deploy?path=/ecommerce-website&update=true\""
            }
        }
    }

    post {
        success {
            echo 'Pipeline terminé avec succès !'
        }
        failure {
            echo 'Pipeline échoué ! Vérifie les logs.'
        }
    }
}
