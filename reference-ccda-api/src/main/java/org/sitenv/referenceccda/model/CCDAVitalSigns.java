package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDAVitalSigns {
	
	private static Logger log = Logger.getLogger(CCDAVitalSigns.class.getName());
	
	private ArrayList<CCDAII>			templateIds;
	private CCDACode					sectionCode;
	private ArrayList<CCDAVitalOrg>		vitalsOrg;

	public void log() {
		
		if(sectionCode != null)
			log.info(" Vital Signs Section Code = " + sectionCode.getCode());
		
		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}
			
		for(int k = 0; k < vitalsOrg.size(); k++) {
			vitalsOrg.get(k).log();
		}
	}
	
	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateIds = ids;
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

	public void setVitalsOrg(ArrayList<CCDAVitalOrg> vos) {
		
		if(vos != null)
			this.vitalsOrg = vos;
	}

	public CCDAVitalSigns()
	{
		templateIds = new ArrayList<CCDAII>();
		vitalsOrg = new ArrayList<CCDAVitalOrg>();
	}
}
