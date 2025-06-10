// FUNCTION: rest_call
// Description: Generic function for HTTP Rest calls
def rest_call(url, credentials, http_method) { 

  if (credentials.containsKey("username") && credentials.containsKey("password")) {
    if (http_method == 'GET') {
       // Format credentials to Base64 for HTTP basic authentication
       def http_creds = credentials.username + ":" + credentials.password
       def encodedCredentials = Base64.getEncoder().encodeToString(http_creds.getBytes())
       http_auth_header = "Basic " + encodedCredentials.toString()
    }
    if (http_method == 'POST') {
       http_auth_header = JsonOutput.toJson(credentials)
    }
  } else if (credentials.containsKey("token")) {
     http_auth_header = "Bearer " + credentials.token
  }

  try {

    connection = new URL(url).openConnection()

    connection.with {
      addRequestProperty('Content-Type', 'application/json')
      addRequestProperty('Accept', 'application/json')
      doOutput = true
      requestMethod = http_method
    }
    
    if (http_method == 'GET') {
      connection.addRequestProperty("Authorization", http_auth_header)
    }
    if (http_method == 'POST') {
      connection.getOutputStream().write(http_auth_header.getBytes('UTF-8'))
    }

    http_result = connection.content.text

  } catch (java.net.UnknownHostException ex) {
    return [error: 1, error_message: ex.toString()]
  } catch (java.lang.IllegalArgumentException ex) {
    return [error: 2, error_message: ex.toString()]
  } catch (java.net.MalformedURLException ex) {
    return [error: 3, error_message: ex.toString()]
  } catch (java.io.FileNotFoundException ex) {
    return [error: 4, error_message: ex.toString()]
  } catch (Exception ex) {
    return [error: connection.getResponseCode(), error_message: ex.toString()]
  }

  // Return an OK result set
  return [http_result: http_result, conn_host: new URL(url).host, conn_protocol: new URL(url).protocol, conn_port: new URL(url).port, conn_uri: new URL(url).file]

}
