node {
    def app
	
    stage('Setup Environment') {
    echo "Setting up JDK environment for this Pipeline..."
    env.JAVA_HOME = tool name: 'jdk21', type: 'jdk'
    env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }
    
    stage('Clone Repository') {
        echo "Cloning repository..."
        git branch: 'hyunjeong', url: 'https://github.com/skaguswjd0212/Faniverse-DevOps.git' 
    }
		
    stage('Build & Test Docker Image') {
        echo "Building and testing the application..."
        dir('backend-repo/FaniverseBE') { 
            sh 'chmod +x gradlew'
            sh './gradlew clean build test'
        }
    }
        
    stage('Build Docker Image') {
        echo "Building Docker image..."
        dir('backend-repo/FaniverseBE') { 
            app = docker.build("skaguswjd0212/faniverse-devops-project")
        }
    }
    
    stage('Push Docker Image') {
        echo "Pushing Docker image to registry..."
        docker.withRegistry('https://registry.hub.docker.com', 'skaguswjd0212') { 
            app.push("${env.BUILD_NUMBER}")
            app.push("latest")
        }
    }
}
