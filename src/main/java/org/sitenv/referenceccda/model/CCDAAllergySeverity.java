package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAAllergySeverity {
	
	private static Logger log = Logger.getLogger(CCDAAllergySeverity.class.getName());

	private ArrayList<CCDAII>				templateIds;
	private CCDACode						severity;
	
	public void log() {
		
		log.info("***Allergy Severity ***");
		
		if(severity != null)
			log.info("Allergy Severity Code = " + severity.getCode());
		
		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}
		
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateIds = ids;
	}

	public CCDACode getSeverity() {
		return severity;
	}

	public void setSeverity(CCDACode severity) {
		this.severity = severity;
	}

	public CCDAAllergySeverity()
	{
		templateIds = new ArrayList<CCDAII>();
	}
}
