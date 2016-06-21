package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAVitalObs {
	private ArrayList<CCDAII>   			templateIds;
	private CCDACode						vsCode;
	private CCDACode						statusCode;
	private CCDAEffTime						measurementTime;
	private CCDAPQ							vsResult;
	private CCDACode						interpretationCode;
	private CCDAPQ							referenceValue;
	
	public CCDAVitalObs()
	{
		
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> templateIds) {
		this.templateIds = templateIds;
	}

	public CCDACode getVsCode() {
		return vsCode;
	}

	public void setVsCode(CCDACode vsCode) {
		this.vsCode = vsCode;
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

	public CCDAPQ getVsResult() {
		return vsResult;
	}

	public void setVsResult(CCDAPQ vsResult) {
		this.vsResult = vsResult;
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
