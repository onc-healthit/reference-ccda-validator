package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAConsumable {
	
	private static Logger log = Logger.getLogger(CCDAConsumable.class.getName());

	ArrayList<CCDAII> 			templateIds;
	CCDACode					medcode;
	ArrayList<CCDACode>			translations;
	CCDADataElement				lotNumberText;
	CCDADataElement				manufacturingOrg;
	
	public void log() {
	
		if(medcode != null)
			log.info("Consumable Code = " + medcode.getCode());
		
		for(int l = 0; l < translations.size(); l++) {
			log.info("Consumable Translation Code = " + translations.get(l).getCode());
		}
		
		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}
		
		if(lotNumberText != null) {
			log.info(" Lot Number = " + lotNumberText.getValue());
		}
		
		if(manufacturingOrg != null) {
			log.info(" Manufacturing Org Name = " + manufacturingOrg.getValue());
		}
		
	}
	
	public ArrayList<CCDACode> getTranslations() {
		return translations;
	}

	public void setTranslations(ArrayList<CCDACode> ts) {
		
		if(ts != null)
			this.translations = ts;
	}

	public CCDADataElement getLotNumberText() {
		return lotNumberText;
	}

	public void setLotNumberText(CCDADataElement lotNumberText) {
		this.lotNumberText = lotNumberText;
	}

	public CCDADataElement getManufacturingOrg() {
		return manufacturingOrg;
	}

	public void setManufacturingOrg(CCDADataElement manufacturingOrg) {
		this.manufacturingOrg = manufacturingOrg;
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateIds = ids;
	}

	public CCDACode getMedcode() {
		return medcode;
	}

	public void setMedcode(CCDACode medcode) {
		this.medcode = medcode;
	}

	public CCDAConsumable()
	{
		templateIds = new ArrayList<CCDAII>();
		translations = new ArrayList<CCDACode>();
	}
}
