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
					sh 'curl -i --user 'demo:abc123' -X POST 'http://www.demo.com:8081/service/rest/beta/components?repository=maven-dev' -F maven2.groupId=org.owasp.webgoat  -F maven2.artifactId=webgoat-server -F maven2.version=8.0.0.M3  -F maven2.asset1=@webgoat-server/target/webgoat-server-8.0.0.M3.jar  -F maven2.asset1.classifier=sources  -F maven2.asset1.extension=jar'
				}
			}
		}
//
//		stage('Policy Evaluation'){
//			steps {
//				nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: 'HelloWorld1', iqStage: 'build', jobCredentialsId: ''
//			}
//		}
//		stage ('Deploy to Staging') {
//			steps {
//				build job: 'JenkinsClassDeployToStage'
//			}
//		}
	}
}
	
