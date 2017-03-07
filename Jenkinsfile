def runOsSafe(GString command) {
  if (isUnix()) {
    sh command
  } else {
    bat command
  }
}

node {
  def mvnHome

  stage('Preparation') {
    checkout scm
    mvnHome = tool 'M3'
  }
  stage('Build') {
    runOsSafe "'${mvnHome}/bin/mvn' clean package"
  }
  stage('Nexus Lifecycle Analysis') {
    nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: 'webgoat', iqStage: 'build', jobCredentialsId: ''
  }
  if (currentBuild.result == 'FAILURE') {
    return
  }
  stage('Results') {
    junit '**/target/surefire-reports/TEST-*.xml'
    archive 'target/*.jar'
  }
}
