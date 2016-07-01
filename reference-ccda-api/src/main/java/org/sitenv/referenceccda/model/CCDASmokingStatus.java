package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDASmokingStatus {

	private static Logger log = Logger.getLogger(CCDASmokingStatus.class.getName());
	
	private ArrayList<CCDAII>					smokingStatusTemplateIds;
	private CCDACode							smokingStatusCode;
	private CCDAEffTime							observationTime;
	
	public void log() {
		
		if(smokingStatusCode != null)
			log.info(" Smoking Status Code = " + smokingStatusCode.getCode());
		
		for(int j = 0; j < smokingStatusTemplateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + smokingStatusTemplateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + smokingStatusTemplateIds.get(j).getExtValue());
		}
		
		if(observationTime != null)
			observationTime.log();
	}
	
	public CCDASmokingStatus()
	{
		smokingStatusTemplateIds = new ArrayList<CCDAII>();
	}

	public ArrayList<CCDAII> getSmokingStatusTemplateIds() {
		return smokingStatusTemplateIds;
	}

	public void setSmokingStatusTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.smokingStatusTemplateIds = ids;
	}

	public CCDACode getSmokingStatusCode() {
		return smokingStatusCode;
	}

	public void setSmokingStatusCode(CCDACode smokingStatusCode) {
		this.smokingStatusCode = smokingStatusCode;
	}

	public CCDAEffTime getObservationTime() {
		return observationTime;
	}

	public void setObservationTime(CCDAEffTime observationTime) {
		this.observationTime = observationTime;
	}
}
