This document is meant to outline the steps to successfully configure and deploy the reference ccda application.

**1. Assumptions**

    a. Tomcat 7 or above has been installed and NOT running. https://tomcat.apache.org/download-70.cgi
    b. You are using a pre-built referenceccdaservice.war file. In other words, the .war file was not built from the project directly.
        Such a thing is possible but beyond the scope of this document.
    c. With Tomcat installed and the referenceccdaservice.war in your hands, you are ready to begin configuration and deployment.

**2. Configuration Instructions**
*    Vocabulary Artifacts needed for vocabulary validation
*    Scenario files needed for content validation
*    The war
*    Server (tomcat) configuration

Vocabulary Artifacts

      1. Create a directory that will hold the vocabulary artifacts. For example, /path/to/VocabularyConfiguration
          a. this folder will contain the needed vocabulary configuration files

      2. Place the ccdaReferenceValidatorConfig.xml in this folder. For example, /path/to/VocabularyConfiguration/ccdaReferenceValidatorConfig.xml
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

    3.Create a local directory for Codes, Valuesets and Scenario files. For example,
       a. /path/to/validatorconfiguration/vocabulary/code_repository
       b. /path/to/validatorconfiguration/vocabulary/valueset_repository/VSAC
       c. /path/to/validatorconfiguration/scenarios
>     NOTE: Code systems and valuesets are externally published and this project does **NOT** come packaged with any of these files.
>     code system - defines a set of codes with meanings (also known as enumeration, terminology, classification, and/or ontology)
>     value set - selects a set of codes from those defined by one or more code systems
>     To learn more about valuesets and code systems, go to:
[VSAC Support](https://www.nlm.nih.gov/vsac/support/authorguidelines/code-systems.html)

The referenceccdaservice.war

    1. Navigate to the webapps directory in your Tomcat instance. For example, ~/apache-tomcat-7.0.57/webapps
    2. Place the referenceccdaservice.war file here

Server Configuration

    1. Place a copy of referenceccdaservice.xml in $CATALINA_BASE/conf/[enginename]/[hostname]/. For example, ~/apache-tomcat-7.0.57/conf/Catalina/localhost
    2. Edit the referenceccdaservice.xml key values to point to the locations configured in the previous steps. For example;
            Context reloadable="true">
                <Parameter name="vocabulary.localCodeRepositoryDir" value="E:/Development/Environment/VocabularyConfiguration/Vocabulary/code_repository" override="true"/>
            	<Parameter name="vocabulary.localValueSetRepositoryDir" value="E:/Development/Environment/VocabularyConfiguration/Vocabulary/valueset_repository" override="true"/>
            	<Parameter name="referenceccda.configFile" value="E:/Development/Environment/VocabularyConfiguration/ccdaReferenceValidatorConfig.xml" override="true"/>
            </Context>

**3. Run the application**

    1. Start your tomcat instance - you should see output showing the databases getting initialized and the .war file getting deployed.
    NOTE: Allow a few moments for the vocabulary valuesets and codes to be inserted into the in-memory database.
    2. Access the endpoint. For example, localhost:8080/referenceccdaservice/
    3. For convenience, API documentation and a validation UI is included.
    API documentation - /referenceccdaservice/swagger-ui.html
    UI - referenceccdaservice/ui