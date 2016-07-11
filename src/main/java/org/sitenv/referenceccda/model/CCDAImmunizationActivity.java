package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAImmunizationActivity {
	
	private static Logger log = Logger.getLogger(CCDAImmunizationActivity.class.getName());
	
	private ArrayList<CCDAII>     				templateIds;
	private CCDAEffTime							time;
	private CCDACode							routeCode;
	private CCDACode							approachSiteCode;
	private CCDAPQ								doseQuantity;
	private CCDACode							adminUnitCode;
	private CCDAConsumable						consumable;
	private CCDAOrganization					organization;
	
	public void log() {
		
		
		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}
		
		if(routeCode != null)
			log.info("Medication Activity Route Code = " + routeCode.getCode());
		
		if(approachSiteCode != null)
			log.info("Medication Activity Approach Site Code = " + approachSiteCode.getCode());
		
		if(adminUnitCode != null)
			log.info("Medication Activity Admin Unit Code = " + adminUnitCode.getCode());
		
		if(time != null) {
			time.log();
		}
		
		if(doseQuantity != null){
			doseQuantity.log();
		}
		
		if(consumable != null) {
			consumable.log();
		}
		
		if(organization != null) {
			organization.log();
		}
	}
	
	public CCDAOrganization getOrganization() {
		return organization;
	}

	public CCDAImmunizationActivity()
	{
		templateIds = new ArrayList<CCDAII>();
	}

	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateIds = ids;
	}

	public CCDAEffTime getTime() {
		return time;
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

	public void setTime(CCDAEffTime t) {
		
		time = t;
	}

	public void setOrganization(CCDAOrganization representedOrg) {
		organization = representedOrg;
	}
}
