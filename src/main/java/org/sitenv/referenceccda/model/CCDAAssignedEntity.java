package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAAssignedEntity {

	private ArrayList<CCDADataElement>			telecom;
	private ArrayList<CCDADataElement>			addresses;
	private CCDAOrganization					organization;
	
	public ArrayList<CCDADataElement> getTelecom() {
		return telecom;
	}

	public void setTelecom(ArrayList<CCDADataElement> telecom) {
		this.telecom = telecom;
	}

	public ArrayList<CCDADataElement> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<CCDADataElement> addresses) {
		this.addresses = addresses;
	}

	public CCDAOrganization getOrganization() {
		return organization;
	}

	public void setOrganization(CCDAOrganization organization) {
		this.organization = organization;
	}

	public CCDAAssignedEntity()
	{
		
	}
}
