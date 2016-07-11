package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAProblem {

	private static Logger log = Logger.getLogger(CCDAProblem.class.getName());
	
	private ArrayList<CCDAII>       		sectionTemplateId;
	private CCDACode                 		sectionCode;
	private ArrayList<CCDAProblemConcern>  	problemConcerns;
	
	public void log() {
		
		if(sectionCode != null)
			log.info(" Problem Section Code = " + sectionCode.getCode());
		
		for(int j = 0; j < sectionTemplateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + sectionTemplateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + sectionTemplateId.get(j).getExtValue());
		}
		
		for(int k = 0; k < problemConcerns.size(); k++) {
			problemConcerns.get(k).log();
		}
	}
	
	public ArrayList<CCDAProblemConcern> getProblemConcerns() {
		return problemConcerns;
	}

	public void setProblemConcerns(ArrayList<CCDAProblemConcern> pc) {
		
		if(pc != null)
			this.problemConcerns = pc;
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

	public CCDAProblem()
	{
		problemConcerns = new ArrayList<CCDAProblemConcern>();
		sectionTemplateId = new ArrayList<CCDAII>();
	}
}
