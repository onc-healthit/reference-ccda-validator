package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDAAllergy {
	
	private static Logger log = Logger.getLogger(CCDAAllergy.class.getName());

	private ArrayList<CCDAII>       		sectionTemplateId;
	private CCDACode                 		sectionCode;
	private ArrayList<CCDAAllergyConcern>	allergyConcern;
	
	public void log() {
		
		if(sectionCode != null)
			log.info(" Allergy Section Code = " + sectionCode.getCode());
		
		for(int j = 0; j < sectionTemplateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + sectionTemplateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + sectionTemplateId.get(j).getExtValue());
		}
		
		for(int k = 0; k < allergyConcern.size(); k++) {
			allergyConcern.get(k).log();
		}
	}
	
	public ArrayList<CCDAII> getSectionTemplateId() {
		return sectionTemplateId;
	}

	public void setSectionTemplateId(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.sectionTemplateId = ids;
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

	public void setAllergyConcern(ArrayList<CCDAAllergyConcern> acs) {
		
		if(acs != null)
			this.allergyConcern = acs;
	}

	public CCDAAllergy()
	{
		sectionTemplateId = new ArrayList<CCDAII>();
		allergyConcern = new ArrayList<CCDAAllergyConcern>();
	}
	
}
