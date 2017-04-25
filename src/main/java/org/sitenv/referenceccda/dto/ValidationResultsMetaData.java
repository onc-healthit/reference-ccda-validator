package org.sitenv.referenceccda.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationResultsMetaData {
	private String ccdaDocumentType;
	private boolean serviceError;
	private String serviceErrorMessage;
	private String ccdaFileName;
	private String ccdaFileContents;
	private final Map<String, AtomicInteger> errorCounts = new LinkedHashMap<String, AtomicInteger>();
	private List<ResultMetaData> resultMetaData;
	
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

	// ------------------------- INTERNAL CODE CHANGE START
	private static final String INFO = "INFO";
	private static final String WARNING = "WARNING";
	private static final String ERROR = "ERROR";
	private boolean isValid;
	private String cdaSchemaValidationErrorMessage;

	@JsonProperty("isValid")
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	@JsonProperty("resultMetaData")
	public List<ResultMetaData> getCustomResultMetaData() {
		resultMetaData = new ArrayList<ResultMetaData>();
		for (Map.Entry<String, AtomicInteger> entry : errorCounts.entrySet()) {
			resultMetaData.add(new ResultMetaData(entry.getKey(), entry.getValue().intValue(), getSeverityType(entry.getKey())));
		}
		return resultMetaData;
	}

	
	private String getSeverityType(String key) {
		String severityType = (StringUtils.containsIgnoreCase(key, ERROR)) ? ERROR
				: (StringUtils.containsIgnoreCase(key, WARNING)) ? WARNING : INFO;
		
		return severityType;
	}

	public String getCdaSchemaValidationErrorMessage() {
		return cdaSchemaValidationErrorMessage;
	}

	public void setCdaSchemaValidationErrorMessage(
			String cdaSchemaValidationErrorMessage) {
		this.cdaSchemaValidationErrorMessage = cdaSchemaValidationErrorMessage;
	}
	
	@JsonIgnore
	// ------------------------- INTERNAL CODE CHANGE END
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

}
