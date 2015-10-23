package org.sitenv.referenceccda.dto;

import org.sitenv.referenceccda.validators.enums.ValidationResultType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class ValidationResultsMetaData {
	private String ccdaDocumentType;
	private boolean serviceError;
	private String serviceErrorMessage;
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

	public List<ResultMetaData> getResultMetaData() {
		resultMetaData = new ArrayList<ResultMetaData>();
		for (Map.Entry<String, AtomicInteger> entry : errorCounts.entrySet()) {
			resultMetaData.add(new ResultMetaData(entry.getKey(), entry.getValue().intValue()));
		}
		return resultMetaData;
	}

	public void addCount(ValidationResultType resultType) {
		if (errorCounts.containsKey(resultType.getTypePrettyName())) {
			errorCounts.get(resultType.getTypePrettyName()).addAndGet(1);
		} else {
			errorCounts.put(resultType.getTypePrettyName(), new AtomicInteger(1));
		}
	}
}
