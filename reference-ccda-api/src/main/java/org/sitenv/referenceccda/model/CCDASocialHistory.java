package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDASocialHistory {

	private static Logger log = Logger.getLogger(CCDASocialHistory.class.getName());
	
	private ArrayList<CCDAII>					sectionTemplateIds;
	private CCDACode							sectionCode;
	private ArrayList<CCDASmokingStatus>		smokingStatus;
	private ArrayList<CCDATobaccoUse>			tobaccoUse;
	private CCDABirthSexObs						birthSex;
	
	public CCDABirthSexObs getBirthSex() {
		return birthSex;
	}

	public void setBirthSex(CCDABirthSexObs birthSex) {
		this.birthSex = birthSex;
	}

	public void log() {
		
		if(sectionCode != null)
			log.info(" SocialHistory Section Code = " + sectionCode.getCode());
		
		for(int j = 0; j < sectionTemplateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + sectionTemplateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + sectionTemplateIds.get(j).getExtValue());
		}
		
		for(int k = 0; k < smokingStatus.size(); k++) {
			smokingStatus.get(k).log();
		}
		
		for(int l = 0; l < tobaccoUse.size(); l++) {
			tobaccoUse.get(l).log();
		}
		
		if(birthSex != null)
			birthSex.log();
	}
	
	public CCDASocialHistory()
	{
		sectionTemplateIds = new ArrayList<CCDAII>();
		smokingStatus = new ArrayList<CCDASmokingStatus>();
		tobaccoUse = new ArrayList<CCDATobaccoUse>();
	}

	public ArrayList<CCDAII> getSectionTemplateIds() {
		return sectionTemplateIds;
	}

	public void setSectionTemplateIds(ArrayList<CCDAII> ids) {
		
		
		if(ids != null)
			this.sectionTemplateIds = ids;
	}

	public CCDACode getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(CCDACode sectionCode) {
		this.sectionCode = sectionCode;
	}

	public ArrayList<CCDASmokingStatus> getSmokingStatus() {
		return smokingStatus;
	}

	public void setSmokingStatus(ArrayList<CCDASmokingStatus> ss) {
		
		if(ss != null)
			this.smokingStatus = ss;
	}

	public ArrayList<CCDATobaccoUse> getTobaccoUse() {
		return tobaccoUse;
	}

	public void setTobaccoUse(ArrayList<CCDATobaccoUse> tu) {
		
		if(tu != null)
			this.tobaccoUse = tu;
	}
	
}
