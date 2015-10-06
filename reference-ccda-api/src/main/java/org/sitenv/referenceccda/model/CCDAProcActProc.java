package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAProcActProc {
	private ArrayList<CCDAII>       		sectionTemplateId;
	private CCDACode                 		procCode;
	private CCDACode						procStatus;
	private CCDACode						targetSiteCode;
	private ArrayList<CCDAAssignedEntity>  	performer;
	private ArrayList<CCDAServiceDeliveryLoc>  	sdLocs;
	private CCDAII								piTemplateId;
	private CCDAII								udi;
	private CCDACode							deviceCode;
	private CCDAII								scopingEntityId;
	
	public CCDAProcActProc()
	{
		
	}

	public ArrayList<CCDAII> getSectionTemplateId() {
		return sectionTemplateId;
	}

	public void setSectionTemplateId(ArrayList<CCDAII> sectionTemplateId) {
		this.sectionTemplateId = sectionTemplateId;
	}

	public CCDACode getProcCode() {
		return procCode;
	}

	public void setProcCode(CCDACode procCode) {
		this.procCode = procCode;
	}

	public CCDACode getProcStatus() {
		return procStatus;
	}

	public void setProcStatus(CCDACode procStatus) {
		this.procStatus = procStatus;
	}

	public CCDACode getTargetSiteCode() {
		return targetSiteCode;
	}

	public void setTargetSiteCode(CCDACode targetSiteCode) {
		this.targetSiteCode = targetSiteCode;
	}

	public ArrayList<CCDAAssignedEntity> getPerformer() {
		return performer;
	}

	public void setPerformer(ArrayList<CCDAAssignedEntity> performer) {
		this.performer = performer;
	}

	public ArrayList<CCDAServiceDeliveryLoc> getSdLocs() {
		return sdLocs;
	}

	public void setSdLocs(ArrayList<CCDAServiceDeliveryLoc> sdLocs) {
		this.sdLocs = sdLocs;
	}

	public CCDAII getPiTemplateId() {
		return piTemplateId;
	}

	public void setPiTemplateId(CCDAII piTemplateId) {
		this.piTemplateId = piTemplateId;
	}

	public CCDAII getUdi() {
		return udi;
	}

	public void setUdi(CCDAII udi) {
		this.udi = udi;
	}

	public CCDACode getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(CCDACode deviceCode) {
		this.deviceCode = deviceCode;
	}

	public CCDAII getScopingEntityId() {
		return scopingEntityId;
	}

	public void setScopingEntityId(CCDAII scopingEntityId) {
		this.scopingEntityId = scopingEntityId;
	}
	
}
