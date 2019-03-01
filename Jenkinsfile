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
        synopsys_coverity buildCommand: 'mvn clean compile', buildStatusForIssues: 'FAILURE', changeSetExclusionPatterns: '', changeSetInclusionPatterns: '', checkForIssuesInView: true, commands: [[command: '']], configureChangeSetPatterns: false, coverityAnalysisType: 'COV_RUN_DESKTOP', coverityRunConfiguration: 'SIMPLE', coverityToolName: 'Docker-Container', onCommandFailure: 'SKIP_REMAINING_COMMANDS', projectName: 'WebGoat', streamName: 'webgoat8', viewName: 'OWASP Top 10'
      }
    }
  }
}
