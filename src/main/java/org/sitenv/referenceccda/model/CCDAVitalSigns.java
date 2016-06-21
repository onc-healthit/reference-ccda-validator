package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAVitalSigns {
	
	private ArrayList<CCDAII>			templateIds;
	private CCDACode					sectionCode;
	private ArrayList<CCDAVitalOrg>		vitalsOrg;

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> templateIds) {
		this.templateIds = templateIds;
	}

	public CCDACode getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(CCDACode sectionCode) {
		this.sectionCode = sectionCode;
	}

	public ArrayList<CCDAVitalOrg> getVitalsOrg() {
		return vitalsOrg;
	}

	public void setVitalsOrg(ArrayList<CCDAVitalOrg> vitalsOrg) {
		this.vitalsOrg = vitalsOrg;
	}

	public CCDAVitalSigns()
	{
		
	}
}
