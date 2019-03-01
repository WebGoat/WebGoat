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
        sh label: 'cov-build', script: 'cov-build --desktop --dir idir --fs-capture-list ${CHANGE_SET} --no-command'
      }
    }
  }
}
