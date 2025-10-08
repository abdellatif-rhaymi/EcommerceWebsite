pipeline {
    agent any

    environment {
        // Variables utilisées pendant le déploiement (MySQL réel)
        DB_URL = "jdbc:mysql://mysql:3306/ecommerce"
        DB_USER = "root"
        DB_PASS = "root"

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
                echo "🏗️ Compilation avec cache Maven..."
                cache(maxCacheSize: 2, caches: [[$class: 'MavenCache']]) {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test -Dtest=UtilisateurUnitTest'
            }
        }
        stage('Unit Tests (H2)') {
            steps {
                echo "🧪 Exécution des tests JUnit avec base H2 en mémoire..."
                sh '''
                    # On active le mode test (H2) via une variable système
                    mvn test -DTEST_ENV=true -Dtest=SampleTest
                '''
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


        stage('Deploy to Tomcat') {
            steps {
                script {
                    echo "🚀 Déploiement sur Tomcat..."
                    sh "mkdir -p ${TOMCAT_WEBAPPS}"
                    // Supprimer ancienne version
                    sh "rm -f ${TOMCAT_WEBAPPS}/ecommerce.war"
                    // Copier le WAR compilé
                    sh "cp target/*.war ${TOMCAT_WEBAPPS}/ecommerce.war"
                    sh 'sleep 25'
                }
            }
        }
    }

    post {
        success {
            echo "✅ Déploiement terminé avec succès !"
            echo "🌍 Accède à l’application : http://localhost:8085/ecommerce/"
        }
        failure {
            echo "❌ Pipeline échoué ! Consulte les logs Jenkins pour plus de détails."
        }
    }
}
