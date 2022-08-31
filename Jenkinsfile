pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                nexusPolicyEvaluation iqApplication: manualApplication('WebGoat__ealagorm'), iqInstanceId: 'Nexus_IQ_140_Local', iqStage: 'release', iqScanPatterns: [[scanPattern: '**/*.jar'], [scanPattern: '**/*pom.xml'], [scanPattern: '**/*.js'], [scanPattern: '**/*.zip'], [scanPattern: '**/*.war'], [scanPattern: '**/*.ear']]
            }
        }

    }
}
