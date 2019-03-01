pipeline {
  agent {
    docker {
      image 'maven:latest'
      args '-v /opt/coverity/coverity_static_analysis:/opt/coverity/coverity_static_analysis --hostname covuser-vm --mac-address 08:00:27:60:53:AB'
    }

  }
  stages {
    stage('Initialize') {
      steps {
        sh '/opt/coverity/coverity_static_analysis/bin/cov-generate-hostid'
      }
    }
  }
}