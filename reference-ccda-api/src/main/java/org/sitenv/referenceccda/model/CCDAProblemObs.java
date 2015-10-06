package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAProblemObs {

	private ArrayList<CCDAII>    templateId;
	private CCDACode             problemType;
	private CCDACode             translationProblemType;
	private CCDAEffTime          effTime;
	private CCDACode             problemCode;
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}

	public void setTemplateId(ArrayList<CCDAII> templateId) {
		this.templateId = templateId;
	}

	public CCDACode getProblemType() {
		return problemType;
	}

	public void setProblemType(CCDACode problemType) {
		this.problemType = problemType;
	}

	public CCDACode getTranslationProblemType() {
		return translationProblemType;
	}

	public void setTranslationProblemType(CCDACode translationProblemType) {
		this.translationProblemType = translationProblemType;
	}

	public CCDAEffTime getEffTime() {
		return effTime;
	}

	public void setEffTime(CCDAEffTime effTime) {
		this.effTime = effTime;
	}

	public CCDACode getProblemCode() {
		return problemCode;
	}

	public void setProblemCode(CCDACode problemCode) {
		this.problemCode = problemCode;
	}

	public CCDAProblemObs()
	{
		
	}
}
