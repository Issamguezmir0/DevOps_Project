pipeline {
    agent any    
    stages {
        stage('Getting code from GITHUB') {
            steps {
                echo 'Pulling code from GitHub...'
                git branch: 'moduleBloc-Sarra',  // Correction de la branche ici
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

    }
}