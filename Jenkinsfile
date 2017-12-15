#!/usr/bin/env groovy

/**
 * Jenkinsfile Template for Gradle projects
 *
 * The first section of the file declares any variable to be used during the different stage:
 * numbers, username/password, URL, port numbers...
 *
 * The second section lists examples of stages that can be used in a jenkinsfile. Simple stages
 * as well as parallel stages and conditional stages.
 *
 * For any information about syntax of the Jenkinsfile please refer to the following documentation:
 * Jenkins pipeline syntax documentation: https://jenkins.io/doc/book/pipeline/syntax/
 *
 **/

node {

    /**
     *---------------------------------------------------------------------------------------
     * Variables
     *---------------------------------------------------------------------------------------
     **/

    /**
     * Setting global variable for this build
     * branch                = git compliant branch name
     * artifactoryBaseUrl    = Artifactory base URL
     * towerTemplateUrl      = Ansible Tower job template URL
     * intEnv                = Integration environment settings (for automatic deployment)
     **/

    def branch = env.BRANCH_NAME.replaceAll('/', '-')

    def artifactoryBaseUrl = "https://artifactory.intranet.opt/artifactory/"

    def intEnv = '''{

    }'''.replace("\"", "\\\"").replace("\n", "")

    /** Set configuration environment */
    stage('config') {
        // Set Java JDK in path
        env.JAVA_HOME = "${tool 'java-8'}"
        env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"

        // Set Gradle tool and add it to the path
        def gradleHome = tool 'gradle-3.4.1'
        env.PATH = "${gradleHome}/bin:${env.PATH}"
    }

    /** Checkout latest branch code */
    stage('checkout') {
        checkout scm
    }

    /** Clean the environment */
    stage('clean') {
        sh "gradle clean --no-daemon"
    }

    /** Launch Unit Tests */
    stage('tests') {
    	try {
   		sh "gradle test --no-daemon --debug --refresh-dependencies"
    	} catch(err) {
    		throw err
    	} finally {
    		junit '**/build/**/TEST-*.xml'
    	}
    }

    /** Build the project and create the .pom */
    stage('packaging') {
        sh "gradle build --no-daemon --refresh-dependencies"
        sh "gradle createPom --no-daemon"
    }

    /** Run SonarQube analysis */
    stage('quality analysis') {
        withSonarQubeEnv('Sonar') {
            sh "gradle sonarqube --no-daemon -Dsonar.branch=${branch}"
        }
    }

    /**
     * Execute following steps, only when on the (main) master branch
     **/
    if (branch.equals("master")) {
        def warPath = ""

        /** Run dependency check */
     /*   stage('dependency check') {
            sh "gradle dependencyCheckAnalyze --info --no-daemon"
        }*/

        /**
         * Publish package to Artifactory
         **/
        stage('publish') {
            def server = Artifactory.server 'ART'
            def props = readProperties  file: 'gradle.properties'
            def uploadTargetPath = props.version.contains('-SNAPSHOT') ? 'libs-snapshot-local' : 'libs-release-local'
            warPath = "${uploadTargetPath}/nc/opt/core/${props['projectName']}/${props.version}/${props['projectName']}-${props.version}.jar"
            def uploadSpec = """ {
                  "files": [
                    {
                      "pattern": "build/libs/(.*).jar",
                      "target": "${uploadTargetPath}/nc/opt/core/${props['projectName']}/${props.version}/{1}.jar",
                      "props": "source=jenkins",
                      "regexp": true
                    },
                    {
                      "pattern": "build/libs/(.*).pom",
                      "target": "${uploadTargetPath}/nc/opt/core/${props['projectName']}/${props.version}/{1}.pom",
                      "props": "source=jenkins",
                      "regexp": true
                    }
                 ]
                }"""
            def buildInfo = server.upload(uploadSpec)
            server.publishBuildInfo(buildInfo)
        }
    }
    
    /**
	 * Edition du rapport de tests
	**/
	publishHTML([
	    allowMissing: false,
	    alwaysLinkToLastBuild: false,
	    keepAll: true,
	    reportDir: 'build/reports/tests/test/',
	    reportFiles: 'index.html',
	    reportName: 'Test Report',
	    reportTitles: ''
	])

	/**
	 * Edition du rapport de check des dépendances
	**/
/*	publishHTML([
	    allowMissing: false,
	    alwaysLinkToLastBuild: false,
	    keepAll: true,
	    reportDir: 'build/reports/',
	    reportFiles: 'dependency-check-report.html',
	    reportName: 'Dependency Check Report',
	    reportTitles: ''
	])*/
    
}