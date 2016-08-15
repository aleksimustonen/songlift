import org.etourdot.xincproc.xinclude.XIncProcEngine;

System.out.println("Processing XIncludes...");
def workingDirectory = "${project.properties.xiInputDir}";
System.out.println("Working directory: " + workingDirectory);
def source = new File(workingDirectory);
if(!source.exists()){
	System.out.println("Defined working directory does not exist - no XIncludes to process.");
	return;
}
source.eachFile() { inputFile -> 
	def filename = org.apache.commons.io.FilenameUtils.getName(inputFile.name)
	def extension = org.apache.commons.io.FilenameUtils.getExtension(inputFile.name)
	if("xml" == extension){
		final tempFile = "${inputFile}.xi.temp";
		
		System.out.println("XInclude Processing starts for file: " + filename);

		FileInputStream inputStream = new FileInputStream(inputFile);
		FileOutputStream outputStream = new FileOutputStream(tempFile);
		System.out.println("Expanding with XIncludes to file: " + tempFile);
		XIncProcEngine.parse(inputStream, "${workingDirectory}", outputStream);
		inputStream.close();
		outputStream.close();

		System.out.println("Replacing original file with tempFile contents...");
		inputStream = new FileInputStream(tempFile);
		outputStream = new FileOutputStream(inputFile);
		outputStream << inputStream
		inputStream.close();
		outputStream.close();

		System.out.println("XInclude Processing finished for file: " + filename);
	} else {
		System.out.println("Skipping file: " + filename + " - it is of unsupported type: " + extension);
	}
} 
System.out.println("XIncludes processed.");