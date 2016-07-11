package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAAllergyObs {

	private static Logger log = Logger.getLogger(CCDAAllergyObs.class.getName());
	
	private ArrayList<CCDAII>    			templateId;
	private CCDACode             			allergyIntoleranceType;
	private CCDACode             			allergySubstance;
	private CCDAEffTime          			effTime;
	private ArrayList<CCDAAllergyReaction>  reactions;
	private CCDAAllergySeverity				severity;
	private Boolean							negationInd;
	
	public void log() {
		
		log.info("***Allergy Obs***");
		
		if(allergyIntoleranceType != null)
			log.info("Allergy Intolerance Type Code = " + allergyIntoleranceType.getCode());
		
		if(allergySubstance != null)
			log.info("Allergy Substance Code = " + allergySubstance.getCode());
	
		for(int j = 0; j < templateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateId.get(j).getExtValue());
		}
		
		if(effTime != null) 
			effTime.log();
		
		if(severity != null) 
			severity.log();
		
		for(int k = 0; k < reactions.size(); k++) {
			reactions.get(k).log();
		}
		
	}
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}


	public void setTemplateId(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateId = ids;
	}


	public CCDACode getAllergyIntoleranceType() {
		return allergyIntoleranceType;
	}


	public void setAllergyIntoleranceType(CCDACode allergyIntoleranceType) {
		this.allergyIntoleranceType = allergyIntoleranceType;
	}


	public CCDACode getAllergySubstance() {
		return allergySubstance;
	}


	public void setAllergySubstance(CCDACode allergySubstance) {
		this.allergySubstance = allergySubstance;
	}


	public CCDAEffTime getEffTime() {
		return effTime;
	}


	public void setEffTime(CCDAEffTime effTime) {
		this.effTime = effTime;
	}


	public ArrayList<CCDAAllergyReaction> getReactions() {
		return reactions;
	}


	public void setReactions(ArrayList<CCDAAllergyReaction> rs) {
		
		if(rs != null)
			this.reactions = rs;
	}


	public CCDAAllergySeverity getSeverity() {
		return severity;
	}


	public void setSeverity(CCDAAllergySeverity severity) {
		this.severity = severity;
	}


	public CCDAAllergyObs()
	{
		templateId = new ArrayList<CCDAII>();
		reactions = new ArrayList<CCDAAllergyReaction>();
	}
	
}
