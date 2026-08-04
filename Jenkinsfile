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
        stage('Load Infrastructure Configuration')
        {
            steps {
                script {
                    env.FRONTEND_HOST = sh (script: """
                    aws ssm get-parameter \
                    --name /elearning/infrastructure/frontend-private-ip \
                    --query Parameter.Value \
                    --output text
                    """,returnStdout: true
                    ).trim()
                    env.BACKEND_HOST = sh (script: """
                    aws ssm get-parameter \
                    --name /elearning/infrastructure/backend-private-ip \
                    --query Parameter.Value \
                    --output text
                    """,returnStdout: true
                    ).trim()
                    env.MONITORING_HOST = sh (script: """
                    aws ssm get-parameter \
                    --name /elearning/infrastructure/monitoring-private-ip \
                    --query Parameter.Value \
                    --output text
                    """,returnStdout: true
                    ).trim()
                    env.S3_BUCKET_NAME = sh (script: """
                    aws ssm get-parameter \
                    --name /elearning/storage/course-content-bucket \
                    --query Parameter.Value \
                    --output text
                    """,returnStdout: true
                    ).trim()
                    echo "Frontend Host: ${env.FRONTEND_HOST}"
                    echo "Backend Host: ${env.BACKEND_HOST}"
                    echo "Monitoring Host: ${env.MONITORING_HOST}"
                }
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
        stage('Generate Prometheus Configuration')
        {
            steps {
                sh '''
                echo "Generating Prometheus configuration..."
                sed \
                -e "s|\\${FRONTEND_HOST}|${FRONTEND_HOST}|g" \
                -e "s|\\${BACKEND_HOST}|${BACKEND_HOST}|g" \
                -e "s|\\${MONITORING_HOST}|${MONITORING_HOST}|g" \
                deployment/monitoring/prometheus.yml.template
                > deployment/monitoring/prometheus.yml

                echo "Prometheus configuration generated successfully."
                '''
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
        stage('Deploy Frontend') {
            steps {
                sshagent(credentials: ['ec2-ssh-key']) {
                    sh """
                        echo "Creating frontend runtime configuration..."

                        cat > deployment/frontend/.env.frontend <<EOF
        BACKEND_HOST=${BACKEND_HOST}
        BACKEND_PORT=8081
        EOF

                        ssh -o StrictHostKeyChecking=no \
                            ec2-user@${FRONTEND_HOST} \
                            'mkdir -p ~/frontend'

                        scp -o StrictHostKeyChecking=no \
                            deployment/frontend/docker-compose.frontend.yml \
                            ec2-user@${FRONTEND_HOST}:~/frontend/

                        scp -o StrictHostKeyChecking=no \
                            deployment/frontend/.env.frontend \
                            ec2-user@${FRONTEND_HOST}:~/frontend/

                        ssh -o StrictHostKeyChecking=no \
                            ec2-user@${FRONTEND_HOST} '
                                cd ~/frontend

                                chmod 600 .env.frontend

                                docker compose \
                                    --env-file .env.frontend \
                                    -f docker-compose.frontend.yml \
                                    pull

                                docker compose \
                                    --env-file .env.frontend \
                                    -f docker-compose.frontend.yml \
                                    up -d --remove-orphans
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
                sh """
                echo "Checking Grafana..."
                curl \
                  --retry 30 \
                  --retry-delay 10 \
                  --retry-connrefused \
                  http://${MONITORING_HOST}:3000/api/health
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
            sh '''
            rm -f deployment/frontend/.env.frontend
            rm -f deployment/monitoring/prometheus.yml
            docker logout || true
            '''
        }
    }
}