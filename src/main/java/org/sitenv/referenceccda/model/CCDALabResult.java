package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDALabResult {

	private ArrayList<CCDAII>			resultSectionTempalteIds;
	private CCDACode					sectionCode;
	private ArrayList<CCDALabResultOrg>	resultOrg;
	private Boolean						isLabTestInsteadOfResult;
	
	public CCDALabResult()
	{
		
	}

	public ArrayList<CCDAII> getResultSectionTempalteIds() {
		return resultSectionTempalteIds;
	}

	public void setResultSectionTempalteIds(ArrayList<CCDAII> resultSectionTempalteIds) {
		this.resultSectionTempalteIds = resultSectionTempalteIds;
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

	public void setResultOrg(ArrayList<CCDALabResultOrg> resultOrg) {
		this.resultOrg = resultOrg;
	}
}
