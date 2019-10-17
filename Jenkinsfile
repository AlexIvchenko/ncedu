pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean package -DskipTests'
        echo 'Build step'
      }
    }
    stage('Tests') {
      steps {
        sh 'mvn test'
      }
    }
  }
  environment {
    DB_HOST = 'hel.krvd.ru'
    DB_USERNAME = 'ncedu'
    DB_PASSWORD = 'rpVA9xJcJEs'
  }
}