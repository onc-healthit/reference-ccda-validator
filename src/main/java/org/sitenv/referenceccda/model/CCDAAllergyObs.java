package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAAllergyObs {

	private ArrayList<CCDAII>    			templateId;
	private CCDACode             			allergyIntoleranceType;
	private CCDACode             			allergySubstance;
	private CCDAEffTime          			effTime;
	private ArrayList<CCDAAllergyReaction>  reactions;
	private CCDAAllergySeverity				severity;
	private Boolean							negationInd;
	
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}


	public void setTemplateId(ArrayList<CCDAII> templateId) {
		this.templateId = templateId;
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


	public void setReactions(ArrayList<CCDAAllergyReaction> reactions) {
		this.reactions = reactions;
	}


	public CCDAAllergySeverity getSeverity() {
		return severity;
	}


	public void setSeverity(CCDAAllergySeverity severity) {
		this.severity = severity;
	}


	public CCDAAllergyObs()
	{
		
	}
	
}
