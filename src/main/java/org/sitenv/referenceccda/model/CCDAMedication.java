package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAMedication {

	private ArrayList<CCDAII>     				templateIds;
	private CCDACode                 			sectionCode;
	private ArrayList<CCDAMedicationActivity>  	medActivities;
	
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

	public CCDAMedication()
	{
		
	}
}
