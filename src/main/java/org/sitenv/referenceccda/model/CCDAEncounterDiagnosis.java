package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAEncounterDiagnosis {
   
	private ArrayList<CCDAII>         templateId;
	private CCDACode                  entryCode;
	private ArrayList<CCDAProblemObs> problemObs;

	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}


	public void setTemplateId(ArrayList<CCDAII> templateId) {
		this.templateId = templateId;
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


	public void setProblemObs(ArrayList<CCDAProblemObs> problemObs) {
		this.problemObs = problemObs;
	}


	public CCDAEncounterDiagnosis()
	{
		problemObs = new ArrayList<CCDAProblemObs>();
	}
}
