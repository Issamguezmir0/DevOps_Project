pipeline docker 
pipeline {
    agent any
    
    environment {
        GIT_HTTP_BUFFER_SIZE = '524288000'
        DOCKER_IMAGE = "sarra24/foyerproject"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Récupération du code') {
            steps {
                git branch: 'moduleBloc-Sarra', url: 'https://github.com/Issamguezmir0/DevOps_Project.git'
            }
        }
        
        stage('MVN clean') {
            steps {
                echo 'Running Maven clean...'
                sh 'mvn clean'
            }
        }
                stage('MVN package') {
            steps {
                echo 'Running Maven package...'
                sh 'mvn package -DskipTests'
            }
        }
        
        stage('MVN build') {
            steps {
                echo 'Running Maven install...'
                sh 'mvn install -DskipTests'
            }
        }

        
        stage('Construction') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }
        
        /*stage('Construction Image Docker') {
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
        }*/
        stage('Upload Artifacts to Nexus') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'nexuslogin', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                        echo 'Deploying artifacts to Nexus...'
                        sh '''
                            mvn deploy \
                            -DskipTests \
                            -DaltDeploymentRepository=nexuslogin::default::http://10.0.0.10:8081/repository/maven-snapshots/ \
                            -Dnexus.username="$NEXUS_USERNAME" \
                            -Dnexus.password="$NEXUS_PASSWORD"
                        '''
                    }
                }
            }
        }
    }
    
    post {
        always {
            sh 'docker logout'
        }
    }
}