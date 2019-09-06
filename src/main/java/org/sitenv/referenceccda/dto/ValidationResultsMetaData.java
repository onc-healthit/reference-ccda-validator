package org.sitenv.referenceccda.dto;

import org.sitenv.referenceccda.validators.enums.ValidationResultType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class ValidationResultsMetaData {
	private String ccdaDocumentType;
	private String ccdaVersion;
	private String objectiveProvided;
	private boolean serviceError;
	private String serviceErrorMessage;
	private String ccdaFileName;
	private String ccdaFileContents;
	private final Map<String, AtomicInteger> errorCounts = new LinkedHashMap<String, AtomicInteger>();
	private List<ResultMetaData> resultMetaData;
	private int vocabularyValidationConfigurationsCount;
	private int vocabularyValidationConfigurationsErrorCount;
	private String severityLevel;

	public ValidationResultsMetaData() {
		for (ValidationResultType resultType : ValidationResultType.values()) {
			errorCounts.put(resultType.getTypePrettyName(), new AtomicInteger(0));
		}
	}

	public String getCcdaDocumentType() {
		return ccdaDocumentType;
	}

	public void setCcdaDocumentType(String ccdaDocumentType) {
		this.ccdaDocumentType = ccdaDocumentType;
	}
	
	public void setCcdaVersion(String ccdaVersion) {
		this.ccdaVersion = ccdaVersion;
	}
	
	public String getCcdaVersion() {
		return ccdaVersion;
	}
	
	public String getObjectiveProvided() {
		return objectiveProvided;
	}
	

	public void setObjectiveProvided(String objectiveProvided) {
		this.objectiveProvided = objectiveProvided;
	}
	

	public boolean isServiceError() {
		return serviceError;
	}

	public void setServiceError(boolean serviceError) {
		this.serviceError = serviceError;
	}

	public String getServiceErrorMessage() {
		return serviceErrorMessage;
	}

	public void setServiceErrorMessage(String serviceErrorMessage) {
		this.serviceErrorMessage = serviceErrorMessage;
	}

	public List<ResultMetaData> getResultMetaData() {
		resultMetaData = new ArrayList<ResultMetaData>();
		for (Map.Entry<String, AtomicInteger> entry : errorCounts.entrySet()) {
			resultMetaData.add(new ResultMetaData(entry.getKey(), entry.getValue().intValue()));
		}
		return resultMetaData;
	}

	public String getCcdaFileName() {
		return ccdaFileName;
	}

	public void setCcdaFileName(String ccdaFileName) {
		this.ccdaFileName = ccdaFileName;
	}

	public String getCcdaFileContents() {
		return ccdaFileContents;
	}

	public void setCcdaFileContents(String ccdaFileContents) {
		this.ccdaFileContents = ccdaFileContents;
	}

	public void addCount(ValidationResultType resultType) {
		if (errorCounts.containsKey(resultType.getTypePrettyName())) {
			errorCounts.get(resultType.getTypePrettyName()).addAndGet(1);
		} else {
			errorCounts.put(resultType.getTypePrettyName(), new AtomicInteger(1));
		}
	}
	
	public int getVocabularyValidationConfigurationsCount() {
		return vocabularyValidationConfigurationsCount;
	}
	
	public void setVocabularyValidationConfigurationsCount(int vocabularyValidationConfigurationsCount) {
		this.vocabularyValidationConfigurationsCount = vocabularyValidationConfigurationsCount;
	}
	
	public int getVocabularyValidationConfigurationsErrorCount() {
		return vocabularyValidationConfigurationsErrorCount;
	}
	
	public void setVocabularyValidationConfigurationsErrorCount(int vocabularyValidationConfigurationsErrorCount) {
		this.vocabularyValidationConfigurationsErrorCount = vocabularyValidationConfigurationsErrorCount;
	}
	
	public String getSeverityLevel() {
		return severityLevel;
	}
	
	public void setSeverityLevel(String severityLevel) {
		this.severityLevel = severityLevel;
	}
}
