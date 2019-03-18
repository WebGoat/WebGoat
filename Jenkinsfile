pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
//        sh '/apps/mvn/bin/mvn clean deploy -pl webgoat-server  -DskipTests'
          sh '/app/mvn_3_5_3/bin/mvn clean deploy  -DskipTests'
      }
      post {
        success {
          echo 'Taging build in NXRM...'
          sleep 1
//          sh "curl -i --user 'admin:abc123' -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://www.demo.com:8081/service/rest/v1/tags/associate/jerry-1?repository=maven-dev&group=org.owasp.webgoat&name=webgoat-server&version=8.0.0.M3'"
          sh "curl -i --user 'admin:abc123' -X POST 'http://www.demo.com:8081/service/rest/v1/tags/associate/jerry-1?repository=maven-dev&group=org.owasp.webgoat&name=webgoat-server&version=8.0.0.M3'"
        }

      }
    }
    stage('Scan and Promote') {
      parallel {
        stage('Approve for QA') {
          steps {
            timeout(time: 5, unit: 'DAYS') {
              input 'Move to QA?'
            }

            sh "curl -i --user 'admin:abc123' -X POST  'http://www.demo.com:8081/service/rest/v1/staging/move/maven-qa?repository=maven-dev&tag=jerry-1'"
          }
        }
        stage('Policy Evaluation') {
          steps {
            nexusPolicyEvaluation(iqStage: 'build', iqApplication: 'WebGoatServer')
          }
        }
      }
    }
  }
}