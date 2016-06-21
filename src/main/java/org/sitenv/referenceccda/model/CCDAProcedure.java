package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAProcedure {
	private ArrayList<CCDAII>       		sectionTemplateId;
	private CCDACode                 		sectionCode;
	private ArrayList<CCDAProcActProc>		procActsProcs;
	
	public CCDAProcedure()
	{
		
	}

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

	public ArrayList<CCDAProcActProc> getProcActsProcs() {
		return procActsProcs;
	}

	public void setProcActsProcs(ArrayList<CCDAProcActProc> procActsProcs) {
		this.procActsProcs = procActsProcs;
	}
	
}
