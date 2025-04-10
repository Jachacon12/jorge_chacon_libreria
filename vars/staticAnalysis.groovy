def call(Boolean abort = false) {
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

  timeout(time: 5, unit: 'MINUTES') {
    def qg = waitForQualityGate()
    def branch = env.BRANCH_NAME?.toLowerCase()

    if (abort) {
      if (qg.status != 'OK') {
        error "Quality Gate failed. Pipeline aborted due to abort=true."
      }
    } else {
      if (branch == 'master' || branch?.startsWith('hotfix')) {
        if (qg.status != 'OK') {
          error "Quality Gate failed on critical branch '${branch}'. Pipeline aborted."
        }
      } else {
        echo "Quality Gate failed but branch '${branch}' is non-critical. Continuing."
      }
    }
  }
}
