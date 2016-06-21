package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDALabResultOrg {

	private ArrayList<CCDAII>   			templateIds;
	private CCDACode						statusCode;
	private CCDACode						orgCode;
	private CCDAEffTime						effTime;
	private ArrayList<CCDALabResultObs>		resultObs;
	
	public CCDALabResultOrg()
	{
		
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> templateIds) {
		this.templateIds = templateIds;
	}

	public CCDACode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(CCDACode statusCode) {
		this.statusCode = statusCode;
	}

	public CCDACode getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(CCDACode orgCode) {
		this.orgCode = orgCode;
	}

	public CCDAEffTime getEffTime() {
		return effTime;
	}

	public void setEffTime(CCDAEffTime effTime) {
		this.effTime = effTime;
	}

	public ArrayList<CCDALabResultObs> getResultObs() {
		return resultObs;
	}

	public void setResultObs(ArrayList<CCDALabResultObs> resultObs) {
		this.resultObs = resultObs;
	}
	
}
