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

    echo "Evaluating Quality Gate status for branch: ${branch}"
    echo "Status: ${qg.status}, Abort flag: ${abort}"

    if (abort) {
      if (qg.status != 'OK') {
        error "Quality Gate failed. Pipeline aborted because abort=true was explicitly set."
      }
    } else {
      if (branch == 'master' || branch?.startsWith('hotfix')) {
        if (qg.status != 'OK') {
          error "Quality Gate failed on '${branch}'. Pipeline aborted because it is a critical branch. 'master' implies changes were previously approved, and 'hotfix' requires immediate and safe deployment."
        }
      } else {
        echo "Quality Gate failed but branch '${branch}' is considered non-critical. Proceeding with the pipeline."
      }
    }
  }
}
