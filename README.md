# AppResourceChecker
A small Swing-based tool for searching unused resources within Java projects.

##Usage example:
Let's say you have some properties files containing the resources for multilingual support and you want
to check which of the resource keys are not in use by your application any longer.
You can select a properties file in AppResourceChecker and select a folder. The application can then recursively search
the files in the folder and find unused resource keys.

Currently only *Java*, *XHTML* and files ending with *-flow.xml* are recognized by the application to keep the search time short.

##Build and development tool-chain
* **Maven 3** is used for building the project.
* I am using **Eclipse Neo.1a** with **m2e-plugin** for development. That's why you will find a *.project* and a
pom-derived *.classpath* file in the repository.


###ToDo:
* Set default for the location of the properites file
* Set default for the folder to be searched     
* Make file types configurable
* Write tests...
* Clean up code...
* Maven should build a zip-file an include a batch-file for executing the application.
