pipeline {
    agent any

    environment {
        GIT_HTTP_BUFFER_SIZE = '524288000'
        DOCKER_IMAGE = "sarra24/foyerproject"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Getting code from GITHUB') {
            steps {
                echo 'Configuring Git buffer size...'
                sh 'git config --global http.postBuffer 524288000'
                echo 'Pulling code from GitHub...'
                git branch: 'moduleBloc-Sarra',
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
                withCredentials([usernamePassword(credentialsId: 'sonarqube-credentials', usernameVariable: 'SONAR_USER', passwordVariable: 'SONAR_PASS')]) {
                    echo 'Running SonarQube analysis...'
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_USER -Dsonar.password=$SONAR_PASS -Dsonar.host.url=http://10.0.0.10:9000'
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
                    docker.withRegistry('https://registry.hub.docker.com', 'dockercredentials') {
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push()
                        docker.image("${DOCKER_IMAGE}:${DOCKER_TAG}").push("latest")
                    }
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

        stage('Upload Artifacts to Nexus') {
            steps {
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

    post {
        always {
            sh 'docker logout'
        }
    }
}
