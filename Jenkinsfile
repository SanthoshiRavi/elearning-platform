pipeline {
    agent any
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-21-amazon-corretto.x86_64'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        DOCKERHUB_USERNAME = credentials('dockerhub-username')
        DOCKERHUB_TOKEN = credentials('dockerhub-token')
        BACKEND_IMAGE = "${DOCKERHUB_USERNAME}/elearning-backend"
        FRONTEND_IMAGE = "${DOCKERHUB_USERNAME}/elearning-frontend"
        IMAGE_TAG = "${BUILD_NUMBER}"
        BACKEND_HOST = "10.0.1.130"
        FRONTEND_HOST = "10.0.1.69"
        MONITORING_HOST = "10.0.1.216"
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }
        stage('Docker Build') {
            steps {
                sh """
                docker build -t ${BACKEND_IMAGE}:${IMAGE_TAG} ./backend
                docker build -t ${BACKEND_IMAGE}:latest ./backend

                docker build -t ${FRONTEND_IMAGE}:${IMAGE_TAG} ./frontend
                docker build -t ${FRONTEND_IMAGE}:latest ./frontend
                """
            }
        }
        stage('Docker Login') {
            steps {
                sh 'echo ${DOCKERHUB_TOKEN} | docker login -u ${DOCKERHUB_USERNAME} --password-stdin'
            }
        }
        stage('Push Images') {
            steps {
                sh """
                docker push ${BACKEND_IMAGE}:${IMAGE_TAG}
                docker push ${BACKEND_IMAGE}:latest

                docker push ${FRONTEND_IMAGE}:${IMAGE_TAG}
                docker push ${FRONTEND_IMAGE}:latest
                """
            }
        }
        stage('Deploy Backend') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    sh """
                    scp -o StrictHostKeyChecking=no deployment/backend/docker-compose.backend.yml \
                        ec2-user@${BACKEND_HOST}:~/docker-compose.backend.yml

                    ssh -o StrictHostKeyChecking=no ec2-user@${BACKEND_HOST} '
                        docker compose -p elearning-backend \
                            -f docker-compose.backend.yml pull

                        docker compose -p elearning-backend \
                            -f docker-compose.backend.yml up -d
                    '
                    """
                }
            }
        }
        stage('Deploy Monitoring') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \
                        ec2-user@${MONITORING_HOST} \
                        'mkdir -p ~/monitoring'

                    scp \
                        -o StrictHostKeyChecking=no \
                        deployment/monitoring/docker-compose.monitoring.yml \
                        ec2-user@${MONITORING_HOST}:~/monitoring/

                    scp \
                        -o StrictHostKeyChecking=no \
                        deployment/monitoring/prometheus.yml \
                        ec2-user@${MONITORING_HOST}:~/monitoring/

                    ssh -o StrictHostKeyChecking=no ec2-user@${MONITORING_HOST} '
                        cd ~/monitoring

                        docker compose \
                            -f docker-compose.monitoring.yml pull

                        docker compose \
                            -f docker-compose.monitoring.yml up -d
                    '
                    """
                }
            }
        }
        stage('Deploy Monitoring') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no \
                        ec2-user@${MONITORING_HOST} \
                        'mkdir -p ~/monitoring'

                    scp \
                        -o StrictHostKeyChecking=no \
                        deployment/monitoring/docker-compose.monitoring.yml \
                        ec2-user@${MONITORING_HOST}:~/monitoring/

                    scp \
                        -o StrictHostKeyChecking=no \
                        deployment/monitoring/prometheus.yml \
                        ec2-user@${MONITORING_HOST}:~/monitoring/

                    ssh -o StrictHostKeyChecking=no ec2-user@${MONITORING_HOST} '
                        cd ~/monitoring

                        docker compose \
                            -f docker-compose.monitoring.yml pull

                        docker compose \
                            -f docker-compose.monitoring.yml up -d
                    '
                    """
                }
            }
        }
        stage('Health Check') {
            steps {
                sh """
                echo "Checking Backend..."
                curl --retry 20 --retry-delay 5 http://${BACKEND_HOST}:8081/actuator/health

                echo "Checking Prometheus..."
                curl --retry 20 --retry-delay 5 http://${MONITORING_HOST}:9090/-/healthy

                echo "Checking Grafana..."
                curl --retry 20 --retry-delay 5 http://${MONITORING_HOST}:3000/api/health
                """
            }
        }
    }
    post {
        success {
            echo 'Build and Deploy Successful'
        }
        failure {
            echo 'Build Failed'
        }
        always {
            sh 'docker logout'
        }
    }
}