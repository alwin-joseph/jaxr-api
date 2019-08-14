env.label = "jaxr-tck-pod-${UUID.randomUUID().toString()}"

pipeline {
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))  
  }
  agent {
    kubernetes {
      label "${env.label}"
      defaultContainer 'jakartaeetck-ci'
      yaml """
apiVersion: v1
kind: Pod
metadata:
spec:
  hostAliases:
  - ip: "127.0.0.1"
    hostnames:
    - "localhost.localdomain"
  containers:
  - name: jakartaeetck-ci
    image: jakartaee/cts-base:0.1
    command:
    - cat
    tty: true
    imagePullPolicy: Always
    resources:
      limits:
        memory: "5Gi"
        cpu: "2.0"
"""
    }
  }

parameters {
  string(name: 'GF_BUNDLE_URL', 
         defaultValue: 'http://download.eclipse.org/glassfish/glassfish-5.1.0.zip', 
         description: 'URL required for downloading GlassFish Full/Web profile bundle' )
  string(name: 'JAXRTCK_BUNDLE_BASE_URL', 
         defaultValue: '', 
	 description: 'Base URL required for downloading prebuilt binary TCK Bundle from a hosted location' )
  string(name: 'JAXRTCK_BUNDLE_FILE_NAME', 
         defaultValue: '', 
	 description: 'Name of bundle file to be appended to the base url' )
}

environment {
CTS_HOME = "/root"
ANT_OPTS = "-Djavax.xml.accessExternalStylesheet=all -Djavax.xml.accessExternalSchema=all -Djavax.xml.accessExternalDTD=file,http" 
}

stages {
  stage('jaxr-tck-run') {
    steps {
    container('jaxr-tck-ci') {
      sh """
        env
        bash -x ${WORKSPACE}/docker/jaxrtck.sh
        """
        archiveArtifacts artifacts: "jaxrtck.log",allowEmptyArchive: true
        junit testResults: 'results/junitreports/*.xml', allowEmptyResults: true    }
    }
  }
}

}
