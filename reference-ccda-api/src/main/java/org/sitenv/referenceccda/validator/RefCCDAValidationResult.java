package org.sitenv.referenceccda.validator;

import org.sitenv.referenceccda.validator.enums.ErrorType;

public class RefCCDAValidationResult {

	// Common Error information
	private String errorDescription;
	private ErrorType errorType;
	private String xPath;

	// Only valid for Vocabulary testing
	private String expectedCodeSystem;
	private String actualCodeSystem;
	private String expectedCode;
	private String actualCode;
	private String expectedDisplayName;
	private String actualDisplayName;
	private String expectedValueSet;

	public RefCCDAValidationResult(String errorDesc, String xpath, ErrorType ccdaIgConformanceError) {
		errorDescription = errorDesc;
		xPath = xpath;
		errorType = ccdaIgConformanceError;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
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

}
