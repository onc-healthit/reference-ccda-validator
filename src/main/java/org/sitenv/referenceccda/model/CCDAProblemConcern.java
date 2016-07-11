package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAProblemConcern {
	
	private static Logger log = Logger.getLogger(CCDAProblemConcern.class.getName());

	private ArrayList<CCDAII>     		templateId;
	private CCDACode         	   		concernCode;
	private CCDADataElement  			statusCode;
	private CCDAEffTime      			effTime;
	private ArrayList<CCDAProblemObs>  	problems;
	
	public void log() {
		
		if(concernCode != null)
			log.info(" Problem Concern Code = " + concernCode.getCode());
		
		if(statusCode != null)
			log.info(" Problem Concern Status = " + statusCode.getValue());
		
		for(int j = 0; j < templateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateId.get(j).getExtValue());
		}
		
		if(effTime != null)
			effTime.log();
		
		for(int k = 0; k < problems.size(); k++) {
			problems.get(k).log();
		}
	}
	
	public CCDAProblemConcern()
	{
		problems = new ArrayList<CCDAProblemObs>();
		templateId = new ArrayList<CCDAII>();
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

	public ArrayList<CCDAProblemObs> getProblems() {
		return problems;
	}

	public void setProblems(ArrayList<CCDAProblemObs> ps) {
		
		if(ps != null)
			this.problems = ps;
	}
	
}
