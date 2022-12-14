pipeline{
    agent any
    stages{
        stage('Build Backend'){
            steps{
                bat 'mvn clean package -DskipTests=true'
            }
        }
        stage('Unit Tests'){
            steps{
                bat 'mvn test'
            }
        }
        stage('Sonar Analysis'){
            environment{
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps{
                withSonarQubeEnv('SONAR_QUBE'){
                     bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=TasksBackEnd -Dsonar.host.url=http://localhost:9000 -Dsonar.login=squ_c5dde614ed024b13f79b3275e4892699c1c35064 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/model/**,**Application.java"
                }
               
            }
        }
        stage('Quality Gate'){
            steps{
                sleep(5)
                timeout(time:1 , unit: 'MINUTES'){
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Deploy Backend'){
            steps{
                deploy adapters: [tomcat8(credentialsId: 'TomCatLogin', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }
        stage('API Test'){
            steps{
                dir('api-test'){
                    git 'https://github.com/RafaelAmaralPaula/tasks-api-test'
                    bat 'mvn test'
                }
            }
        }
        stage('Deploy Frontend'){
            steps{
                dir('frontend'){
                    git 'https://github.com/RafaelAmaralPaula/tasks-frontend'
                    bat 'mvn clean package'
                    deploy adapters: [tomcat8(credentialsId: 'TomCatLogin', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks', war: 'target/tasks.war'

                }
            }
        }
        stage('Functional Test'){
            steps{
                dir('functional-test'){
                    git 'https://github.com/RafaelAmaralPaula/tasks-functional-tests'
                    bat 'mvn test'
                }
            }
        }
        stage('Deploy Prod'){
            steps{
                bat 'docker-compose build'
                bat 'docker-compose up -d'
            }
        }
        stage('Health Check'){
            steps{
                dir('healthcheck-test'){
                    git 'https://github.com/RafaelAmaralPaula/tasks-healthcheck'
                    bat 'mvn test'
                }
            }
        }
    }
    post{
        always{
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml , functional-test/target/surefire-reports/*.xml , api-test/target/surefire-reports/*.xml , healthcheck/target/surefire-reports/*.xml'
        }
        unsuccessful{
            emailext attachLog: true, body: 'See the attached log below', subject: 'Build $BUILD_NUMBER has faildes', to: 'rafaelpaulajr@gmail.com'
        }
        fixed{
             emailext attachLog: true, body: 'See the attached log below', subject: 'Build is fine!', to: 'rafaelpaulajr@gmail.com'
        }
    }
}

