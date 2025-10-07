pipeline {
    agent any

    environment {
        DB_URL = "jdbc:mysql://mysql:3306/ecommerce"
        DB_USER = "root"
        DB_PASS = "root"
        TOMCAT_WEBAPPS = "/var/jenkins_home/tomcat_webapps"
        MAVEN_OPTS = "-Dmaven.repo.local=.m2/repository"  // 👉 Cache Maven local
        SONAR_TOKEN = credentials('sonar-token') // Token stocké dans Jenkins credentials

    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/abdellatif-rhaymi/EcommerceWebsite.git'
            }
        }

        stage('Build') {
            steps {
                echo "🏗️ Compilation..."
                sh 'mvn clean package -DskipTests'
            }
        }


        stage('Tests and analyse') {
            parallel {
                stage('Unit Tests') {
                    steps {
                        echo "⚡ Lancement des tests unitaires (rapide)..."
                        sh 'mvn test -Dtest=UtilisateurUnitTest'
                    }
                }

                stage('Integration Tests (H2)') {
                    steps {
                        echo "🧪 Tests d’intégration sur base H2 en parallèle..."
                        sh 'mvn test -DTEST_ENV=true -Dtest=SampleTest'
                    }
                }
                stage('SonarQube Analysis') {
                    steps {
                        withSonarQubeEnv('SonarQube') { // Nom du serveur configuré dans Jenkins
                            sh "mvn sonar:sonar \
                                -Dsonar.projectKey=EcommerceWebsite \
                                -Dsonar.host.url=http://localhost:9000 \
                                -Dsonar.login=${SONAR_TOKEN}"
                        }
                    }
                 }
            }
        }
        

        stage('Conditional Deploy') {
            when {
                changeset "**/src/main/**"  // 👉 Ne déploie que si le code source (pas les tests) a changé
            }
            steps {
                script {
                    echo "🚀 Déploiement sur Tomcat uniquement si le code a changé..."
                    sh "mkdir -p ${TOMCAT_WEBAPPS}"
                    sh "rm -f ${TOMCAT_WEBAPPS}/ecommerce.war"
                    sh "cp target/*.war ${TOMCAT_WEBAPPS}/ecommerce.war"
                }
            }
        }

        stage('Publish Test Report') {
            steps {
                echo "📊 Publication du rapport JUnit..."
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }

    post {
        success {
            echo "✅ Build et déploiement terminés avec succès !"
            echo "🌍 Application disponible sur : http://localhost:8085/ecommerce/"
        }
        failure {
            echo "❌ Pipeline échoué. Vérifie les logs Jenkins."
        }
    }
}
