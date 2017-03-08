import groovy.json.JsonOutput

def runOsSafe(GString command) {
  if (isUnix()) {
    sh command
  } else {
    bat command
  }
}

node {
  def javaHome
  def mvnHome
  def commitId
  def gitHubApiToken

  stage('Preparation') {
    checkout scm
    sh 'git rev-parse HEAD > .git/commit-id'
    commitId = readFile('.git/commit-id')

    javaHome = tool 'Java 7'
    mvnHome = tool 'M3'
    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'integrations-github-api',
                      usernameVariable: 'GITHUB_API_USERNAME', passwordVariable: 'GITHUB_API_PASSWORD']]) {
      gitHubApiToken = env.GITHUB_API_PASSWORD
    }
  }
  stage('Build') {
    runOsSafe "JAVA_HOME=${javaHome} ${mvnHome}/bin/mvn clean package"
  }
  stage('Nexus Lifecycle Analysis') {
    def analysisPayload = JsonOutput.toJson(
      state: 'pending',
      context: 'analysis',
      description: 'Nexus Lifecycle Analysis in running'
    )
    sh "curl -H \"Authorization: token ${githubApiToken}\" --request POST --data '${analysisPayload}' https://api.github.com/repos/whyjustin/WebGoat/statuses/${commitId} > /dev/null"

    def evaluation = nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: 'webgoat', iqStage: 'build', jobCredentialsId: ''

    analysisPayload = JsonOutput.toJson(
            state: 'failure',
            context: 'analysis',
            description: 'Nexus Lifecycle Analysis failed',
            target_url: "${evaluation.applicationCompositionReportUrl}"
    )
    sh "curl -H \"Authorization: token ${githubApiToken}\" --request POST --data '${analysisPayload}' https://api.github.com/repos/whyjustin/WebGoat/statuses/${commitId} > /dev/null"
  }
  if (currentBuild.result == 'FAILURE') {
    return
  }
  stage('Results') {
    junit '**/target/surefire-reports/TEST-*.xml'
    archive 'target/*.jar'
  }
}
