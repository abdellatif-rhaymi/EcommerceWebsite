pipeline {
    agent any

    environment {
        DB_URL = "jdbc:mysql://mysql:3306/ecommerce"
        DB_USER = "root"
        DB_PASS = "password"
        PATH = "/usr/share/maven/bin:$PATH"
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
                    // Déploie le WAR directement dans Tomcat via Manager App
                    sh '''
                    WAR_FILE=$(ls target/*.war | head -n 1)
                    curl --fail -u admin:admin123 \
                        --upload-file $WAR_FILE \
                        "http://tomcat:8080/manager/text/deploy?path=/ecommerce-website&update=true"
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline terminé avec succès et application déployée sur Tomcat !"
        }
        failure {
            echo "❌ Pipeline échoué, vérifie les logs."
        }
    }
}
