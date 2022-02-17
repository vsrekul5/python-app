pipeline{
    agent any
    envoronment{
        registry = 'vsrekul/mypythonapp'
        dockerImage = ''
        registryCredentials = 'dh_id'
    }
    stages{
        stage('Build'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/vsrekul5/python-app.git']]])                                
            }
        }
        stage('create image'){
            steps{
                dockerImage = docker.build reegistry
                }                
            }
        }
        stage('upload the image to the Dockr Hub'){
            steps{
                docker.withRegistr('', registryCredentials){
                    dockerImage.push()
                } 
            }
        }
        stage('stop container'){
            steps{
               sh 'docker ps -f name=mypythonappContainer -q | xargs --no-run-if-empty docker container stop'
               sh 'docker container ls -a -fname=mypyappContainer -q | xargs -r docker container rm'
            }
        }
        stage('Run the app in a docker container'){
            steps{
                sudo docker run -p 8091:5000 --rm --name mypythonappContainer vsrekul/mypythonapp
            }
        }
    }
}