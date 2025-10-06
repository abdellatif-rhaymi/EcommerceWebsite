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
        


        stage('Test JUnit DAO') {
            steps {
                echo '🧪 Exécution des tests JUnit sur UtilisateurDao...'
                sh '''
                    mkdir -p test-classes
                    javac -cp "./tomcat_webapps/ecommerce/WEB-INF/lib/*:./classes" -d test-classes $(find ./src/test/java -name "*.java")
                    java -jar ./tomcat_webapps/ecommerce/WEB-INF/lib/junit-platform-console-standalone-1.10.2.jar \
                        --class-path ./classes:./test-classes:./tomcat_webapps/ecommerce/WEB-INF/lib/mysql-connector-j-9.1.0.jar \
                        --scan-classpath
                '''
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                script {
                    sh "mkdir -p ${TOMCAT_WEBAPPS}"
                    // Supprime l’ancienne version si elle existe
                    sh "rm -f ${TOMCAT_WEBAPPS}/ecommerce.war"
                    // Copie et renomme le WAR
                    sh "cp target/*.war ${TOMCAT_WEBAPPS}/ecommerce.war"
                }
            }
        }
        
    }

    post {
        success {
            echo "✅ Déploiement terminé avec succès !"
            echo "🌍 Accéder à l’application : http://localhost:8085/ecommerce/"
        }
        failure {
            echo "❌ Pipeline échoué ! Vérifie les logs."
        }
    }
}
