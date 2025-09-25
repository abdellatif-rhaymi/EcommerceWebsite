pipeline {
    agent any
    environment {
        DB_URL = "jdbc:mysql://localhost:3306/ecommerce"
        DB_USER = "root"
        DB_PASS = "password"
        PATH = "/var/jenkins_home/maven/bin:$PATH"
        TOMCAT_URL = "http://admin:admin123@localhost:8091/manager/text"
        WAR_NAME = "ecommerce-website-1.0-SNAPSHOT.war"
        LOCAL_WEBAPPS = "/Users/abdellatif/tomcat_webapps" // dossier monté sur ton Mac
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
       			 sh 'cp target/ecommerce-website-1.0-SNAPSHOT.war /Users/abdellatif/tomcat_webapps/'
   
                // Copier le .war dans le dossier monté pour Docker
                // sh "cp target/${WAR_NAME} ${LOCAL_WEBAPPS}/"

                // Déployer via Tomcat Manager
                sh "curl --upload-file ${LOCAL_WEBAPPS}/${WAR_NAME} \"${TOMCAT_URL}/deploy?path=/ecommerce-website&update=true\""
            }
        }
    }
    post {
        success {
            echo 'Pipeline terminé avec succès et déploiement fait !'
        }
        failure {
            echo 'Pipeline échoué ! Vérifie les logs.'
        }
    }
}
