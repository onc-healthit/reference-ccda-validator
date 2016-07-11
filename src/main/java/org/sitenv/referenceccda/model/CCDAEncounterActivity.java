package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAEncounterActivity {

	private static Logger log = Logger.getLogger(CCDAEncounterActivity.class.getName());
	
	private ArrayList<CCDAII>                 templateId;
	private CCDACode                          encounterTypeCode;
	private CCDAEffTime                       effectiveTime;
	private ArrayList<CCDAEncounterDiagnosis> diagnoses;
	private ArrayList<CCDAServiceDeliveryLoc> sdLocs;
	private ArrayList<CCDAProblemObs>         indications;
	
	public void log() {
		
		log.info("*** Encounter Activit ***");
		
		if(encounterTypeCode != null)
			log.info("Encounter Activity Type Code = " + encounterTypeCode.getCode());
		
		for(int j = 0; j < templateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateId.get(j).getExtValue());
		}
		
		if(effectiveTime != null) {
			effectiveTime.log();
		}
		
		
		for(int l = 0; l < diagnoses.size(); l++) {
			diagnoses.get(l).log();
		}
		
		for(int m = 0; m < sdLocs.size(); m++) {
			sdLocs.get(m).log();
		}
		
		for(int n = 0; n < indications.size(); n++) {
			indications.get(n).log();
		}
	}
	
	public ArrayList<CCDAProblemObs> getIndications() {
		return indications;
	}

	public void setIndications(ArrayList<CCDAProblemObs> inds) {
		if(inds != null)
			this.indications = inds;
	}

	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}
	
	public void setTemplateId(ArrayList<CCDAII> ids) {
		if(ids != null)
			this.templateId = ids;
	}

	public CCDACode getEncounterTypeCode() {
		return encounterTypeCode;
	}

	public void setEncounterTypeCode(CCDACode encounterTypeCode) {
		this.encounterTypeCode = encounterTypeCode;
	}

	public CCDAEffTime getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(CCDAEffTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public ArrayList<CCDAEncounterDiagnosis> getDiagnoses() {
		return diagnoses;
	}

	public void setDiagnoses(ArrayList<CCDAEncounterDiagnosis> diag) {
		
		if(diag != null)
			this.diagnoses = diag;
	}

	public ArrayList<CCDAServiceDeliveryLoc> getSdLocs() {
		return sdLocs;
	}

	public void setSdLocs(ArrayList<CCDAServiceDeliveryLoc> sdl) {
		if(sdl != null)
			this.sdLocs = sdl;
	}

	public CCDAEncounterActivity()
	{
		diagnoses = new ArrayList<CCDAEncounterDiagnosis>();
		templateId = new ArrayList<CCDAII>();
		indications = new ArrayList<CCDAProblemObs>();
		sdLocs = new ArrayList<CCDAServiceDeliveryLoc>();
	}
}
