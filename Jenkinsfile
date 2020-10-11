pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    mvn -B  install -DskipTests'''
      }
    }
    stage('Scan App - Build Container') {
      parallel {
        stage('IQ-BOM') {
          steps {
            nexusPolicyEvaluation(iqApplication: 'webgoat8', iqStage: 'build', iqScanPatterns: [[scanPattern: '']])
          }
        }
        stage('Static Analysis') {
          steps {
            echo '...run SonarQube or other SAST tools here'
          }
        }
        stage('Build Container') {
          steps {
            sh '''#!/bin/bash
cd webgoat-server
whoami
pwd
echo $PATH
docker build --no-cache -t webgoat/webgoat-8.0:latest .
docker tag webgoat/webgoat-8.0:latest registry.mycompany.com/webgoat/webgoat-8.0:latest
docker push registry.mycompany.com/webgoat/webgoat-8.0:latest
'''
          }
        }
      }
    }
    stage('Test Container') {
      parallel {
        stage('Anchore OS Scan') {
          steps {
            sh 'echo "nexus:5000/webgoat/webgoat-8.0 ${WORKSPACE}/webgoat-server/Dockerfile" > anchore_images'
            anchore 'anchore_images'
          }
        }
        stage('IQ-Scan Application') {
          steps {
            sh 'docker save webgoat/webgoat-8.0 -o $WORKSPACE/webgoat.tar'
            nexusPolicyEvaluation(iqStage: 'stage-release', iqApplication: 'webgoat8', iqScanPatterns: [[scanPattern: 'webgoat.tar']])
          }
        }
      }
    }
    stage('Publish Container') {
      when {
        branch 'master'
      }
      steps {
        sh '''
                    docker tag webgoat/webgoat-8.0 mycompany.com:5000/webgoat/webgoat-8.0:8.0
                    docker push mycompany.com:5000/webgoat/webgoat-8.0
                '''
      }
    }
  }
  tools {
    maven 'M3'
    jdk 'JDK8'
  }
}
