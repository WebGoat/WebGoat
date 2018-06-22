pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '/apps/mvn/bin/mvn clean deploy -pl webgoat-server  -DskipTests'
      }
      post {
        success {
          echo 'Taging build in NXRM...'
          sleep 1
          sh 'curl -i --user \'demo:abc123\' -X POST --header \'Content-Type: application/json\' --header \'Accept: application/json\' \'http://www.demo.com:8081/service/rest/beta/tags/associate/jerry-1?repository=maven-dev&group=org.owasp.webgoat&name=webgoat-server&version=8.0.0.M3\''

        }

      }
    }
    stage('Promote to QA?') {
      parallel {
        stage('Promote to QA?') {
          steps {
            timeout(time: 5, unit: 'DAYS') {
              input 'Move to QA?'
            }

          }
        }
        stage('Policy Evaluation') {
          steps {
            nexusPolicyEvaluation(iqStage: 'build', iqApplication: 'Webgoat')
          }
        }
      }
    }
    stage('Move to QA') {
      steps {
        sh 'curl -i --user \'demo:abc123\' -X POST --header \'Content-Type: application/json\' --header \'Accept: application/json\' \'http://www.demo.com:8081/service/rest/beta/staging/move/maven-qa?repository=maven-dev&tag=jerry-1\''
      }
    }
  }
}