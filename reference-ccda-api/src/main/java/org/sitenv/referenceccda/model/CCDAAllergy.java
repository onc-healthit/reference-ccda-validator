package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAAllergy {

	private ArrayList<CCDAII>       		sectionTemplateId;
	private CCDACode                 		sectionCode;
	private ArrayList<CCDAAllergyConcern>	allergyConcern;
	
	public ArrayList<CCDAII> getSectionTemplateId() {
		return sectionTemplateId;
	}

	public void setSectionTemplateId(ArrayList<CCDAII> sectionTemplateId) {
		this.sectionTemplateId = sectionTemplateId;
	}

	public CCDACode getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(CCDACode sectionCode) {
		this.sectionCode = sectionCode;
	}

	public ArrayList<CCDAAllergyConcern> getAllergyConcern() {
		return allergyConcern;
	}

	public void setAllergyConcern(ArrayList<CCDAAllergyConcern> allergyConcern) {
		this.allergyConcern = allergyConcern;
	}

	public CCDAAllergy()
	{
		
	}
	
}
