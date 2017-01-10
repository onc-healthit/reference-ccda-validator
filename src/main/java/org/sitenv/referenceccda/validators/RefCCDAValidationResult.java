package org.sitenv.referenceccda.validators;

import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.MDHTResultDetails;

public class RefCCDAValidationResult {

	// Common Error information
	private final String description;
	private final ValidationResultType type;
	private final String xPath;
    private final String validatorConfiguredXpath;
	private final String documentLineNumber;
    private final MDHTResultDetails mdhtResultDetails;

	// Only valid for Vocabulary testing
	private String actualCode;
    private String actualCodeSystem;
    private String actualCodeSystemName;
    private String actualDisplayName;

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
        this.mdhtResultDetails = builder.mdhtResultDetails;
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

    //builder pattern
    public static class RefCCDAValidationResultBuilder{
        // Common Error information
        private final String description;
        private final ValidationResultType type;
        private final String xPath;
        private final String validatorConfiguredXpath;
        private final String documentLineNumber;
        private final MDHTResultDetails mdhtResultDetails;

        // Only valid for Vocabulary testing
        private String actualCodeSystem;
        private String actualCode;
        private String actualDisplayName;
        private String actualCodeSystemName;

		public RefCCDAValidationResultBuilder(String description, String xPath,
				String validatorConfiguredXpath, ValidationResultType type, String documentLineNumber, 
				MDHTResultDetails mdhtResultDetails) {
			this.description = description;
			this.validatorConfiguredXpath = validatorConfiguredXpath;
			this.type = type;
			this.xPath = xPath;
			this.documentLineNumber = documentLineNumber;
			this.mdhtResultDetails = mdhtResultDetails;
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
