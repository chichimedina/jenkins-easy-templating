// FUNCTION: run_command
// Description: Run a shell command and return a map containing 'stdout' and 'stderr'
def run_command(command, environment=null) {

   def out = new StringBuffer()
   def err = new StringBuffer()
   def process = command.execute(environment, null)

   process.consumeProcessOutput(out, err)
   process.waitForOrKill(wait_for_task)

   return ["stdout": out, "stderr": err]

}
