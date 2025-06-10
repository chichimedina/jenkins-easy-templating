// Define local variables
def local_job_path = new String()
def local_job_name = new String()

if (job_vars.CREATE_ENVIRONMENT_FOLDER.toBoolean()) {
  local_job_path = job_vars.JOB_PATH + "/" + job_vars.ENVIRONMENT
} else {
  local_job_path = job_vars.JOB_PATH
}

// Determine whether or not to use the Job DSL file name as the name of the job in Jenkins
// If not, variables "JOB_DEPARTMENT" and "ENVIRONMENT" will be used to build the name of the job
if (job_vars.JOB_NAME) {
  local_job_name = local_job_path + "/" + job_vars.JOB_NAME
} else {
  if (job_vars.JOB_USE_FILENAME_AS_NAME.toBoolean()) {
    local_job_name = local_job_path + "/" + job_filename
  } else{
    local_job_name = local_job_path + "/" + "${job_vars.JOB_DEPARTMENT.toLowerCase()}-${job_vars.JOB_PARENT_PROJECT.toLowerCase()}-${job_vars.ENVIRONMENT.toLowerCase()}"
  }
}

// Set Job Descriptio based on job_vars.JOB_DESCRIPTION variable
if (job_vars.JOB_DESCRIPTION) {
  local_job_description = job_vars.JOB_DESCRIPTION
} else {
  local_job_description = "[${job_vars.JENKINS_PROJECT_NAME}] Job created for ${job_vars.JOB_DEPARTMENT.toUpperCase()}"
}

// Read Active Choice Groovy scripts and save them for later variables injection
def pipeline_script = JobDSL.readFileFromWorkspace("projects/${job_vars.JOB_PARENT_PROJECT}/scripts/PipelineScript.groovy")

// Variables injection
job_vars.each { it ->
  pipeline_script = pipeline_script.replaceAll("\\{\\{${it.key}\\}\\}", it.value)
}


JobDSL.folder(job_vars.JOB_PATH)   // Creates first the folder(s) for the project
JobDSL.folder(local_job_path)                  // Creates the folder(s) where to put this job in

JobDSL.pipelineJob(local_job_name) {

  description(local_job_description)

  // Disable concurrent jobs
  properties { 
    disableConcurrentBuilds() 
  } 

  // Keep a history of up to 500 builds
  logRotator {
    numToKeep(500)
  }

  // BU specific variables
  environmentVariables {
    keepBuildVariables(true)
    keepSystemVariables(true)
    env('JOB_PROP_1', job_vars.JOB_PROP_1)
    env('JOB_PROP_2', job_vars.JOB_PROP_2)
  }

  // Pipeline itself
  definition {
    cps {
      script(pipeline_script)
      sandbox()
    }   
  }

}
