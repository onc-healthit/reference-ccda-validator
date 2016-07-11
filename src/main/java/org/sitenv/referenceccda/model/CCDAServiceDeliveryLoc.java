package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAServiceDeliveryLoc {
	
	private static Logger log = Logger.getLogger(CCDAServiceDeliveryLoc.class.getName());

	private ArrayList<CCDAII>           templateId;
	private CCDACode                    locationCode;
	private ArrayList<CCDAAddress>      address;
	private ArrayList<CCDADataElement>  telecom;
	private CCDADataElement             name;
	
	public void log() {
		
		log.info("*** Service Delivery Location ***");
				
		for(int j = 0; j < templateId.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateId.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateId.get(j).getExtValue());
		}
	
		if(locationCode != null)
			log.info("Location Code = " + locationCode.getCode());
		
		for(int i = 0; i < address.size(); i++) {
			address.get(i).log();
		}
		
		for(int l = 0; l < telecom.size(); l++) {
			log.info(" Telecom [" + l + "] = " + telecom.get(l).getValue());
		}
		
		if(name != null)
			log.info(" Name = " + name.getValue());
		
	}
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}

	public void setTemplateId(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateId = ids;
	}

	public CCDACode getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(CCDACode locationCode) {
		this.locationCode = locationCode;
	}

	public ArrayList<CCDAAddress> getAddress() {
		return address;
	}

	public void setAddress(ArrayList<CCDAAddress> addr) {
		
		if(addr != null)
			this.address = addr;
	}

	public ArrayList<CCDADataElement> getTelecom() {
		return telecom;
	}

	public void setTelecom(ArrayList<CCDADataElement> tels) {
		
		if( tels != null)
			this.telecom = tels;
	}

	public CCDADataElement getName() {
		return name;
	}

	public void setName(CCDADataElement name) {
		this.name = name;
	}

	public CCDAServiceDeliveryLoc()
	{
		templateId = new ArrayList<CCDAII>();
		telecom = new ArrayList<CCDADataElement>();
		address = new ArrayList<CCDAAddress>();
	}
}
