System.out.println("Building Modules.xml...");
def workingDirectory = "${project.properties.workingDir}";
System.out.println("List of modules is populated from working directory: " + workingDirectory);
def source = new File(workingDirectory);
if(!source.exists()){
	System.out.println("Defined working directory does not exist - no modules to process.");
	return;
}

def lines = [];

source.eachFile() { inputFile -> 
	def filename = org.apache.commons.io.FilenameUtils.getName(inputFile.name);
	if(inputFile.isDirectory()){
		
		//System.out.println("Processing starts for file: " + filename);

		def line = "<module runlevel=\"2\">modules/" + filename + "/</module>";
		lines.add(line);
		System.out.println("Module line: " + line);

		//System.out.println("Processing finished for file: " + filename);
	} else {
		System.out.println("Skipping file: " + filename + " - it is not a directory.");
	}
} 

def vanillaXml = "${project.properties.vanillaXml}";
def targetXml = "${project.properties.targetXml}";

def xmlLines = org.apache.commons.io.FileUtils.readLines(new File(vanillaXml), "UTF-8");
xmlLines = xmlLines.findAll({it.contains("<module ")});
xmlLines.addAll(lines);
xmlLines.add(0, "<modules>");
xmlLines.add("</modules>");
xmlLines.add(0, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");

org.apache.commons.io.FileUtils.touch(new File(targetXml));
org.apache.commons.io.FileUtils.writeLines(new File(targetXml), xmlLines);

System.out.println("Modules.xml built.");