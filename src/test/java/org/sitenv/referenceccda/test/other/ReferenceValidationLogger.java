package org.sitenv.referenceccda.test.other;

import java.util.List;

import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.vocabularies.test.other.ValidationLogger;

public class ReferenceValidationLogger extends ValidationLogger {

	public static void printResults(List<RefCCDAValidationResult> results) {
			printResults(results, true, true, true);
	}
	
	public static void printResults(List<RefCCDAValidationResult> results, 
			boolean showSchema, boolean showType, boolean showIgOrMuType) {
		if (logResults) {
			if (!results.isEmpty()) {
				for (RefCCDAValidationResult result : results) {
					printResults(result, showSchema, showType, showIgOrMuType);
				}
				println();
			} else {
				println("There are no results to print as the list is empty.");
			}
		}
	}
	
	public static void printResults(RefCCDAValidationResult result) {
			printResults(result, true, true, true);
	}
	
	public static void printResults(RefCCDAValidationResult result, 
			boolean showSchema, boolean showType, boolean showIgOrMuOrDS4PType) {
		if (logResults) {
			println("Description : " + result.getDescription());
			if(showType) {
				println("Type : " + result.getType());
			}
			if(showSchema) {
				println("result.isSchemaError() : " + result.isSchemaError());
				println("result.isDataTypeSchemaError() : " + result.isDataTypeSchemaError());
			}
			if(showIgOrMuOrDS4PType) {
				println("result.isIGIssue() : " + result.isIGIssue());
				println("result.isMUIssue() : " + result.isMUIssue());
				println("result.isDS4PIssue() : " + result.isDS4PIssue());
			}
		}
	}	
	
}
