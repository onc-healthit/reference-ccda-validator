package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAUDI {
	
	private static Logger log = Logger.getLogger(CCDAUDI.class.getName());
	
	private ArrayList<CCDAII>     templateIds;
	private ArrayList<CCDAII>     UDIValue;
	private CCDACode deviceCode;
	private ArrayList<CCDAII> scopingEntityId;
	
	public CCDAUDI() {
		templateIds = new ArrayList<CCDAII>();
		UDIValue = new ArrayList<CCDAII>();
		scopingEntityId = new ArrayList<CCDAII>();
	}
	
	public void log() { 
		
		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}
		
		for(int k = 0; k < UDIValue.size(); k++) {
			log.info(" Tempalte Id [" + k + "] = " + UDIValue.get(k).getRootValue());
			log.info(" Tempalte Id Ext [" + k + "] = " + UDIValue.get(k).getExtValue());
		}
		
		for(int l = 0; l < scopingEntityId.size(); l++) {
			log.info(" Tempalte Id [" + l + "] = " + scopingEntityId.get(l).getRootValue());
			log.info(" Tempalte Id Ext [" + l + "] = " + scopingEntityId.get(l).getExtValue());
		}
		
		if(deviceCode != null)
			log.info(" Device Code = " + deviceCode.getCode());
		
	}
	
	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}
	public void setTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateIds = ids;
	}
	public CCDACode getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(CCDACode deviceCode) {
		this.deviceCode = deviceCode;
	}
	public ArrayList<CCDAII> getUDIValue() {
		return UDIValue;
	}
	public void setUDIValue(ArrayList<CCDAII> udis) {
		
		if(udis != null)
			UDIValue = udis;
	}
	public ArrayList<CCDAII> getScopingEntityId() {
		return scopingEntityId;
	}
	public void setScopingEntityId(ArrayList<CCDAII> sids) {
		
		if(sids != null)
			this.scopingEntityId = sids;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((UDIValue == null) ? 0 : UDIValue.hashCode());
		result = prime * result
				+ ((deviceCode == null) ? 0 : deviceCode.hashCode());
		result = prime * result
				+ ((scopingEntityId == null) ? 0 : scopingEntityId.hashCode());
		result = prime * result
				+ ((templateIds == null) ? 0 : templateIds.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CCDAUDI other = (CCDAUDI) obj;
		if (UDIValue == null) {
			if (other.UDIValue != null)
				return false;
		} else if (!UDIValue.equals(other.UDIValue))
			return false;
		if (deviceCode == null) {
			if (other.deviceCode != null)
				return false;
		} else if (!deviceCode.equals(other.deviceCode))
			return false;
		if (scopingEntityId == null) {
			if (other.scopingEntityId != null)
				return false;
		} else if (!scopingEntityId.equals(other.scopingEntityId))
			return false;
		if (templateIds == null) {
			if (other.templateIds != null)
				return false;
		} else if (!templateIds.equals(other.templateIds))
			return false;
		return true;
	}
	
	
}
