package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAVitalOrg {

	private ArrayList<CCDAII>   			templateIds;
	private CCDACode						statusCode;
	private CCDACode						orgCode;
	private CCDACode						translationCode;
	private CCDAEffTime						effTime;
	private ArrayList<CCDAVitalObs>			vitalObs;
	
	public CCDAVitalOrg()
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

	public CCDACode getTranslationCode() {
		return translationCode;
	}

	public void setTranslationCode(CCDACode translationCode) {
		this.translationCode = translationCode;
	}

	public CCDAEffTime getEffTime() {
		return effTime;
	}

	public void setEffTime(CCDAEffTime effTime) {
		this.effTime = effTime;
	}

	public ArrayList<CCDAVitalObs> getVitalObs() {
		return vitalObs;
	}

	public void setVitalObs(ArrayList<CCDAVitalObs> vitalObs) {
		this.vitalObs = vitalObs;
	}
	
	
}
