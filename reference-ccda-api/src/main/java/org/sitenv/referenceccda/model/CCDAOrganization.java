package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CCDAOrganization {
	
	private static Logger log = Logger.getLogger(CCDAOrganization.class.getName());

	private ArrayList<CCDADataElement> 				names;
	private ArrayList<CCDADataElement>							telecom;
	private ArrayList<CCDAAddress>								address;
	
	public void log() {
		
		for(int j = 0; j < telecom.size(); j++) {
			log.info(" Telecom [" + j + "] = " + telecom.get(j).getValue());
		}
		
		for(int j = 0; j < names.size(); j++) {
			log.info(" Name [" + j + "] = " + names.get(j).getValue());
		}
		
		for(int k = 0; k < address.size(); k++) {
			address.get(k).log();
		}
		
		
	}
	
	public CCDAOrganization()
	{
		names = new ArrayList<CCDADataElement>();
		address = new ArrayList<CCDAAddress>();
		telecom = new ArrayList<CCDADataElement>();
	}

	public ArrayList<CCDADataElement> getNames() {
		return names;
	}

	public void setNames(ArrayList<CCDADataElement> n) {
		
		if(n != null)
			this.names = n;
	}

	public ArrayList<CCDADataElement> getTelecom() {
		return telecom;
	}

	public void setTelecom(ArrayList<CCDADataElement> tels) {
		
		if(tels != null)
			this.telecom = tels;
	}

	public ArrayList<CCDAAddress> getAddress() {
		return address;
	}

	public void setAddress(ArrayList<CCDAAddress> ads) {
		
		if(ads != null)
		this.address = ads;
	}
}
