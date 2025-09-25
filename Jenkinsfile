pipeline {
    agent any
    environment {
        DB_URL = "jdbc:mysql://localhost:3306/ecommerce"
        DB_USER = "root"
        DB_PASS = "password"
        PATH = "/var/jenkins_home/maven/bin:$PATH"
        TOMCAT_WEBAPPS = "/var/jenkins_home/tomcat_webapps" // volume partagé avec Tomcat
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
                    // Vérifie que le dossier existe
                    sh "mkdir -p ${TOMCAT_WEBAPPS}"
                    // Copie le WAR généré dans le volume partagé
                    sh "cp target/*.war ${TOMCAT_WEBAPPS}/"
                }
            }
        }
    }
    post {
        success {
            echo "Pipeline terminé avec succès !"
        }
        failure {
            echo "Pipeline échoué ! Vérifie les logs."
        }
    }
}
