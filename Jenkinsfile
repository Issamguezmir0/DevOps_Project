pipeline {
    agent any
    
    environment {
        GIT_HTTP_BUFFER_SIZE = '524288000'
        DOCKER_IMAGE = "daly77/foyerproject"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Getting code from GITHUB') {
            steps {
                echo 'Pulling code from GitHub...'
                git branch: 'ModuleFoyer-Mohammed.Ali', url: 'https://github.com/Issamguezmir0/DevOps_Project.git'
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
        
        stage('MVN compile') {
            steps {
                echo 'Running Maven compile...'
                sh 'mvn compile'
            }
        }
        
        stage('Run Unit Tests') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
        }
        
        stage('MVN SONARQUBE') { 
            steps {
                withCredentials([usernamePassword(credentialsId: 'sonar-credentials', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASS')]) { 
                    echo 'Running SonarQube analysis...' 
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_USER -Dsonar.password=$SONAR_PASS -Dsonar.host.url=http://192.168.117.128:9000' 
                } 
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
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-credentials') {
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

        stage('Upload Artifacts to Nexus') {
            steps {
                script {
                     withCredentials([usernamePassword(credentialsId: 'nexusid', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                     echo 'Deploying artifacts to Nexus...'
                    sh '''
                    mvn deploy \
                    -DskipTests \
                    -DaltDeploymentRepository=nexusid::default::http://192.168.117.128:8081/repository/maven-snapshots/ \
                    -Dnexus.username=$NEXUS_USERNAME \
                    -Dnexus.password=$NEXUS_PASSWORD
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
