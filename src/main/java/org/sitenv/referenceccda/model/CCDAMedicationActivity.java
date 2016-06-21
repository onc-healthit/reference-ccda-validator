package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAMedicationActivity {

	private ArrayList<CCDAII>     				templateIds;
	private CCDAEffTime							duration;
	private CCDAEffTime							frequency;
	private CCDACode							routeCode;
	private CCDACode							approachSiteCode;
	private CCDAPQ								doseQuantity;
	private CCDAPQ								rateQuantity;
	private CCDACode							adminUnitCode;
	private CCDAConsumable						consumable;
	
	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> templateIds) {
		this.templateIds = templateIds;
	}

	public CCDAEffTime getDuration() {
		return duration;
	}

	public void setDuration(CCDAEffTime duration) {
		this.duration = duration;
	}

	public CCDAEffTime getFrequency() {
		return frequency;
	}

	public void setFrequency(CCDAEffTime frequency) {
		this.frequency = frequency;
	}

	public CCDACode getRouteCode() {
		return routeCode;
	}

	public void setRouteCode(CCDACode routeCode) {
		this.routeCode = routeCode;
	}

	public CCDACode getApproachSiteCode() {
		return approachSiteCode;
	}

	public void setApproachSiteCode(CCDACode approachSiteCode) {
		this.approachSiteCode = approachSiteCode;
	}

	public CCDAPQ getDoseQuantity() {
		return doseQuantity;
	}

	public void setDoseQuantity(CCDAPQ doseQuantity) {
		this.doseQuantity = doseQuantity;
	}

	public CCDAPQ getRateQuantity() {
		return rateQuantity;
	}

	public void setRateQuantity(CCDAPQ rateQuantity) {
		this.rateQuantity = rateQuantity;
	}

	public CCDACode getAdminUnitCode() {
		return adminUnitCode;
	}

	public void setAdminUnitCode(CCDACode adminUnitCode) {
		this.adminUnitCode = adminUnitCode;
	}

	public CCDAConsumable getConsumable() {
		return consumable;
	}

	public void setConsumable(CCDAConsumable consumable) {
		this.consumable = consumable;
	}

	CCDAMedicationActivity()
	{
		
	}
	
}
