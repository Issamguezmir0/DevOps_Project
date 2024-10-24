pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        stage('Récupération du code') {
            steps {
                git 'https://github.com/Issamguezmir0/DevOps_Project.git'
                git branch: 'moduleBloc-Sarra', url: 'https://github.com/Issamguezmir0/DevOps_Project.git'
            }
        }

        stage('Nettoyage') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Construction') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
    }