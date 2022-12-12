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
                sleep(10)
                timeout(time:1 , unit: 'MINUTES'){
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }
}

