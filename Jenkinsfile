pipeline {
    agent any

    tools {
        maven 'Maven'  // doit correspondre à l’installation Maven dans Jenkins
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
            step([$class: 'DeployPublisher',
                   war: '**/target/*.war',
                   contextPath: 'app',
                   onFailure: false,
                   adapters: [
                       [$class: 'Tomcat9xAdapter',
                        credentialsId: 'tomcat-credentials',
                        url: 'http://tomcat:8085']
                   ]
            ])
        }
    }
}
