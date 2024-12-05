node {
    def app
    def jdk

    stage('Setup Environment') {
    echo "Setting up JDK environment for this Pipeline..."
    jdk = tool name: 'jdk21', type: 'jdk'
        
    withEnv([
            "JAVA_HOME=${jdk}",
            "PATH=${jdk}/bin:${env.PATH}",
            "PROJECT_ID=shaped-infusion-435600-i6",
            "CLUSTER_NAME=kube",
            "LOCATION=asia-northeast3-a",
            "CREDENTIALS_ID=gke"
    ]) {
            echo "JAVA_HOME: ${env.JAVA_HOME}"
            echo "PATH: ${env.PATH}"
            echo "PROJECT_ID: ${env.PROJECT_ID}"
            echo "CLUSTER_NAME: ${env.CLUSTER_NAME}"
        }
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
	
    stage('Apply Kubernetes Secrets') {
	echo "Applying Kubernetes Secrets..."
        sh 'kubectl apply -f k8s/app-secrets.yml'
        sh 'kubectl apply -f k8s/gcp-secrets.yml'
    }
	
    stage('Deploy to Kubernetes') {
        when {
            branch 'main'
        }
        steps {
            script {
                sh 'kubectl apply -f k8s/faniverse-backend-deployment.yml'
	        sh 'kubectl apply -f k8s/faniverse-backend-service.yml'
                echo "Deploying to Kubernetes..."
	        step([$class: 'KubernetesEngineBuilder',
                  projectId: env.PROJECT_ID,
                  clusterName: env.CLUSTER_NAME,
                  location: env.LOCATION,
                  manifestPattern: 'k8s/*.yml',
                  credentialsId: env.CREDENTIALS_ID,
                  verifyDeployments: true])
            }
        }    
    }
}
