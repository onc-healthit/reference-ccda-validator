package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAOrganization {

	private ArrayList<CCDADataElement> 				names;
	private CCDADataElement							telecom;
	private CCDAAddress								address;
	
	public CCDAOrganization()
	{
		
	}

	public ArrayList<CCDADataElement> getNames() {
		return names;
	}

	public void setNames(ArrayList<CCDADataElement> names) {
		this.names = names;
	}

	public CCDADataElement getTelecom() {
		return telecom;
	}

	public void setTelecom(CCDADataElement telecom) {
		this.telecom = telecom;
	}

	public CCDAAddress getAddress() {
		return address;
	}

	public void setAddress(CCDAAddress address) {
		this.address = address;
	}
}
