package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAAllergyConcern {

	private ArrayList<CCDAII>     		templateId;
	private CCDACode         	   		concernCode;
	private CCDADataElement  			statusCode;
	private CCDAEffTime      			effTime;
	private ArrayList<CCDAAllergyObs>  	allergyObs;
	
	public CCDAAllergyConcern()
	{
		
	}

	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}

	public void setTemplateId(ArrayList<CCDAII> templateId) {
		this.templateId = templateId;
	}

	public CCDACode getConcernCode() {
		return concernCode;
	}

	public void setConcernCode(CCDACode concernCode) {
		this.concernCode = concernCode;
	}

	public CCDADataElement getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(CCDADataElement statusCode) {
		this.statusCode = statusCode;
	}

	public CCDAEffTime getEffTime() {
		return effTime;
	}

	public void setEffTime(CCDAEffTime effTime) {
		this.effTime = effTime;
	}

	public ArrayList<CCDAAllergyObs> getAllergyObs() {
		return allergyObs;
	}

	public void setAllergyObs(ArrayList<CCDAAllergyObs> allergyObs) {
		this.allergyObs = allergyObs;
	}
}
