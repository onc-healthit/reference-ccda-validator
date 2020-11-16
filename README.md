This document is meant to outline the steps to successfully configure and deploy the reference ccda application.

If you would like to get a test virtual machine up and running quickly, check out https://github.com/mieweb/vagrant-ccda-validator for a Vagrant build. Note: This is a 3rd-party option and is not directly supported by SITE/the ONC. It intends to pull, configure, and deploy the data from our codebase based on these instructions. Otherwise, please continue to follow the full instructions below to create a local, external, or customized instance of the validator.

**1. Assumptions**

    a. Tomcat 7 (7.0.53 recommended) or above has been installed and NOT running. https://tomcat.apache.org/download-70.cgi
    b. You are using a pre-built referenceccdaservice.war file. In other words, the .war file was not built from the project directly.
        Such a thing is possible but beyond the scope of this document.
    c. With Tomcat installed and the referenceccdaservice.war in your hands, you are ready to begin configuration and deployment.
    d. Java 7 or 8 is installed and a JAVA_HOME is set. Note: Java 7 is recommended. We do not support any Java version above JDK 8.

**2. Configuration Instructions**
*    Vocabulary Artifacts needed for vocabulary validation (please see https://github.com/onc-healthit/code-validator-api and https://github.com/onc-healthit/code-validator-api/tree/master/codevalidator-api/docs for further information)
*    Scenario files needed for content validation (based on https://github.com/onc-healthit/content-validator-api)
        * If not wanting to perform content validation, it is OK to have an empty scenario directory. The application will startup fine and validate correctly as long as a scenario isn't sent in the API. It will still perform IG and Vocabulary validation as needed. 
        * The scenario files used in SITE are located here: https://github.com/onc-healthit/2015-certification-ccda-testdata
*    The war
        * Get the latest war here https://github.com/onc-healthit/reference-ccda-validator/releases
*    Server (tomcat) configuration
        * Get the latest configuration file, ccdaReferenceValidatorConfig.xml, here https://github.com/onc-healthit/reference-ccda-validator/tree/master/configuration
        * Note: Please watch out for any updates to this file for each release in order keep your validator up to date as it is not included with the WAR

Vocabulary Artifacts

      1. Create a directory that will hold the vocabulary artifacts. For example, path/to/configs_folder
          a. this folder will contain the needed vocabulary configuration files

      2. Place the default ccdaReferenceValidatorConfig.xml in this folder. For example, path/to/configs_folder/ccdaReferenceValidatorConfig.xml. This file is located in this repository.
          a. this file contains the xpath expressions that target particular nodes in the given C-CDA to be validated. Each expression is configured with at least one validator. For example:
>            <expression xpathExpression="/v3:ClinicalDocument/v3:templateId[@root='2.16.840.1.113883.10.20.22.1.1' and @extension='2015-08-01']/ancestor::v3:ClinicalDocument[1]/v3:confidentialityCode[not(@nullFlavor)]">
>            		<validator>
>           			<name>ValueSetCodeValidator</name>
>            			<validationResultSeverityLevels>
>            				<codeSeverityLevel>SHOULD</codeSeverityLevel>
>            			</validationResultSeverityLevels>
>           			<allowedValuesetOids>2.16.840.1.113883.1.11.16926</allowedValuesetOids>
>           		</validator>
>           	</expression>

    3. Create a local directory for Codes, Valuesets and Scenario files. For example,
       a. path/to/validator_configuration/vocabulary/code_repository/
       b. path/to/validator_configuration/vocabulary/valueset_repository/VSAC/
       c. path/to/validator_configuration/scenarios/
       
    4. Optional: Add custom configurations to the existing path (path/to/your/configs_folder) or create a separate local directory for your custom vocabulary configurations (xml files). Using the existing path is preferred but either option works fine. This allows one to take advantage of dynamic vocabulary configurations which can be changed in realtime for each validation via the API by sending the "vocabularyConfig" key and the filename without extension as the value. The default configuration file is the one that was added earlier and is located in this repository (ccdaReferenceValidatorConfig.xml). If "referenceccda.isDynamicVocab" is set to "false" or not set at all the default file (ccdaReferenceValidatorConfig.xml) will be used. If it is set to "true" and the path is set as described, one can use any config in the directory.

NOTES: 
* For the valueset_repository, assuming the path above of /path/to/validator_configuration/vocabulary/valueset_repository/VSAC/, please ensure that the valueset repository spreadsheet files are in the aformentioned subfolder labeled VSAC. However, please do **NOT** include this VSAC folder in the referenceccdaservice.xml config file. So in this example, the actual path on the user computer or server would be /path/to/validator_configuration/vocabulary/valueset_repository/VSAC/ where as the path saved in the referenceccdaservice.xml config file referenceccdaservice.xml would be /path/to/validator_configuration/vocabulary/valueset_repository/
* Most of the code systems and valuesets are externally published and this project does **NOT** come packaged with any of these externally published files. However, valuesets which were not externally published by VSAC were hand created by SITE and are available for download here: https://github.com/onc-healthit/code-validator-api/tree/master/codevalidator-api/docs/ValueSetsHandCreatedbySITE

code system - defines a set of codes with meanings (also known as enumeration, terminology, classification, and/or ontology)

value set - selects a set of codes from those defined by one or more code systems

To learn more about valuesets and code systems, go to:
[VSAC Support](https://www.nlm.nih.gov/vsac/support/authorguidelines/code-systems.html)

The referenceccdaservice.war

    1. Navigate to the webapps directory in your Tomcat instance. For example, ~/apache-tomcat-7.0.53/webapps
    2. Place the referenceccdaservice.war file here

Server Configuration

    1. Place a copy of referenceccdaservice.xml in $CATALINA_BASE/conf/[enginename]/[hostname]/
    Note: You will likely be using the "Catalina" engine and the default host for Tomcat is "localhost". 
    For example, ~/apache-tomcat-7.0.53/conf/Catalina/localhost
    
    At this point, your directory structure should contain at least the following:
    -tomcat
    --conf
    ---Catalina
    ----localhost
    ----referenceccdaservice.xml
    --webapps
    ---referenceccdaservice.war  
    
    2. Edit the referenceccdaservice.xml key values to point to the locations configured in the previous steps and setup other values. For example:
            <Context reloadable="true">
                <Parameter name="vocabulary.localCodeRepositoryDir" value="path/to/your/code_repository" override="true"/>
                <Parameter name="vocabulary.localValueSetRepositoryDir" value="path/to/your/valueset_repository" override="true"/>
                <Parameter name="referenceccda.configFile" value="path/to/your/configs_folder/ccdaReferenceValidatorConfig.xml" override="true"/>
                <Parameter name="referenceccda.isDynamicVocab" value="false" override="true"/>
                <Parameter name="referenceccda.configFolder" value="path/to/your/configs_folder" override="true"/>
                <Parameter name="content.scenariosDir" value="path/to/your/scenarios_directory" override="true"/>
             </Context>

**3. Run the application**

    1. Start your tomcat instance - you should see output showing the databases getting initialized and the .war file getting deployed.
    NOTE: Allow a few minutes for the vocabulary valuesets and codes to be inserted into the in-memory database.
    2. Access the endpoint. For example, localhost:8080/referenceccdaservice/
    3. For convenience, API documentation and a validation UI is included:
        API documentation - /referenceccdaservice/swagger-ui.html
        UI - referenceccdaservice/ui
        
**4. Troubleshooting (please feel free to submit PRs with updates from your own issues/solutions)**

* The best way to resolve an issue is to refer to the log at tomcat/logs/referenceccdaservice.<TODAY'S DATE>.log. The errors in that log along with the various documentation for this project and its dependencies should help point to a resolution. If there is still confusion, ahead are some examples and their solutions:
    * The log states, "LOADING SCENARIO FILES AT /opt/apache-tomcat-7.0.53/mdht/Environment/VocabularyConfiguration/scenarios/" instead of the directory you have specified in your ccdaReferenceValidatorConfig.xml which exists on your computer or server
        * The ccdaReferenceValidatorConfig.xml configuration file is designed to override default settings with your local settings/directory setup. What this message means (if the path doesn't match you local file structure) is that the application has not found your ccdaReferenceValidatorConfig.xml file. Please ensure that you have taken the latest version from here https://github.com/onc-healthit/reference-ccda-validator/tree/master/configuration and have placed it in a path similar to tomcat/conf/Catalina/localhost
    * The log states, "nested exception is java.lang.OutOfMemoryError: GC overhead limit exceeded"
        * It is recommended that your computer has at least 4GBs of RAM as the software loads all of the vocabulary into RAM. It uses an "in-memory database" on boot so it's very RAM intensive. However, just because you have the RAM doesn't mean Java is handling it appropriately. If you have enough RAM free and there is still an issue, please ensure you are using Java 7 (as opposed to Java 8 or higher) and create a file named setenv.sh (.bat for windows) file in your tomcat/bin directory. Please include the following data within that file:
            * ``` 
                  #!/bin/bash
                  JAVA_HOME=/usr/lib/jvm/java-7-oracle
                  JAVA_OPTS="$JAVA_OPTS -Xmx5120m -XX:MaxPermSize=1024m"
              ```
