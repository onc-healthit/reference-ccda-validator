package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAAllergySeverity {

	private ArrayList<CCDAII>				templateIds;
	private CCDACode						severity;
	
	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> templateIds) {
		this.templateIds = templateIds;
	}

	public CCDACode getSeverity() {
		return severity;
	}

	public void setSeverity(CCDACode severity) {
		this.severity = severity;
	}

	public CCDAAllergySeverity()
	{
		
	}
}
