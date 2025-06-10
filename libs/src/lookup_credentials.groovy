// FUNCTION: lookup_credentials
// Description: Look up a Jenkins credentials in the Jenkins Credentials collection
// Returns: A map with proper keys depending on the Jenkins Credentials type
def lookup_credentials(jenkins_credentials, folder_fullpath = null) { 

  def scope
  
  // If no folder has been passed in, then look up from Global scope
  // Otherwise, look up from the Folder scope
  if (folder_fullpath == null) {
    scope = Jenkins.instance
  } else {
    scope = Jenkins.instance.getAllItems(AbstractFolder.class).find{ (it.getFullName() == folder_fullpath) }
  }

  // Look up "username" and "password" from credentials in the Jenkins credentials store
  def jenkinsCredentials = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
          com.cloudbees.plugins.credentials.Credentials.class,
          scope,
          null,
          null
  );

  // Iteract through the Jenkins credentials collection
  for (creds in jenkinsCredentials) {
  
    // If this Jenkins credentials is the one we passed in as a parameter
    if (creds.id == jenkins_credentials) {
      res = (creds.toString() =~ /AWSCredentialsImpl/)                                  // AWS Credentials type
      if (res.size() > 0) {
         return ["accessKey": creds.accessKey, "secretKey": creds.secretKey]
      }
      res = (creds.toString() =~ /StringCredentialsImpl/)                               // String Credentials type
      if (res.size() > 0) {
         return ["secret": creds.secret]
      }
      res = (creds.toString() =~ /UsernamePasswordCredentialsImpl/)                     // Username/Password Credentials type
      if (res.size() > 0) {
         return ["username": creds.username, "password": creds.password]
      }
    }
  }

  return []

}
