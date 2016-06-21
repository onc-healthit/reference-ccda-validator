package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDASocialHistory {

	private ArrayList<CCDAII>					sectionTemplateIds;
	private CCDACode							sectionCode;
	private ArrayList<CCDASmokingStatus>		smokingStatus;
	private ArrayList<CCDATobaccoUse>			tobaccoUse;
	
	public CCDASocialHistory()
	{
		
	}

	public ArrayList<CCDAII> getSectionTemplateIds() {
		return sectionTemplateIds;
	}

	public void setSectionTemplateIds(ArrayList<CCDAII> sectionTemplateIds) {
		this.sectionTemplateIds = sectionTemplateIds;
	}

	public CCDACode getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(CCDACode sectionCode) {
		this.sectionCode = sectionCode;
	}

	public ArrayList<CCDASmokingStatus> getSmokingStatus() {
		return smokingStatus;
	}

	public void setSmokingStatus(ArrayList<CCDASmokingStatus> smokingStatus) {
		this.smokingStatus = smokingStatus;
	}

	public ArrayList<CCDATobaccoUse> getTobaccoUse() {
		return tobaccoUse;
	}

	public void setTobaccoUse(ArrayList<CCDATobaccoUse> tobaccoUse) {
		this.tobaccoUse = tobaccoUse;
	}
	
}
