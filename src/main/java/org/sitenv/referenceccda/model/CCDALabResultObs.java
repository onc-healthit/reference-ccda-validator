package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDALabResultObs {

	private ArrayList<CCDAII>   			templateIds;
	private CCDACode						labCode;
	private CCDACode						statusCode;
	private CCDAEffTime						measurementTime;
	private CCDAPQ							results;
	private CCDACode						resultCode;
	private CCDACode						interpretationCode;
	private CCDAPQ							referenceValue;
	
	public CCDALabResultObs()
	{
		
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> templateIds) {
		this.templateIds = templateIds;
	}

	public CCDACode getLabCode() {
		return labCode;
	}

	public void setLabCode(CCDACode labCode) {
		this.labCode = labCode;
	}

	public CCDACode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(CCDACode statusCode) {
		this.statusCode = statusCode;
	}

	public CCDAEffTime getMeasurementTime() {
		return measurementTime;
	}

	public void setMeasurementTime(CCDAEffTime measurementTime) {
		this.measurementTime = measurementTime;
	}

	public CCDAPQ getResults() {
		return results;
	}

	public void setResults(CCDAPQ results) {
		this.results = results;
	}

	public CCDACode getResultCode() {
		return resultCode;
	}

	public void setResultCode(CCDACode resultCode) {
		this.resultCode = resultCode;
	}

	public CCDACode getInterpretationCode() {
		return interpretationCode;
	}

	public void setInterpretationCode(CCDACode interpretationCode) {
		this.interpretationCode = interpretationCode;
	}

	public CCDAPQ getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(CCDAPQ referenceValue) {
		this.referenceValue = referenceValue;
	}
}

