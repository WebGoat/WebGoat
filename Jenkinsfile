pipeline {
    agent {
        docker {
            image 'maven:3.9.0-eclipse-temurin-17' 
            args '-v /root/.m2:/root/.m2' 
        }
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -DskipTests clean install' 
            }
        }
        stage ('OWASP Dependency-Check & SonarQube') {  
            steps {    
  				sh 'mvn org.owasp:dependency-check-maven:check'
				withSonarQubeEnv('SonarQube')  {
                    sh 'mvn -DskipTests clean package sonar:sonar -Dsonar.dependencyCheck.jsonReportPath=target/dependency-check-report.json -Dsonar.dependencyCheck.xmlReportPath=target/dependency-check-report.xml -Dsonar.dependencyCheck.htmlReportPath=target/dependency-check-report.html'
                }
            }  
        }        
    }
}
