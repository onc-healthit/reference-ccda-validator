package org.sitenv.referenceccda.validators.schema;

public class MDHTResultDetails {
	private boolean isSchemaError, isDataTypeSchemaError, isIGIssue, isMUIssue, isDS4PIssue;
	
	public MDHTResultDetails() {
		this(false, false, false, false, false);
	}	
	
	public MDHTResultDetails(boolean isSchemaError, boolean isDataTypeSchemaError, 
			boolean isIGIssue, boolean isMUIssue, boolean isDS4PIssue) {
		this.isSchemaError = isSchemaError;
		this.isDataTypeSchemaError = isDataTypeSchemaError;
		this.isIGIssue = isIGIssue;
		this.isMUIssue = isMUIssue;
		this.isDS4PIssue = isDS4PIssue;
	}

	public boolean isSchemaError() {
		return isSchemaError;
	}

	public void setSchemaError(boolean isSchemaError) {
		this.isSchemaError = isSchemaError;
	}

	public boolean isDataTypeSchemaError() {
		return isDataTypeSchemaError;
	}

	public void setDataTypeSchemaError(boolean isDataTypeSchemaError) {
		this.isDataTypeSchemaError = isDataTypeSchemaError;
	}

	public boolean isIGIssue() {
		return isIGIssue;
	}

	public void setIGIssue(boolean isIGIssue) {
		this.isIGIssue = isIGIssue;
	}

	public boolean isMUIssue() {
		return isMUIssue;
	}

	public void setMUIssue(boolean isMUIssue) {
		this.isMUIssue = isMUIssue;
	}
	
	public boolean isDS4PIssue() {
		return isDS4PIssue;
	}
	
	public void setDS4PIssue(boolean isDS4PIssue) {
		this.isDS4PIssue = isDS4PIssue;
	}
}
