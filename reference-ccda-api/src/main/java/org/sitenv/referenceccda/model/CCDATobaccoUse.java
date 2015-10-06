package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDATobaccoUse {
	private ArrayList<CCDAII>					tobaccoUseTemplateIds;
	private CCDACode							tobaccoUseSectionCode;
	private CCDACode							tobaccoUseCode;
	private CCDAEffTime							tobaccoUseTime;
	
	public ArrayList<CCDAII> getTobaccoUseTemplateIds() {
		return tobaccoUseTemplateIds;
	}

	public void setTobaccoUseTemplateIds(ArrayList<CCDAII> tobaccoUseTemplateIds) {
		this.tobaccoUseTemplateIds = tobaccoUseTemplateIds;
	}

	public CCDACode getTobaccoUseSectionCode() {
		return tobaccoUseSectionCode;
	}

	public void setTobaccoUseSectionCode(CCDACode tobaccoUseSectionCode) {
		this.tobaccoUseSectionCode = tobaccoUseSectionCode;
	}

	public CCDACode getTobaccoUseCode() {
		return tobaccoUseCode;
	}

	public void setTobaccoUseCode(CCDACode tobaccoUseCode) {
		this.tobaccoUseCode = tobaccoUseCode;
	}

	public CCDAEffTime getTobaccoUseTime() {
		return tobaccoUseTime;
	}

	public void setTobaccoUseTime(CCDAEffTime tobaccoUseTime) {
		this.tobaccoUseTime = tobaccoUseTime;
	}

	public CCDATobaccoUse()
	{
		
	}
}
