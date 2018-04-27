pipeline {
	agent any
	stages {
		stage('Build'){
			steps {
				sh '/apps/mvn/bin/mvn clean package -DskipTests'
			}
 			post {
				success {
					echo 'Now Archiving to Dev repo...'
//					archiveArtifacts artifacts: '**/target/*.war'
					sh "curl -i --user 'demo:abc123' -X POST 'http://www.demo.com:8081/service/rest/beta/components?repository=maven-dev' -F maven2.groupId=org.owasp.webgoat  -F maven2.artifactId=webgoat-server -F maven2.version=8.0.0.M3  -F maven2.asset1=@webgoat-server/target/webgoat-server-8.0.0.M3.jar  -F maven2.asset1.classifier=sources  -F maven2.asset1.extension=jar"
//					tag
//					found the system needed a pause
					sleep(10)
					sh "curl -i --user 'demo:abc123' -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://www.demo.com:8081/service/rest/beta/tags/associate/jerry-1?repository=maven-dev&group=org.owasp.webgoat&name=webgoat-server&version=8.0.0.M3'" 
				}
			}
		}
//
		stage('Policy Evaluation for Dev'){
			steps {
				nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: 'Webgoat', iqScanPatterns: [[scanPattern: '**/webgoat-server-8.0.0.M3.jar']], iqStage: 'build', jobCredentialsId: ''
			}
		}
		stage ('Promoting webgoat-server to QA repo') {
			steps {
				sh "curl -i --user 'demo:abc123' -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' 'http://www.demo.com:8081/service/rest/beta/staging/move/maven-qa?repository=maven-dev&tag=jerry-1'"
			}
		}
//		stage('Policy Evaluation for Staging'){
//			steps {
//				nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: 'Webgoat', iqStage: 'stage-release', jobCredentialsId: ''
//			}
//		}
	}
}
	
