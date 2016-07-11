package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDALabResultOrg {

	private static Logger log = Logger.getLogger(CCDALabResultOrg.class.getName());
	
	private ArrayList<CCDAII>   			templateIds;
	private CCDACode						statusCode;
	private CCDACode						orgCode;
	private CCDAEffTime						effTime;
	private ArrayList<CCDALabResultObs>		resultObs;
	
	public void log() {
		
		if(orgCode != null)
			log.info(" Organizer  Code = " + orgCode.getCode());
		
		if(statusCode != null)
			log.info(" Organizer Status  Code = " + statusCode.getCode());
		
		if(effTime != null)
			effTime.log();
		
		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}
		
		for(int k = 0; k < resultObs.size(); k++) {
			resultObs.get(k).log();
		}
	}
	
	public CCDALabResultOrg()
	{
		templateIds = new ArrayList<CCDAII>();
		resultObs = new ArrayList<CCDALabResultObs>();
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateIds = ids;
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

	public void setResultObs(ArrayList<CCDALabResultObs> robs) {
		
		if(robs != null)
			this.resultObs = robs;
	}
	
}
