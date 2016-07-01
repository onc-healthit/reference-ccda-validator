package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDAEncounter {
	
	private static Logger log = Logger.getLogger(CCDAEncounter.class.getName());

	private ArrayList<CCDAII>    templateId;
	private CCDACode  sectionCode;
	private ArrayList<CCDAEncounterActivity> encActivities;
	
	public void log() {
		
		if(sectionCode != null)
			log.info("Encounter Section Code = " + sectionCode.getCode());
		
		for(int j = 0; j < templateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateId.get(j).getExtValue());
		}
		
		for(int k = 0; k < encActivities.size(); k++) {
			encActivities.get(k).log();
		}
	}
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}

	public void setTemplateId(ArrayList<CCDAII> templId) {
		if(templId != null)
			this.templateId = templId;
	}

	public CCDACode getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(CCDACode sectionCode) {
		this.sectionCode = sectionCode;
	}

	public ArrayList<CCDAEncounterActivity> getEncActivities() {
		return encActivities;
	}

	public void setEncActivities(ArrayList<CCDAEncounterActivity> enActivities) {
		if(enActivities != null)
			this.encActivities = enActivities;
	}

	public CCDAEncounter()
	{
		encActivities = new ArrayList<CCDAEncounterActivity>();
		templateId = new ArrayList<CCDAII>();
	}
}
