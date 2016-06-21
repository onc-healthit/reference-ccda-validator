package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAConsumable {

	ArrayList<CCDAII> 			templateIds;
	CCDACode					medcode;
	ArrayList<CCDACode>			translations;
	CCDADataElement				lotNumberText;
	CCDADataElement				manufacturingOrg;
	
	public ArrayList<CCDACode> getTranslations() {
		return translations;
	}

	public void setTranslations(ArrayList<CCDACode> translations) {
		this.translations = translations;
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

	public void setTemplateIds(ArrayList<CCDAII> templateIds) {
		this.templateIds = templateIds;
	}

	public CCDACode getMedcode() {
		return medcode;
	}

	public void setMedcode(CCDACode medcode) {
		this.medcode = medcode;
	}

	public CCDAConsumable()
	{

	}
}
