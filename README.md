This document is meant to outline the steps to successfully configure and deploy the reference ccda application.

**1. Assumptions**

    a. Tomcat 7 or above has been installed and NOT running. https://tomcat.apache.org/download-70.cgi
    b. You are using a pre-built referenceccdaservice.war file. In other words, the .war file was not built from the project directly.
        Such a thing is possible but beyond the scope of this document.
    c. With Tomcat installed and the referenceccdaservice.war in your hands, you are ready to begin configuration and deployment.

**2. Configuration Instructions**
*    Vocabulary Artifacts needed for vocabulary validation (please see https://github.com/siteadmin/code-validator-api and https://github.com/siteadmin/code-validator-api/tree/master/codevalidator-api/docs for further information)
*    Scenario files needed for content validation
*    The war
*    Server (tomcat) configuration

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
       c. path/to/validator_configuration/scenarios_directory/
       
    4. Optional: Add custom configurations to the existing path (path/to/your/configs_folder) or create a separate local directory for your custom vocabulary configurations (xml files). Using the existing path is preferred but either option works fine. This allows one to take advantage of dynamic vocabulary configurations which can be changed in realtime for each validation via the API by sending the "vocabularyConfig" key and the filename without extension as the value. The default configuration file is the one that was added earlier and is located in this repository (ccdaReferenceValidatorConfig.xml). If "referenceccda.isDynamicVocab" is set to "false" or not set at all the default file (ccdaReferenceValidatorConfig.xml) will be used. If it is set to "true" and the path is set as described, one can use any config in the directory.

NOTES: 
* For the valueset_repository, assuming the path above of /path/to/validator_configuration/vocabulary/valueset_repository/VSAC/, please ensure that the valueset repository spreadsheet files are in the aformentioned subfolder labeled VSAC. However, please do **NOT** include this VSAC folder in the referenceccdaservice.xml config file. So in this example, the actual path on the user computer or server would be /path/to/validator_configuration/vocabulary/valueset_repository/VSAC/ where as the path saved in the referenceccdaservice.xml config file referenceccdaservice.xml would be /path/to/validator_configuration/vocabulary/valueset_repository/
* Most of the code systems and valuesets are externally published and this project does **NOT** come packaged with any of these externally published files. However, valuesets which were not externally published by VSAC were hand created by SITE and are available for download here: https://github.com/siteadmin/code-validator-api/tree/master/codevalidator-api/docs/ValueSetsHandCreatedbySITE

code system - defines a set of codes with meanings (also known as enumeration, terminology, classification, and/or ontology)

value set - selects a set of codes from those defined by one or more code systems

To learn more about valuesets and code systems, go to:
[VSAC Support](https://www.nlm.nih.gov/vsac/support/authorguidelines/code-systems.html)

The referenceccdaservice.war

    1. Navigate to the webapps directory in your Tomcat instance. For example, ~/apache-tomcat-7.0.57/webapps
    2. Place the referenceccdaservice.war file here

Server Configuration

    1. Place a copy of referenceccdaservice.xml in $CATALINA_BASE/conf/[enginename]/[hostname]/. For example, ~/apache-tomcat-7.0.57/conf/Catalina/localhost
    2. Edit the referenceccdaservice.xml key values to point to the locations configured in the previous steps and setup other values. For example;
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
    NOTE: Allow a few moments for the vocabulary valuesets and codes to be inserted into the in-memory database.
    2. Access the endpoint. For example, localhost:8080/referenceccdaservice/
    3. For convenience, API documentation and a validation UI is included:
        API documentation - /referenceccdaservice/swagger-ui.html
        UI - referenceccdaservice/ui
