pipeline {
  agent {
    docker {
      image 'maven:latest'
    }

  }
  stages {
    stage('Initialize') {
      steps {
        sh 'hostname'
      }
    }
  }
}