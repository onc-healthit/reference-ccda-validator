package org.sitenv.referenceccda.validators;

import org.sitenv.referenceccda.validators.enums.ValidationResultType;

public class RefCCDAValidationResult {

	// Common Error information
	private final String description;
	private final ValidationResultType type;
	private final String xPath;
    private final String validatorConfiguredXpath;
	private final String documentLineNumber;

	// Only valid for Vocabulary testing
	private String expectedCodeSystem;
	private String actualCodeSystem;
	private String expectedCode;
	private String actualCode;
	private String expectedDisplayName;
	private String actualDisplayName;
	private String expectedValueSet;

	private RefCCDAValidationResult(RefCCDAValidationResultBuilder builder){
        this.description = builder.description;
        this.xPath = builder.xPath;
        this.validatorConfiguredXpath = builder.validatorConfiguredXpath;
        this.type = builder.type;
        this.documentLineNumber = builder.documentLineNumber;
        this.actualCode = builder.actualCode;
        this.actualCodeSystem = builder.actualCodeSystem;
        this.actualDisplayName = builder.expectedDisplayName;
        this.expectedCode = builder.expectedCode;
        this.expectedCodeSystem = builder.expectedCodeSystem;
        this.expectedDisplayName = builder.expectedDisplayName;
        this.expectedValueSet = builder.expectedValueSet;
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

    public String getExpectedCodeSystem() {
        return expectedCodeSystem;
    }

    public String getActualCodeSystem() {
        return actualCodeSystem;
    }

    public String getExpectedCode() {
        return expectedCode;
    }

    public String getActualCode() {
        return actualCode;
    }

    public String getExpectedDisplayName() {
        return expectedDisplayName;
    }

    public String getActualDisplayName() {
        return actualDisplayName;
    }

    public String getExpectedValueSet() {
        return expectedValueSet;
    }

    //builder pattern
    public static class RefCCDAValidationResultBuilder{
        // Common Error information
        private final String description;
        private final ValidationResultType type;
        private final String xPath;
        private final String validatorConfiguredXpath;
        private final String documentLineNumber;

        // Only valid for Vocabulary testing
        private String expectedCodeSystem;
        private String actualCodeSystem;
        private String expectedCode;
        private String actualCode;
        private String expectedDisplayName;
        private String actualDisplayName;
        private String expectedValueSet;

        public RefCCDAValidationResultBuilder(String description, String xPath, String validatorConfiguredXpath, ValidationResultType type, String documentLineNumber){
            this.description = description;
            this.validatorConfiguredXpath = validatorConfiguredXpath;
            this.type = type;
            this.xPath = xPath;
            this.documentLineNumber = documentLineNumber;
        }

        public RefCCDAValidationResultBuilder expectedCodeSystem(String expectedCodeSystem) {
            this.expectedCodeSystem = expectedCodeSystem;
            return this;
        }

        public RefCCDAValidationResultBuilder actualCodeSystem(String actualCodeSystem) {
            this.actualCodeSystem = actualCodeSystem;
            return this;
        }

        public RefCCDAValidationResultBuilder expectedCode(String expectedCode) {
            this.expectedCode = expectedCode;
            return this;
        }

        public RefCCDAValidationResultBuilder actualCode(String actualCode) {
            this.actualCode = actualCode;
            return this;
        }

        public RefCCDAValidationResultBuilder expectedDisplayName(String expectedDisplayName) {
            this.expectedDisplayName = expectedDisplayName;
            return this;
        }

        public RefCCDAValidationResultBuilder actualDisplayName(String actualDisplayName) {
            this.actualDisplayName = actualDisplayName;
            return this;
        }

        public RefCCDAValidationResultBuilder expectedValueSet(String expectedValueSet) {
            this.expectedValueSet = expectedValueSet;
            return this;
        }

        public RefCCDAValidationResult build(){
            return new RefCCDAValidationResult(this);
        }
    }

}
