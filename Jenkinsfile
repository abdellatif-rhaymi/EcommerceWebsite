pipeline {
    agent any

    environment {
        DB_URL = "jdbc:mysql://mysql:3306/ecommerce"
        DB_USER = "root"
        DB_PASS = "root"
        TOMCAT_WEBAPPS = "/var/jenkins_home/tomcat_webapps"
        MAVEN_OPTS = "-Dmaven.repo.local=/root/.m2/repository"

        DOCKER_HUB_USER = "abdellatifrhaymi"     // Ton pseudo Docker Hub
        APP_NAME = "ecommerce-app"
        IMAGE_NAME = "${DOCKER_HUB_USER}/${APP_NAME}:latest"
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
                        sh 'mvn clean package -DskipTests'
                    }
                }
            }
        }
        // 🔥 NOUVEAUX STAGES CI-DESSOUS 🔥
        stage('Build Docker Image') {
            steps {
                echo "🐳 Construction de l’image Docker..."
                sh "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                echo "📦 Envoi de l’image sur Docker Hub..."
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${IMAGE_NAME}
                    """
                }
            }
        }
        stage('Deploy from Docker Hub') {
        steps {
            echo "🚀 Déploiement depuis Docker Hub..."
            sh '''
                docker pull abdellatifrhaymi/ecommerce-app:latest
                docker stop tomcat-server || true
                docker rm tomcat-server || true
                docker run -d --name tomcat-server -p 8085:8079 abdellatifrhaymi/ecommerce-app:latest
            '''
        }
    }

    }

    post {
        success {
            echo "✅ Pipeline terminée avec succès !"
            echo "🌍 Accède à l’application : http://localhost:8085/ecommerce/"
            echo "🐳 Image Docker poussée sur Docker Hub : ${IMAGE_NAME}"
        }
        failure {
            echo "❌ Pipeline échouée ! Consulte les logs Jenkins pour les erreurs."
        }
    }
}
