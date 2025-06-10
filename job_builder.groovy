
def job_definition_files

// Check if the `JOB DEFINITION FILES` Jenkins parameter is set and not empty
if (binding.hasVariable('JOB DEFINITION FILES')) {

  job_definition_files = binding.getVariable('JOB DEFINITION FILES')

  if (job_definition_files.isEmpty()) {
    throw new Exception("\n\n\n\tRequired input parameter `JOB DEFINITION FILES` is empty.\n        Please check this out.\n\n")
  }

} else {
  throw new Exception("\n\n\n\tRequired input parameter `JOB DEFINITION FILES` is not set.\n        Please check this out.\n\n")
}

// Create a List object based on the `JOB DEFINITION FILES` input parameter, and delete any string or blank strings
def job_files = job_definition_files.split('\n') as List
job_files.removeAll { it =~ /^[ |#]+.*$/ || it.isEmpty() }

if (job_files.isEmpty()) {
  throw new Exception("\n\n\n\tThe list of Job definition files is empty or has no valid entries.\n        Please check this out.\n\n")
}

// Iterate through a list of job definition files given the `JOB DEFINITION FILES` input parameter
job_files.each { job ->

   // 
   // NOTE: A job (a .job file in the repository) is in reality an instance of `varsfile`. The sole existence of this `varsfile` instance defines a job to create.
   // This is why it's merged to a global `vars.groovy` file later on
   //

   // Get job filename from the `JOB DEFINITION FILES` input variable
   job_filename = new File(job).getName().replaceFirst('(\\..*)$', '').replaceAll("_", "-")
   
   // Get local variables from file
   local_varsfile = evaluate(readFileFromWorkspace(WORKSPACE + "/jobs/" + job))
   // Get global variables from file
   global_varsfile = evaluate(readFileFromWorkspace("projects/${local_varsfile.JOB_PARENT_PROJECT}/vars/vars.groovy"))
   // Merge global and local variables maps. Local variables global ones.
   vars = global_varsfile << local_varsfile
   
   // Pass some variables on to the DSL script
   bindings = new Binding()
   bindings.setVariable("JobDSL", this)
   bindings.setVariable("job_filename", job_filename)
   bindings.setVariable("job_workspace", WORKSPACE)
   bindings.setVariable("job_vars", vars)
     
   shell = new GroovyShell(bindings)
   script = shell.parse(readFileFromWorkspace("projects/${local_varsfile.JOB_PARENT_PROJECT}/scripts/PipelineJob.groovy"))
   script.run()

}
