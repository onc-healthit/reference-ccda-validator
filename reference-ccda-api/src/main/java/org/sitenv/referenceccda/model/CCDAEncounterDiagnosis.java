package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDAEncounterDiagnosis {
	
	private static Logger log = Logger.getLogger(CCDAEncounterDiagnosis.class.getName());
	
	private ArrayList<CCDAII>         templateId;
	private CCDACode                  entryCode;
	private ArrayList<CCDAProblemObs> problemObs;

	public void log() {
		
		log.info("*** Encounter Diagnosis ***");
		if(entryCode != null)
			log.info("Encounter Diagnosis Code = " + entryCode.getCode());
		
		for(int j = 0; j < templateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateId.get(j).getExtValue());
		}
		
		for(int l = 0; l < problemObs.size(); l++) {
			problemObs.get(l).log();
		}
	}
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}


	public void setTemplateId(ArrayList<CCDAII> ids) {
		if(ids != null)
			this.templateId = ids;
	}


	public CCDACode getEntryCode() {
		return entryCode;
	}


	public void setEntryCode(CCDACode entryCode) {
		this.entryCode = entryCode;
	}


	public ArrayList<CCDAProblemObs> getProblemObs() {
		return problemObs;
	}


	public void setProblemObs(ArrayList<CCDAProblemObs> pobs) {
		if(pobs != null)
			this.problemObs = pobs;
	}


	public CCDAEncounterDiagnosis()
	{
		problemObs = new ArrayList<CCDAProblemObs>();
		templateId = new ArrayList<CCDAII>();
	}
}
