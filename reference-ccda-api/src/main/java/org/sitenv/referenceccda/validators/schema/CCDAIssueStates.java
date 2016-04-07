package org.sitenv.referenceccda.validators.schema;

public class CCDAIssueStates {

	/**
	 * Intended to flag if the current result has the issue specified
	 */
	private static boolean isIGIssue, isMUIssue, isSchemaError,
			isDataTypeSchemaError;

	/**
	 * Intended to flag if the entire document has a schema error of any kind
	 */
	private static boolean hasSchemaError;

	/**
	 * Should be reset for each result as unique to each result
	 */
	public static void resetUniqueResultValues() {
		isIGIssue = false;
		isMUIssue = false;
		isSchemaError = false;
		// if isDataTypeSchemaError is true, isSchemaError is also true, so
		// multiple states are allowed
		isDataTypeSchemaError = false;
	}

	/**
	 * Should be reset for each validation as a whole (e.g. a full document)
	 */
	public static void resetHasSchemaError() {
		hasSchemaError = false;
	}

	public static boolean isBaseLevelIGIssueOnly() {
		return isIGIssue && !isMUIssue;
	}

	public static boolean isGenericSchemaErrorOnly() {
		return isSchemaError && !isDataTypeSchemaError;
	}

	protected static void setIsIGIssue(boolean isIGIssue) {
		CCDAIssueStates.isIGIssue = isIGIssue;
	}

	protected static void setIsMUIssue(boolean isMUIssue) {
		CCDAIssueStates.isMUIssue = isMUIssue;
	}

	protected static void setIsSchemaError(boolean isSchemaError) {
		CCDAIssueStates.isSchemaError = isSchemaError;
		hasSchemaError = isSchemaError;
	}

	protected static void setIsDataTypeSchemaError(boolean isDataTypeSchemaError) {
		CCDAIssueStates.isDataTypeSchemaError = isDataTypeSchemaError;
	}

	/**
	 * @return true if the document has at least one schema error in its results
	 */
	public static boolean hasSchemaError() {
		return hasSchemaError;
	}

	public static boolean getCurrentIGIssueState() {
		return isIGIssue;
	}

	public static boolean getCurrentMUIssueState() {
		return isMUIssue;
	}

	public static boolean getCurrentSchemaErrorState() {
		return isSchemaError;
	}

	public static boolean getCurrentDataTypeSchemaErrorState() {
		return isDataTypeSchemaError;
	}

}
