package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAImmunizationActivity {
	private ArrayList<CCDAII>     				templateIds;
	private CCDAEffTime							time;
	private CCDACode							routeCode;
	private CCDACode							approachSiteCode;
	private CCDAPQ								doseQuantity;
	private CCDACode							adminUnitCode;
	private CCDAConsumable						consumable;
	
	public CCDAImmunizationActivity()
	{
		
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> templateIds) {
		this.templateIds = templateIds;
	}

	public CCDAEffTime getTime() {
		return time;
	}

	public void setTime(CCDAEffTime time) {
		this.time = time;
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
}
