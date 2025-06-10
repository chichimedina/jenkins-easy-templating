pipeline {

    agent {
       label '{{JENKINS_AGENT}}'
    }

    stages {
        
        stage ("{{PIPELINE_STAGE_1_TITLE}}") {
            
            steps {

                script {
                    println("\n\n\n")
                    println("Department's name is: {{JOB_DEPARTMENT}}")
                    println("\n\n\n")
                }
            
            }
            
        }

        stage ("{{PIPELINE_STAGE_2_TITLE}}") {
            
            steps {

                script {
                    println("\n\n\n")
                    println("Department's theme colour is: {{THEME_COLOUR}}")
                    println("\n\n\n")
                }
            
            }
            
        }

    }

}
