pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        stage('Récupération du code') {
            steps {
                // Checkout the specific branch in one step
                git branch: 'moduleUniversite-Seif', url: 'https://github.com/Issamguezmir0/DevOps_Project.git'
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
}