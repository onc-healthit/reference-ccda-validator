package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDAProcActProc {
	
	private static Logger log = Logger.getLogger(CCDAProcActProc.class.getName());

	private ArrayList<CCDAII>       		sectionTemplateId;
	private CCDACode                 		procCode;
	private CCDACode						procStatus;
	private CCDACode						targetSiteCode;
	private ArrayList<CCDAAssignedEntity>  	performer;
	private ArrayList<CCDAServiceDeliveryLoc>  	sdLocs;
	private CCDAII								piTemplateId;
	private ArrayList<CCDAUDI>								udi;
	private CCDACode							deviceCode;
	private CCDAII								scopingEntityId;
	
	public void log() {
		
		if(procCode != null)
			log.info(" Procedure Code = " + procCode.getCode());
		
		if(procStatus != null)
			log.info(" Procedure status = " + procStatus.getCode());
		
		for(int j = 0; j < sectionTemplateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + sectionTemplateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + sectionTemplateId.get(j).getExtValue());
		}
		
		if(targetSiteCode != null)
			log.info(" Target Site Code = " + targetSiteCode.getCode());
		
		if(deviceCode != null)
			log.info(" Device Code = " + deviceCode.getCode());
		
		if(piTemplateId != null)
			log.info(" Tempalte Id = " + piTemplateId.getRootValue());
		
		if(scopingEntityId != null)
			log.info(" Scoping Entity Id = " + scopingEntityId.getRootValue());
		
		for(int k = 0; k < performer.size(); k++) {
			performer.get(k).log();
		}
		
		for(int l = 0; l < sdLocs.size(); l++) {
			sdLocs.get(l).log();
		}
		
		for(int m = 0; m < udi.size(); m++) {
			udi.get(m).log();
		}
	}
	
	public CCDAProcActProc()
	{
		sectionTemplateId = new ArrayList<CCDAII>();
		performer = new ArrayList<CCDAAssignedEntity>();
		sdLocs = new ArrayList<CCDAServiceDeliveryLoc>();
		udi = new ArrayList<CCDAUDI>();
	}

	public ArrayList<CCDAII> getSectionTemplateId() {
		return sectionTemplateId;
	}

	public void setSectionTemplateId(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.sectionTemplateId = ids;
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

	public void setPerformer(ArrayList<CCDAAssignedEntity> ps) {
		
		if(ps != null)
			this.performer = ps;
	}

	public ArrayList<CCDAServiceDeliveryLoc> getSdLocs() {
		return sdLocs;
	}

	public void setSdLocs(ArrayList<CCDAServiceDeliveryLoc> sdl) {
		
		if(sdl != null)
		this.sdLocs = sdl;
	}

	public CCDAII getPiTemplateId() {
		return piTemplateId;
	}

	public void setPiTemplateId(CCDAII piTemplateId) {
		this.piTemplateId = piTemplateId;
	}

	public ArrayList<CCDAUDI> getUdi() {
		return udi;
	}

	public void setUdi(ArrayList<CCDAUDI> udis) {
		
		if(udis != null)
			this.udi = udis;
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

	public void setPatientUDI(ArrayList<CCDAUDI> udis) {
		
		if(udis != null)
			this.udi = udis;
		
	}
	
}
