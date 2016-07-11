package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAAllergyConcern {
	
	private static Logger log = Logger.getLogger(CCDAAllergyConcern.class.getName());

	private ArrayList<CCDAII>     		templateId;
	private CCDACode         	   		concernCode;
	private CCDADataElement  			statusCode;
	private CCDAEffTime      			effTime;
	private ArrayList<CCDAAllergyObs>  	allergyObs;
	
	public void log() {
		
		if(concernCode != null)
			log.info(" Allergy Concern Code = " + concernCode.getCode());
		
		if(statusCode != null)
			log.info(" Allergy Concern Status = " + statusCode.getValue());
		
		for(int j = 0; j < templateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateId.get(j).getExtValue());
		}
		
		if(effTime != null)
			effTime.log();
		
		for(int k = 0; k < allergyObs.size(); k++) {
			allergyObs.get(k).log();
		}
	}
	
	public CCDAAllergyConcern()
	{
		templateId = new ArrayList<CCDAII>();
		allergyObs = new ArrayList<CCDAAllergyObs>();
	}

	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}

	public void setTemplateId(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateId = ids;
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

	public void setAllergyObs(ArrayList<CCDAAllergyObs> aobs) {
		
		if(aobs != null)
			this.allergyObs = aobs;
	}
}
