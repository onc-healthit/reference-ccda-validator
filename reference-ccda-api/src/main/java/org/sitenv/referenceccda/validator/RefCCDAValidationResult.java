package org.sitenv.referenceccda.validator;

import org.sitenv.referenceccda.validator.enums.ValidationResultType;

public class RefCCDAValidationResult {

	// Common Error information
	private String description;
	private ValidationResultType type;
	private String xPath;
	private String documentLineNumber;

	// Only valid for Vocabulary testing
	private String expectedCodeSystem;
	private String actualCodeSystem;
	private String expectedCode;
	private String actualCode;
	private String expectedDisplayName;
	private String actualDisplayName;
	private String expectedValueSet;

	public RefCCDAValidationResult(String desc, String xpath, ValidationResultType ccdaIgConformanceError) {
		description = desc;
		xPath = xpath;
		type = ccdaIgConformanceError;
	}

	public RefCCDAValidationResult(String desc, String xpath, ValidationResultType ccdaIgConformanceError, String lineNumber) {
		description = desc;
		xPath = xpath;
		type = ccdaIgConformanceError;
		documentLineNumber = lineNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ValidationResultType getType() {
		return type;
	}

	public void setType(ValidationResultType type) {
		this.type = type;
	}

	public String getxPath() {
		return xPath;
	}

	public void setxPath(String xPath) {
		this.xPath = xPath;
	}

	public String getExpectedCodeSystem() {
		return expectedCodeSystem;
	}

	public void setExpectedCodeSystem(String expectedCodeSystem) {
		this.expectedCodeSystem = expectedCodeSystem;
	}

	public String getActualCodeSystem() {
		return actualCodeSystem;
	}

	public void setActualCodeSystem(String actualCodeSystem) {
		this.actualCodeSystem = actualCodeSystem;
	}

	public String getExpectedCode() {
		return expectedCode;
	}

	public void setExpectedCode(String expectedCode) {
		this.expectedCode = expectedCode;
	}

	public String getActualCode() {
		return actualCode;
	}

	public void setActualCode(String actualCode) {
		this.actualCode = actualCode;
	}

	public String getExpectedDisplayName() {
		return expectedDisplayName;
	}

	public void setExpectedDisplayName(String expectedDisplayName) {
		this.expectedDisplayName = expectedDisplayName;
	}

	public String getActualDisplayName() {
		return actualDisplayName;
	}

	public void setActualDisplayName(String actualDisplayName) {
		this.actualDisplayName = actualDisplayName;
	}

	public String getExpectedValueSet() {
		return expectedValueSet;
	}

	public void setExpectedValueSet(String expectedValueSet) {
		this.expectedValueSet = expectedValueSet;
	}

	public String getDocumentLineNumber() {
		return documentLineNumber;
	}

	public void setDocumentLineNumber(String documentLineNumber) {
		this.documentLineNumber = documentLineNumber;
	}

}
