def call() {
    withSonarQubeEnv('Sonar local') {
        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
            sh '''
                sonar-scanner \
                  -Dsonar.projectKey=devops-sonar \
                  -Dsonar.sources=. \
                  -Dsonar.token=$SONAR_TOKEN
            '''
        }
    }
}
