package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAEncounter {

	private ArrayList<CCDAII>    templateId;
	private CCDACode  sectionCode;
	private ArrayList<CCDAEncounterActivity> encActivities;
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}

	public void setTemplateId(ArrayList<CCDAII> templateId) {
		this.templateId = templateId;
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

	public void setEncActivities(ArrayList<CCDAEncounterActivity> encActivities) {
		this.encActivities = encActivities;
	}

	public CCDAEncounter()
	{
		encActivities = new ArrayList<CCDAEncounterActivity>();
	}
}
