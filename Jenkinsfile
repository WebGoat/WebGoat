pipeline {
    agent any
    tools {
        jdk 'JDK 21'
    }
    parameters {
      choice choices: ['Baseline', 'APIS', 'Full'],
              description: 'Type of scan that is going to perform inside the container',
              name: 'SCAN_TYPE'

      string defaultValue: 'http://localhost:8081/WebGoat',
              description: 'Target URL to scan',
              name: 'TARGET'

      booleanParam defaultValue: true,
                    description: 'Parameter to know if wanna generate report.',
                    name: 'GENERATE_REPORT'
    }
    stages {
      stage('SCM') {
        steps {
          checkout scm
        }
      }
      // stage ('Dependency Check') {
      //     steps {
      //         dependencyCheck additionalArguments: '', nvdCredentialsId: 'Dependency-Check API-Key', odcInstallation: 'Dependency Check', stopBuild: true
      //     }
      // }
      // stage('SonarQube Analysis') {
      //     steps {
      //         script {
      //             def mvn = tool 'Maven 3.9.8';
      //             withSonarQubeEnv() {
      //                 sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=test"
      //             }
      //         }
      //     }
      // }
      // ... BUILD IMAGE ...
      stage('Start application docker container') {
        steps {
          script {
            sh '''
              docker run -dt --name webgoat \
                -p 8082:8080 \
                -p 9092:9090 \
                webgoat/webgoat
            '''
          }
        }
      }
      // Angelehnt an:
      // https://medium.com/globant/owasp-zap-integration-with-jenkins-795d65991404
      stage('Setting up OWASP ZAP docker container') {
        steps {
          script {
            sh '''
              docker run -dt --name owasp \
              owasp/zap2docker-stable \
              /bin/bash
            '''
          }
        }
      }
      stage('Prepare wrk directory') {
        when {
          environment name: 'GENERATE_REPORT', value: 'true'
        }
        steps {
          script {
            sh '''
              docker exec owasp \
              mkdir /zap/wrk
            '''
          }
        }
      }
      stage('Scanning target on owasp container') {
        steps {
          script {
            scan_type = "${params.SCAN_TYPE}"
            echo "----> scan_type: $scan_type"
            target = "${params.TARGET}"
            switch (scan_type) {
              case 'Baseline':
                sh """
                  docker exec owasp \
                  zap-baseline.py \
                  -t $target \
                  -x report.xml \
                  -I
                """
                break

              case 'APIS':
                sh """
                  docker exec owasp \
                  zap-api-scan.py \
                  -t $target \
                  -x report.xml \
                  -I
                """
                break

              case 'Full':
                sh """
                  docker exec owasp \
                  zap-full-scan.py \
                  -t $target \
                  //-x report.xml
                  -I
                """
                break

              default:
                error 'Something went wrong...'
            }
          }
        }
      }
      stage('Copy Report to Workspace') {
        steps {
          script {
            sh """
                docker cp owasp:/zap/wrk/report.xml ${WORKSPACE}/report.xml
            """
          }
        }
      }
    }
    post {
        always {
          echo 'Removing container'
          sh '''
                docker stop owasp
                docker rm owasp
            '''
        }
    }
}
