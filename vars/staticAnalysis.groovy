def call(boolean abort = false) {
  def branch = env.BRANCH_NAME?.toLowerCase()
  def shouldCheckGate = abort || branch == 'master' || branch?.startsWith('hotfix')

  if (shouldCheckGate) {
    echo "SonarQube analysis will run because branch is '${branch}' or abort is true"

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
      echo "Evaluating Quality Gate for branch: ${branch} (abort flag: ${abort})"
      echo "Quality Gate Status: ${qg.status}"

      if (abort && qg.status != 'OK') {
        error "Quality Gate failed. Pipeline aborted as abort=true was explicitly set."
      } else if ((branch == 'master' || branch?.startsWith('hotfix')) && qg.status != 'OK') {
        error "Quality Gate failed on '${branch}'. Pipeline aborted for critical branch (verified/master or urgent/hotfix)."
      } else {
        echo "Quality Gate passed or non-critical branch."
      }
    }
  } else {
    echo "Skipping SonarQube analysis and Quality Gate — branch '${branch}' is non-critical and abort=false."
  }
}
