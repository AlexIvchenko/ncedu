pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean install -Dlicense.skip=true'
        echo 'Build step'
      }
    }
  }
  environment {
    DB_HOST = 'hel.krvd.ru'
    DB_USER = 'ncedu'
    DB_PASSWORD = 'rpVA9xJcJEs'
  }
}