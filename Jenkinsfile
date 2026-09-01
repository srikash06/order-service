pipeline {

    agent any

    environment {

        APP_NAME = 'order-service'

        IMAGE_NAME = 'order-service'
    }

    stages {

        stage('Checkout') {

            steps {

                checkout scm
            }
        }


        stage('Generate Image Tag') {

            steps {

                script {

                    env.GIT_SHORT_SHA = sh(
                        script: 'git rev-parse --short=8 HEAD',
                        returnStdout: true
                    ).trim()

                    env.IMAGE_TAG = "${env.BUILD_NUMBER}-${env.GIT_SHORT_SHA}"

                    echo "Image Tag: ${env.IMAGE_TAG}"
                }
            }
        }


        stage('Maven Build & Test') {

            steps {

                sh '''
                    mvn clean verify
                '''
            }

            post {

                always {

                    junit(
                        testResults: 'target/surefire-reports/*.xml',
                        allowEmptyResults: true
                    )
                }
            }
        }


        stage('Docker Build') {

            steps {

                sh '''
                    docker build \
                      -t ${IMAGE_NAME}:${IMAGE_TAG} \
                      .
                '''
            }
        }


        stage('Docker Image Inspect') {

            steps {

                sh '''
                    docker image inspect \
                      ${IMAGE_NAME}:${IMAGE_TAG}
                '''
            }
        }


        stage('Trivy Vulnerability Scan') {

            steps {

                sh '''
                    trivy image \
                      --severity HIGH,CRITICAL \
                      --exit-code 1 \
                      --ignore-unfixed \
                      ${IMAGE_NAME}:${IMAGE_TAG}
                '''
            }
        }

    }


    post {

        success {

            echo "Pipeline successful"

            echo "Image: ${IMAGE_NAME}:${IMAGE_TAG}"
        }


        failure {

            echo "Pipeline failed"
        }


        always {

            sh '''
                docker images \
                ${IMAGE_NAME} \
                --format "table {{.Repository}}\\t{{.Tag}}\\t{{.Size}}" \
                || true
            '''
        }
    }
}