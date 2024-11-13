pipeline {
    agent any
    environment {
        DOCKER_CREDENTIALS_ID = 'dockercredentials'
        DOCKER_IMAGE_NAME = 'seifriahi/foyerproject'
        DOCKER_TAG = 'latest'
    }
    stages {
        stage('Getting code from GITHUB') {
            steps {
                echo 'Pulling code from GitHub...'
                git branch: 'moduleUniversite-Seif',  // Correction de la branche ici
                url: 'https://github.com/Issamguezmir0/DevOps_Project.git'
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
       stage('Check Project Files After Checkout') {
             steps {
                  echo 'Listing project files after Git checkout...'
                  sh 'pwd'
                  sh 'ls -l'
             }
       }

       stage('Build without Tests') {
            steps {
                 echo 'Building project without tests...'
                 sh 'mvn clean package -DskipTests'
            }
       }

       stage('Check JAR File') {
            steps {
                 echo 'Checking if JAR file exists...'
                 sh 'ls -l target/'
            }
       }

       stage('Build Docker Image') {
             steps {
                  echo 'Building Docker image...'
                  sh 'docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} .'
             }
       }

       stage('Login to Docker Hub') {
            steps {
                 script {
                     withCredentials([usernamePassword(credentialsId: DOCKER_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                          sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
                     }
                 }
            }
       }
       stage('Push to Docker Hub') {
             steps {
                 script {
                        // Tag and push the Docker image
                        sh 'docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}'
                        sh 'docker push ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}'
                 }
             }
       }
       stage('Deploy with Docker Compose') {
              steps {
                       echo 'Starting Docker Compose...'
                       sh '''
                           docker compose down || true
                           docker compose up -d
                       '''
              }
       }
       stage('MVN SONARQUBE') {
                steps {
                    withCredentials([usernamePassword(credentialsId: 'sonarqube-credentials', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASS')]) {
                        echo 'Running SonarQube analysis...'
                        sh '''
                            mvn sonar:sonar \
                                -Dsonar.login=$SONAR_USER \
                                -Dsonar.password=$SONAR_PASS \
                                -Dsonar.host.url=http://10.0.0.10:9000
                        '''
                    }
                }
       }



   }
    post {
        always {
            echo 'Cleaning up...'
            sh 'docker rmi ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} || true'
            sh 'docker logout'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs.'
        }
    }
}