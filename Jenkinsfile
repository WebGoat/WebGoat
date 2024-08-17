pipeline {
    agent any
    tools {
        jdk 'JDK 21'
        dockerTool 'Docker'
    }
    parameters {
      choice choices: ['Baseline', 'APIS', 'Full'],
              description: 'Type of scan that is going to perform inside the container',
              name: 'SCAN_TYPE'

      string defaultValue: 'http://webgoat:8082/WebGoat',
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
              docker network create -d bridge dast-network
              docker run -dt --name webgoat \
                -p 8082:8080 \
                -p 9092:9090 \
                --network=dast-network \
                --network-alias webgoat\
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
            sh 'docker pull zaproxy/zap-stable'
            sh '''
              docker run -dt --name owasp \
              --network=dast-network \
              zaproxy/zap-stable \
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
                    -J report.json \
                    -I
                """
                break

              case 'APIS':
                sh """
                  docker exec owasp \
                    zap-api-scan.py \
                    -t $target \
                    -J report.json \
                    -I
                """
                break

              case 'Full':
                sh """
                  docker exec owasp \
                    zap-full-scan.py \
                    -t $target \
                    //-J report.json
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
                docker cp owasp:/zap/wrk/report.json $WORKSPACE/report.json
            """
          }
        }
      }
      stage('Count severities') {
        steps {
          script {
            sh """
              # JSON-Datei einlesen
              file=$WORKSPACE/report.json

              # Zähle die Gesamtzahl der Schwachstellen
              total_vulnerabilities=\$(jq '[.site[].alerts[]] | length' "$file")

              # Gruppiere nach Schweregrad und zähle jede Gruppe
              critical_risks=\$(jq '[.site[].alerts[] | select(.riskdesc | startswith("Critical"))] | length' "$file")
              high_risks=\$(jq '[.site[].alerts[] | select(.riskdesc | startswith("High"))] | length' "$file")
              medium_risks=\$(jq '[.site[].alerts[] | select(.riskdesc | startswith("Medium"))] | length' "$file")
              low_risks=\$(jq '[.site[].alerts[] | select(.riskdesc | startswith("Low"))] | length' "$file")
              informational_risks=\$(jq '[.site[].alerts[] | select(.riskdesc | startswith("Informational"))] | length' "$file")

              # Ergebnisse ausgeben
              echo "Gesamtzahl der Schwachstellen: $total_vulnerabilities"
              echo "Kritisches Risiko: $critical_risks"
              echo "Hohes Risiko: $high_risks"
              echo "Mittleres Risiko: $medium_risks"
              echo "Niedriges Risiko: $low_risks"
              echo "Informationsschwachstellen: $informational_risks"
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
