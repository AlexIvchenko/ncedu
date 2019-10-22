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
    stage('Deploy') {
      steps {
        sh '''if [ -f /home/spring/app.jar ]; then sudo systemctl stop ncedu; fi
cp target/*.jar /home/spring/app.jar
sudo systemctl start ncedu'''
      }
    }
  }
  environment {
    DB_HOST = 'hel.krvd.ru'
    DB_USERNAME = 'ncedu'
    DB_PASSWORD = 'rpVA9xJcJEs'
    DB_DATABASE = 'ncedu'
  }
}