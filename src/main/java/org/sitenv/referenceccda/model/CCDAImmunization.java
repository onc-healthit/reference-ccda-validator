package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAImmunization {
	private ArrayList<CCDAII>     				templateIds;
	private CCDACode                 			sectionCode;
	private ArrayList<CCDAImmunizationActivity> immActivity;
	
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

	public ArrayList<CCDAImmunizationActivity> getImmActivity() {
		return immActivity;
	}

	public void setImmActivity(ArrayList<CCDAImmunizationActivity> immActivity) {
		this.immActivity = immActivity;
	}

	public CCDAImmunization() 
	{
		
	}
}
