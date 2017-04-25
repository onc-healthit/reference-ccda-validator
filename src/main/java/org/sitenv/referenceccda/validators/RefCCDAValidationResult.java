package org.sitenv.referenceccda.validators;

import org.apache.commons.lang3.StringUtils;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.MDHTResultDetails;

public class RefCCDAValidationResult {

	// Common Error information
	private final String description;
	private final ValidationResultType type;
	private final String xPath;
    private final String validatorConfiguredXpath;
	private final String documentLineNumber;

    // Only required for MDHT
    private final MDHTResultDetails mdhtResultDetails;

	// Only valid for Vocabulary testing
	private String ruleID;	
	private String actualCode;
    private String actualCodeSystem;
    private String actualCodeSystemName;
    private String actualDisplayName;

	// ------------------------- INTERNAL CODE CHANGE START
    private String diagnosticSource;
	// ------------------------- INTERNAL CODE CHANGE END

	
	private RefCCDAValidationResult(RefCCDAValidationResultBuilder builder){
        this.description = builder.description;
        this.xPath = builder.xPath;
        this.validatorConfiguredXpath = builder.validatorConfiguredXpath;
        this.type = builder.type;
        this.documentLineNumber = builder.documentLineNumber;
        this.actualCode = builder.actualCode;
        this.actualCodeSystem = builder.actualCodeSystem;
        this.actualCodeSystemName = builder.actualCodeSystemName;
        this.actualDisplayName = builder.actualDisplayName;        
    	// ------------------------- INTERNAL CODE CHANGE START
        this.diagnosticSource = builder.diagnosticSource;
        this.ruleID = builder.ruleID;
    	// ------------------------- INTERNAL CODE CHANGE END
        if (builder.mdhtResultDetails != null) {
        	this.mdhtResultDetails = builder.mdhtResultDetails;
        } else {
        	this.mdhtResultDetails = new MDHTResultDetails(false, false, false, false);
        }
    }

    public String getDescription() {
        return description;
    }

    public ValidationResultType getType() {
        return type;
    }

    public String getxPath() {
        return xPath;
    }

    public String getValidatorConfiguredXpath() {
        return validatorConfiguredXpath;
    }

    public String getDocumentLineNumber() {
        return documentLineNumber;
    }

    public boolean isSchemaError() {
    	return mdhtResultDetails.isSchemaError();
    }
    
    public boolean isDataTypeSchemaError() {
    	return mdhtResultDetails.isDataTypeSchemaError();
    }
    
	public boolean isIGIssue() {
		return mdhtResultDetails.isIGIssue();
	}

	public boolean isMUIssue() {
		return mdhtResultDetails.isMUIssue();
	}    

    public String getActualCodeSystem() {
        return actualCodeSystem;
    }

    public String getActualCode() {
        return actualCode;
    }

    public String getActualDisplayName() {
        return actualDisplayName;
    }

    public String getActualCodeSystemName() {
        return actualCodeSystemName;
    }

	// ------------------------- INTERNAL CODE CHANGE START
    
    public String getRuleID() {
    	return this.ruleID;
    }
    
    public void setRuleID(String ruleID) {
    	this.ruleID = ruleID;
    }

	private static final String ERROR = "ERROR";
	private static final String WARNING = "WARNING";
	private static final String INFO = "INFO";
    
	public String getDiagnosticSource() {
		return diagnosticSource;
	}
	
	public String getSeverity() {
		String severityType = getType().getTypePrettyName();

		String severity = (StringUtils.containsIgnoreCase(severityType, ERROR)) ? ERROR
				: (StringUtils.containsIgnoreCase(severityType, WARNING)) ? WARNING
						: INFO;

		return severity;
	}
	// ------------------------- INTERNAL CODE CHANGE END

	
    //builder pattern
    public static class RefCCDAValidationResultBuilder{
        // Common Error information
        private final String description;
        private final ValidationResultType type;
        private final String xPath;
        private final String validatorConfiguredXpath;
        private final String documentLineNumber;

        // Only required for MDHT
        private MDHTResultDetails mdhtResultDetails;

        // Only valid for Vocabulary testing
        private String ruleID;
        private String actualCodeSystem;
        private String actualCode;
        private String actualDisplayName;
        private String actualCodeSystemName;
        
		public RefCCDAValidationResultBuilder(String description, String xPath,
				String validatorConfiguredXpath, ValidationResultType type,
				String documentLineNumber) {
			this.description = description;
			this.validatorConfiguredXpath = validatorConfiguredXpath;
			this.type = type;
			this.xPath = xPath;
			this.documentLineNumber = documentLineNumber;
		}
		
    	// ------------------------- INTERNAL CODE CHANGE START
        private String diagnosticSource;
		public RefCCDAValidationResultBuilder(String description, String xPath,
				String validatorConfiguredXpath, ValidationResultType type,
				String documentLineNumber, String diagnosticSource) {
			this.description = description;
			this.validatorConfiguredXpath = validatorConfiguredXpath;
			this.type = type;
			this.xPath = xPath;
			this.documentLineNumber = documentLineNumber;
			this.diagnosticSource = diagnosticSource;
		}

		public RefCCDAValidationResultBuilder mdhtResultDetails(MDHTResultDetails mdhtResultDetails) {
			this.mdhtResultDetails = mdhtResultDetails;
			return this;
		}
		
		// ------------------------- INTERNAL CODE CHANGE END

        public RefCCDAValidationResultBuilder ruleID(String ruleID) {
            this.ruleID = ruleID;
            return this;
        }

        public RefCCDAValidationResultBuilder actualCodeSystem(String actualCodeSystem) {
            this.actualCodeSystem = actualCodeSystem;
            return this;
        }

        public RefCCDAValidationResultBuilder actualCode(String actualCode) {
            this.actualCode = actualCode;
            return this;
        }

        public RefCCDAValidationResultBuilder actualDisplayName(String actualDisplayName) {
            this.actualDisplayName = actualDisplayName;
            return this;
        }

        public RefCCDAValidationResultBuilder actualCodeSystemName(String actualCodeSystemName) {
            this.actualCodeSystemName = actualCodeSystemName;
            return this;
        }

        public RefCCDAValidationResult build(){
            return new RefCCDAValidationResult(this);
        }
    }

}
