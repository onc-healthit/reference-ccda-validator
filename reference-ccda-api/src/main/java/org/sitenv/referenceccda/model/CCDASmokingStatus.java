package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDASmokingStatus {

	private ArrayList<CCDAII>					smokingStatusTemplateIds;
	private CCDACode							smokingStatusCode;
	private CCDAEffTime							observationTime;
	
	public CCDASmokingStatus()
	{
		
	}

	public ArrayList<CCDAII> getSmokingStatusTemplateIds() {
		return smokingStatusTemplateIds;
	}

	public void setSmokingStatusTemplateIds(ArrayList<CCDAII> smokingStatusTemplateIds) {
		this.smokingStatusTemplateIds = smokingStatusTemplateIds;
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
