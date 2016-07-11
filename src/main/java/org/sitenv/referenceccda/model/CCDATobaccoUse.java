package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDATobaccoUse {
	
	private static Logger log = Logger.getLogger(CCDATobaccoUse.class.getName());
	
	private ArrayList<CCDAII>					tobaccoUseTemplateIds;
	private CCDACode							tobaccoUseSectionCode;
	private CCDACode							tobaccoUseCode;
	private CCDAEffTime							tobaccoUseTime;
	
	public void log() {
		
		if(tobaccoUseSectionCode != null)
			log.info(" Tobacco Use Section Code = " + tobaccoUseSectionCode.getCode());
		
		if(tobaccoUseCode != null)
			log.info(" Tobacco Use Code = " + tobaccoUseCode.getCode());
		
		for(int j = 0; j < tobaccoUseTemplateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + tobaccoUseTemplateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + tobaccoUseTemplateIds.get(j).getExtValue());
		}
		
		if(tobaccoUseTime != null)
			tobaccoUseTime.log();
	}
	
	public ArrayList<CCDAII> getTobaccoUseTemplateIds() {
		return tobaccoUseTemplateIds;
	}

	public void setTobaccoUseTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.tobaccoUseTemplateIds = ids;
	}

	public CCDACode getTobaccoUseSectionCode() {
		return tobaccoUseSectionCode;
	}

	public void setTobaccoUseSectionCode(CCDACode tobaccoUseSectionCode) {
		this.tobaccoUseSectionCode = tobaccoUseSectionCode;
	}

	public CCDACode getTobaccoUseCode() {
		return tobaccoUseCode;
	}

	public void setTobaccoUseCode(CCDACode tobaccoUseCode) {
		this.tobaccoUseCode = tobaccoUseCode;
	}

	public CCDAEffTime getTobaccoUseTime() {
		return tobaccoUseTime;
	}

	public void setTobaccoUseTime(CCDAEffTime tobaccoUseTime) {
		this.tobaccoUseTime = tobaccoUseTime;
	}

	public CCDATobaccoUse()
	{
		tobaccoUseTemplateIds = new ArrayList<CCDAII>();
	}
}
