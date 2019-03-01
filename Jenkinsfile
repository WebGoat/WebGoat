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
        git(url: 'https://github.com/mprashant24/WebGoat', branch: 'master', credentialsId: 'Git', changelog: true, poll: true)
      }
    }
  }
}