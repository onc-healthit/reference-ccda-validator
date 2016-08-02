package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.content.ParserUtilities;

public class CCDAProblemObs {

	private static Logger log = Logger.getLogger(CCDAProblemObs.class.getName());
	
	private ArrayList<CCDAII>    templateId;
	private CCDACode             problemType;
	private ArrayList<CCDACode>  translationProblemType;
	private CCDAEffTime          effTime;
	private CCDACode             problemCode;
	
	public void compare(CCDAProblemObs subObs, String probObsContext, ArrayList<RefCCDAValidationResult> results) {
		
		log.info(" Comparing data for problem observation Value element/code attribute: " + probObsContext);

		String elementName = "Problem Observation for Value element/code attribute: " + probObsContext;

		// Compare template Ids 
		ParserUtilities.compareTemplateIds(templateId, subObs.getTemplateId(), results, elementName);

		// Compare Effective Times
		String elementNameTime = "Problem Observation Effective Time for Value element/code attribute: " + probObsContext;
		ParserUtilities.compareEffectiveTime(effTime, subObs.getEffTime(), results, elementNameTime);
		
		// Compare PRoblem Codes 
		String elementNameVal = "Problem Observation Value element/code attribute: " + probObsContext;
		ParserUtilities.compareCode(problemCode, subObs.getProblemCode(), results, elementNameVal);
		
		// Add negation indicator
	}
	
	public void log() {
		
		log.info("***Problem Obs***");
		
		if(problemType != null)
			log.info("Problem Type Code = " + problemType.getCode());
		
		for(int l = 0; l < translationProblemType.size(); l++) {
			log.info("Problem Translation Type Code = " + translationProblemType.get(l).getCode());
		}
		
		for(int j = 0; j < templateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateId.get(j).getExtValue());
		}
		
		if(effTime != null)
			effTime.log();
		
		if(problemCode != null)
			log.info("Problem Code = " + problemCode.getCode());
		
	}
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}

	public void setTemplateId(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateId = ids;
	}

	public CCDACode getProblemType() {
		return problemType;
	}

	public void setProblemType(CCDACode problemType) {
		this.problemType = problemType;
	}

	public ArrayList<CCDACode> getTranslationProblemType() {
		return translationProblemType;
	}

	public void setTranslationProblemType(ArrayList<CCDACode> translationProblemType) {
		if(translationProblemType != null)
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
		templateId = new ArrayList<CCDAII>();
		translationProblemType = new ArrayList<CCDACode>();
	}

	
}
