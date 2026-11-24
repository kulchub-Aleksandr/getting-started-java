pipeline {
  agent { label 'java-jdk21' }

  options {
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '20'))
    disableConcurrentBuilds()
    skipDefaultCheckout()
    timeout(time: 30, unit: 'MINUTES')
  }

  environment {
    ALLURE_RESULTS = 'build/allure-results'
    ALLURE_REPORT = 'build/allure-report'
    TEST_RESULTS = 'build/test-results/test'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
        sh 'git log -1 --pretty=format:"%h %s"'
      }
    }

    stage('Test') {
      steps {
        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
          withCredentials([string(credentialsId: 'selenoid-guest-webdriver-url', variable: 'REMOTE')]) {
            sh '''
              echo "Browser: remote Selenoid (URL hidden)"
              ./gradlew --no-daemon clean test -Dselenide.headless=true
            '''
          }
        }
      }
    }

    stage('Report') {
      steps {
        catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
          script {
            if (fileExists(env.ALLURE_RESULTS) && sh(script: "ls -A '${env.ALLURE_RESULTS}' 2>/dev/null | head -1", returnStatus: true) == 0) {
              allure(
                allureVersion: '3',
                includeProperties: false,
                results: [[path: env.ALLURE_RESULTS]],
                configPath: 'allurerc.mjs',
                report: env.ALLURE_REPORT,
                reportBuildPolicy: 'ALWAYS',
                resultPolicy: 'LEAVE_AS_IS'
              )
            } else {
              echo "ERROR: ${env.ALLURE_RESULTS} is empty, Allure report not generated"
            }
          }
        }
      }
    }

    stage('Publish') {
      steps {
        script {
          def hasJunit = sh(script: "ls ${env.TEST_RESULTS}/*.xml 2>/dev/null | head -1", returnStatus: true) == 0
          def hasReport = fileExists(env.ALLURE_REPORT + '/index.html')
          if (!hasJunit) {
            error("JUnit XML not found in ${env.TEST_RESULTS}")
          }
          if (!hasReport) {
            error("Allure report not found in ${env.ALLURE_REPORT}")
          }
          currentBuild.description = "Allure: ${env.BUILD_URL}allure/"
        }
      }
    }
  }

  post {
    always {
      script {
        try {
          archiveArtifacts artifacts: "${env.ALLURE_RESULTS}/**,${env.TEST_RESULTS}/**,build/reports/**,${env.ALLURE_REPORT}/**", allowEmptyArchive: true
        } catch (Exception e) {
          echo "WARNING: artifact archiving failed: ${e.class.simpleName}"
          currentBuild.result = 'FAILURE'
        }
      }
      script {
        if (sh(script: "ls ${env.TEST_RESULTS}/*.xml 2>/dev/null | head -1", returnStatus: true) == 0) {
          try {
            junit testResults: "${env.TEST_RESULTS}/*.xml", allowEmptyResults: true
          } catch (Exception e) {
            echo "WARNING: JUnit publishing failed: ${e.class.simpleName}"
            currentBuild.result = 'FAILURE'
          }
        } else {
          echo "WARNING: JUnit XML not found, nothing to publish"
        }
      }
      script {
        def status = currentBuild.currentResult == 'SUCCESS' ? 'success' : 'failure'
        notifyTelegram(status)
      }
    }
  }
}

def notifyTelegram(String status) {
  if (!fileExists('.github/scripts/notify-telegram.sh')) {
    echo 'WARNING: notify-telegram.sh not found, notification skipped'
    return
  }

  def commitName = ''
  try {
    commitName = sh(returnStdout: true, script: 'git log -1 --pretty=%s').trim()
  } catch (Exception ignored) {
    commitName = ''
  }

  def reportPublished = fileExists(env.ALLURE_REPORT + '/index.html') ? 'true' : 'false'
  def branch = (env.BRANCH_NAME ?: env.GIT_BRANCH ?: 'master').replaceFirst('^origin/', '')

  try {
    withCredentials([
      string(credentialsId: 'telegram-bot-token', variable: 'TELEGRAM_BOT_TOKEN'),
      string(credentialsId: 'telegram-chat-id', variable: 'TELEGRAM_CHAT_ID')
    ]) {
      withEnv([
        "STATUS=${status}",
        "BRANCH=${branch}",
        "COMMIT_SHA=${env.GIT_COMMIT ?: ''}",
        "COMMIT_NAME=${commitName}",
        "RUN_URL=${env.BUILD_URL}",
        "REPORT_URL=${env.BUILD_URL}allure/",
        "REPORT_PUBLISHED=${reportPublished}",
        "ARTIFACTS_URL=${env.BUILD_URL}artifact/"
      ]) {
        sh 'bash .github/scripts/notify-telegram.sh'
      }
    }
  } catch (Exception e) {
    echo "WARNING: Telegram notification skipped: ${e.class.simpleName}"
  }
}
