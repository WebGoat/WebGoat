pipeline {
    agent any
    tools {
        jdk 'JDK 21'
    }

    stages {
        stage('SCM') {
            steps {
                checkout scm
            }
        }
        stage('SonarQube Analysis') {
            steps {
                script {
                    def mvn = tool 'Maven 3.9.8';
                    withSonarQubeEnv() {
                        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=test -Dmaven.test.skip"
                    }
                }
                waitForQualityGate abortPipeline: true
            }
        }
    }
}