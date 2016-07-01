package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDAProcedure {
	
	private static Logger log = Logger.getLogger(CCDAProcedure.class.getName());
	
	private ArrayList<CCDAII>       		sectionTemplateId;
	private CCDACode                 		sectionCode;
	private ArrayList<CCDAProcActProc>		procActsProcs;
	
	public void log() {
		
		if(sectionCode != null)
			log.info(" Procedure Section Code = " + sectionCode.getCode());
		
		for(int j = 0; j < sectionTemplateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + sectionTemplateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + sectionTemplateId.get(j).getExtValue());
		}
		
		for(int k = 0; k < procActsProcs.size(); k++) {
			procActsProcs.get(k).log();
		}
	}
	
	public CCDAProcedure()
	{
		sectionTemplateId = new ArrayList<CCDAII>();
		procActsProcs = new ArrayList<CCDAProcActProc>();
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

	public ArrayList<CCDAProcActProc> getProcActsProcs() {
		return procActsProcs;
	}

	public void setProcActsProcs(ArrayList<CCDAProcActProc> paps) {
		
		if(paps != null)
			this.procActsProcs = paps;
	}
	
}
