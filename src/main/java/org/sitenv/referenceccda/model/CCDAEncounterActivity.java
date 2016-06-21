package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAEncounterActivity {

	private ArrayList<CCDAII>                 templateId;
	private CCDACode                          encounterTypeCode;
	private CCDAEffTime                       effectiveTime;
	private ArrayList<CCDAEncounterDiagnosis> diagnoses;
	private ArrayList<CCDAServiceDeliveryLoc> sdLocs;
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}

	public void setTemplateId(ArrayList<CCDAII> templateId) {
		this.templateId = templateId;
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

	public void setDiagnoses(ArrayList<CCDAEncounterDiagnosis> diagnoses) {
		this.diagnoses = diagnoses;
	}

	public ArrayList<CCDAServiceDeliveryLoc> getSdLocs() {
		return sdLocs;
	}

	public void setSdLocs(ArrayList<CCDAServiceDeliveryLoc> sdLocs) {
		this.sdLocs = sdLocs;
	}

	public CCDAEncounterActivity()
	{
		diagnoses = new ArrayList<CCDAEncounterDiagnosis>();
	}
}
