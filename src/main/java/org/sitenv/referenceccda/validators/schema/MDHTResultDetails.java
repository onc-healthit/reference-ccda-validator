package org.sitenv.referenceccda.validators.schema;

public class MDHTResultDetails {
	private boolean isSchemaError, isDataTypeSchemaError, isIGIssue, isMUIssue;
	
	public MDHTResultDetails(boolean isSchemaError, boolean isDataTypeSchemaError, 
			boolean isIGIssue, boolean isMUIssue) {
		this.isSchemaError = isSchemaError;
		this.isDataTypeSchemaError = isDataTypeSchemaError;
		this.isIGIssue = isIGIssue;
		this.isMUIssue = isMUIssue;
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
}
