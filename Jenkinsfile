pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                nexusPolicyEvaluation
                    enableDebugLogging: false, 
                    failBuildOnNetworkError: false, 
                    iqApplication: selectedApplication('WebGoat__ealagorm'), 
                    iqInstanceId: 'Nexus_IQ_140_Local'
            }
        }

    }
}
