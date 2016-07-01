package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDALabResult {

	private static Logger log = Logger.getLogger(CCDALabResult.class.getName());
	
	private ArrayList<CCDAII>			resultSectionTempalteIds;
	private CCDACode					sectionCode;
	private ArrayList<CCDALabResultOrg>	resultOrg;
	private Boolean						isLabTestInsteadOfResult;
	
	public Boolean getIsLabTestInsteadOfResult() {
		return isLabTestInsteadOfResult;
	}

	public void setIsLabTestInsteadOfResult(Boolean isLabTestInsteadOfResult) {
		this.isLabTestInsteadOfResult = isLabTestInsteadOfResult;
	}

	public void log() {
		
		if(sectionCode != null)
			log.info(" Lab Result Section Code = " + sectionCode.getCode());
		
		for(int j = 0; j < resultSectionTempalteIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + resultSectionTempalteIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + resultSectionTempalteIds.get(j).getExtValue());
		}
		
		log.info(" Is Lab Test " + isLabTestInsteadOfResult);
		
		for(int k = 0; k < resultOrg.size(); k++) {
			resultOrg.get(k).log();
		}
	}
	
	public CCDALabResult()
	{
		resultSectionTempalteIds = new ArrayList<CCDAII>();
		resultOrg = new ArrayList<CCDALabResultOrg>();
		isLabTestInsteadOfResult = false;
	}

	public ArrayList<CCDAII> getResultSectionTempalteIds() {
		return resultSectionTempalteIds;
	}

	public void setResultSectionTempalteIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.resultSectionTempalteIds = ids;
	}

	public CCDACode getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(CCDACode sectionCode) {
		this.sectionCode = sectionCode;
	}

	public ArrayList<CCDALabResultOrg> getResultOrg() {
		return resultOrg;
	}

	public void setResultOrg(ArrayList<CCDALabResultOrg> ros) {
		
		if(ros != null)
			this.resultOrg = ros;
	}
}
