package org.sitenv.referenceccda.validator;

public class RefCCDAValidationResult {
	
	static public String CCDA_IG_CONFORMANCE_ERROR = "CCDA_IG_ERROR";
	static public String CCDA_IG_CONFORMANCE_WARN = "CCDA_IG_WARN";
	static public String CCDA_IG_CONFORMANCE_INFO = "CCDA_IG_INFO";
	static public String CCDA_VOCAB_CONFORMANCE_ERROR = "CCDA_VOCAB_ERROR";
	static public String CCDA_VOCAB_CONFORMANCE_WARN = "CCDA_VOCAB_WARN";
	static public String CCDA_VOCAB_CONFORMANCE_INFO = "CCDA_VOCAB_INFO";
	static public String REF_CCDA_ERROR = "REF_CCDA_ERROR";
	static public String REF_CCDA_WARN = "REF_CCDA_WARN";
	static public String REF_CCDA_INFO = "REF_CCDA_INFO";
	
	//Common Error information
	private String errorDescription;
	private String errorType;
	private String xPath;
	
	
	// Only valid for Vocabulary testing
	private String expectedCodeSystem;
	private String actualCodeSystem;
	private String expectedCode;
	private String actualCode;
	private String expectedDisplayName;
	private String actualDisplayName;
	private String expectedValueSet;
	
	public RefCCDAValidationResult(String errorDesc, String xpath, String errType)
	{
		errorDescription = errorDesc;
		xPath = xpath;
		errorType = errorType;
	}
	
	
	public String getErrorDescription() {
		return errorDescription;
	}


	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}


	public String getErrorType() {
		return errorType;
	}


	public void setErrorType(String errorType) {
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
