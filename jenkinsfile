pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = "sarra24/foyerproject"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Récupération du code') {
            steps {
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
        
        stage('Construction Image Docker') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")
                }
            }
        }
        
        stage('Push Image Docker') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'dockercredentials') {
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push("latest")
                    }
                }
            }
        }
        
        stage('Déploiement') {
            steps {
                echo "Déploiement de l'image ${DOCKER_IMAGE}:${DOCKER_TAG}"
            }
        }
    }
    
    post {
        always {
            sh 'docker logout'
        }
    }
}