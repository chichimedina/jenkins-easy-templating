// FUNCTION: filesize
// Description: Formats a number to Bytes, KB, MB, GB or TB
// Returns: A string with the size unit appended
def filesize = { attrs ->

   labels = [ ' bytes', 'KB', 'MB', 'GB', 'TB' ]
   size = attrs.size
   label = labels.find {
     if( size < 1024 ) {
       true
     } else {
       size /= 1024  
       false
     }
   }

 "${new java.text.DecimalFormat( attrs.format ?: '0.' ).format( size )}$label"

}
