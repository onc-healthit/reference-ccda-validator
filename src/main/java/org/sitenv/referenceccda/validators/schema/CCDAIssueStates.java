package org.sitenv.referenceccda.validators.schema;

public class CCDAIssueStates {

	/**
	 * Intended to flag if the current result has the issue specified
	 */
	private  boolean isIGIssue, isMUIssue, isSchemaError,
			isDataTypeSchemaError;

	/**
	 * Intended to flag if the entire document has a schema error of any kind
	 */
	private  boolean hasSchemaError;

	/**
	 * Should be reset for each result as unique to each result
	 */
	public  void resetUniqueResultValues() {
		this.isIGIssue = false;
		this.isMUIssue = false;
		this.isSchemaError = false;
		// if isDataTypeSchemaError is true, isSchemaError is also true, so
		// multiple states are allowed
		this.isDataTypeSchemaError = false;
	}

	public  boolean isBaseLevelIGIssueOnly() {
		return this.isIGIssue && !this.isMUIssue;
	}

	public  boolean isGenericSchemaErrorOnly() {
		return this.isSchemaError && !this.isDataTypeSchemaError;
	}

	protected  void setIsIGIssue(boolean isIGIssue) {
		//CCDAIssueStates.isIGIssue = isIGIssue;
		this.isIGIssue = isIGIssue;
	}

	protected  void setIsMUIssue(boolean isMUIssue) {
//		CCDAIssueStates.isMUIssue = isMUIssue;
		this.isMUIssue = isMUIssue;
	}

	protected  void setIsSchemaError(boolean isSchemaError) {
		//CCDAIssueStates.isSchemaError = isSchemaError;
		this.hasSchemaError = isSchemaError;
		this.isSchemaError = isSchemaError;
	}

	protected  void setIsDataTypeSchemaError(boolean isDataTypeSchemaError) {
		this.isDataTypeSchemaError = isDataTypeSchemaError;
	}

	/**
	 * @return true if the document has at least one schema error in its results
	 */
	public  boolean hasSchemaError() {
		return this.hasSchemaError;
	}

	public  boolean getCurrentIGIssueState() {
		return this.isIGIssue;
	}

	public  boolean getCurrentMUIssueState() {
		return this.isMUIssue;
	}

	public  boolean getCurrentSchemaErrorState() {
		return this.isSchemaError;
	}

	public  boolean getCurrentDataTypeSchemaErrorState() {
		return this.isDataTypeSchemaError;
	}

}
