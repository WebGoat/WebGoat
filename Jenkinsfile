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
        script {
          echo 'Staring file system capture for change set'
          sh 'cov-build --desktop --dir idir --fs-capture-list ${CHANGE_SET} --no-command'
        }
      }
    }
  }
}
