pipeline {
    agent any

    environment {
        VERSION = "${env.BUILD_NUMBER}"
    }

    tools {
        maven 'Maven 3'      // Ensure Maven is configured in Jenkins
        jdk 'OpenJDK 17'     // Ensure JDK is configured in Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                git 'git@github.com:mahi0x00/WebGoat-mahi.git'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def tag = "webgoat:${env.VERSION}"
                    sh "docker build -t ${tag} ."
                }
            }
        }
    }
}
