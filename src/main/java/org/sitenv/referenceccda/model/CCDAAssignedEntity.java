package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAAssignedEntity {
	
	private static Logger log = Logger.getLogger(CCDAAssignedEntity.class.getName());

	private ArrayList<CCDADataElement>			telecom;
	private ArrayList<CCDAAddress>			addresses;
	private CCDAOrganization					organization;
	
	public void log() {
		
		for(int j = 0; j < telecom.size(); j++) {
			log.info(" Telecom [" + j + "] = " + telecom.get(j).getValue());
		}
		
		for(int k = 0; k < addresses.size(); k++) {
			addresses.get(k).log();
		}
		
		if(organization != null)
			organization.log();
	}
	
	public ArrayList<CCDADataElement> getTelecom() {
		return telecom;
	}

	public void setTelecom(ArrayList<CCDADataElement> tels) {
		
		if(tels != null)
			this.telecom = tels;
	}

	public ArrayList<CCDAAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<CCDAAddress> addrs) {
		
		if(addrs != null)
			this.addresses = addrs;
	}

	public CCDAOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(CCDAOrganization organization) {
		this.organization = organization;
	}

	public CCDAAssignedEntity()
	{
		telecom = new ArrayList<CCDADataElement>();
		addresses = new ArrayList<CCDAAddress>();
		
	}
}
