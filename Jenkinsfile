pipeline{
  agent {
    node {
      label "qx-pro-03-ovh"
    }
  }
  stages {
    stage("Build") {
      tools {
        jdk "JDK 15 ORACLE"
      }
      steps{
        script {
          withSonarQubeEnv('sonar-pro') {
            withMaven(
                // Maven installation declared in the Jenkins "Global Tool Configuration"
                maven: 'Maven 3',
            ) {
              // Run the maven build
              if (isUnix()) {
                sh "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true"
                sh "mvn org.jacoco:jacoco-maven-plugin:report"
              } else {
                bat "mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true"
                bat "mvn org.jacoco:jacoco-maven-plugin:report"
              }
            }
          }
        }
      }
    }
    stage("Dependency Check") {
      tools {
        jdk "JDK 15 ORACLE"
      }
      steps {
        script {
          withMaven(
            // Maven installation declared in the Jenkins "Global Tool Configuration"
            maven: 'Maven 3',
            ) {
              if (isUnix()) {
                bat "mvn org.owasp:dependency-check-maven:$DEPENDENCY_CHECK_VERSION:check -Dformat=XML " +
                    "-DdataDirectory=/opt/qalitax/jenkins-node/workspace/dependency-check-data/$DEPENDENCY_CHECK_VERSION " +
                    "-DassemblyAnalyzerEnabled=false " +
                    "-DfailOnError=false"
              } else {
                bat "mvn org.owasp:dependency-check-maven:$DEPENDENCY_CHECK_VERSION:check -Dformat=all " +
                    "-DdataDirectory=/opt/qalitax/jenkins-node/workspace/dependency-check-data/$DEPENDENCY_CHECK_VERSION  " +
                    "-DassemblyAnalyzerEnabled=false " +
                    "-DfailOnError=false"
              }
            }
        }
      }
    }
    stage("SonarQube Analysis") {
      tools {
        jdk "JDK 15 ORACLE"
      }
      steps {
        script {
          withSonarQubeEnv('sonar-pro') {
            withMaven(maven: 'Maven 3') {
              // Run sonarqube scanner for maven
              if(isUnix()){
                sh "mvn $SONAR_MAVEN_GOAL -Dsonar.host.url=$SONAR_HOST_URL " +
                    "-Dsonar.dependencyCheck.reportPath=./target/dependency-check-report.xml " +
                    "-Dsonar.dependencyCheck.htmlReportPath=./target/dependency-check-report.html"
              } else {
                bat "mvn $SONAR_MAVEN_GOAL -Dsonar.host.url=$SONAR_HOST_URL " +
                    "-Dsonar.dependencyCheck.reportPath=./target/dependency-check-report.xml " +
                    "-Dsonar.dependencyCheck.htmlReportPath=./target/dependency-check-report.html"
              }
            }
          }
        }
      }
    }
  }
  post {
    always{
      cleanWorkspace()
    }
  }
}