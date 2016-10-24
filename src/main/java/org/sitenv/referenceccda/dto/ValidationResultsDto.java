package org.sitenv.referenceccda.dto;

import org.sitenv.referenceccda.validators.RefCCDAValidationResult;

import java.util.List;

public class ValidationResultsDto {
	private ValidationResultsMetaData resultsMetaData;
	private List<RefCCDAValidationResult> ccdaValidationResults;

	public List<RefCCDAValidationResult> getCcdaValidationResults() {
		return ccdaValidationResults;
	}

	public void setCcdaValidationResults(List<RefCCDAValidationResult> ccdaValidationResults) {
		this.ccdaValidationResults = ccdaValidationResults;
	}

	public ValidationResultsMetaData getResultsMetaData() {
		return resultsMetaData;
	}

	public void setResultsMetaData(ValidationResultsMetaData resultsMetaData) {
		this.resultsMetaData = resultsMetaData;
	}

}
