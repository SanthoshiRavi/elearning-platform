pipeline {
    agent any
    environment {
        DOCKERHUB_USERNAME = credentials('dockerhub-username')
        DOCKERHUB_TOKEN = credentials('dockerhub-token')
        BACKEND_IMAGE = "${DOCKERHUB_USERNAME}/elearning-backend"
        FRONTEND_IMAGE = "${DOCKERHUB_USERNAME}/elearning-frontend"
        IMAGE_TAG = "${BUILD_NUMBER}"
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
        stage('Deploy') {
            when {
                expression { return false }
            }
            steps {
                echo "Deployment disabled for now"
            }
        }
    }
    post {
        success {
            echo 'Build Successful'
        }
        failure {
            echo 'Build Failed'
        }
        always {
            sh 'docker logout'
        }
    }
}