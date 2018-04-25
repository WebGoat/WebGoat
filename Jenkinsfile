pipeline {
	agent any
	stages {
		stage('Build'){
			steps {
				sh '/apps/mvn/bin/mvn clean package -DskipTests'
			}
// 			post {
//				success {
//					echo 'Now Archiving...'
//					archiveArtifacts artifacts: '**/target/*.war'
//				}
//			}
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
	
