package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAServiceDeliveryLoc {

	private ArrayList<CCDAII>           templateId;
	private CCDACode                    locationCode;
	private ArrayList<CCDAAddress>      address;
	private ArrayList<CCDADataElement>  telecom;
	private CCDADataElement             name;
	
	public ArrayList<CCDAII> getTemplateId() {
		return templateId;
	}

	public void setTemplateId(ArrayList<CCDAII> templateId) {
		this.templateId = templateId;
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

	public void setAddress(ArrayList<CCDAAddress> address) {
		this.address = address;
	}

	public ArrayList<CCDADataElement> getTelecom() {
		return telecom;
	}

	public void setTelecom(ArrayList<CCDADataElement> telecom) {
		this.telecom = telecom;
	}

	public CCDADataElement getName() {
		return name;
	}

	public void setName(CCDADataElement name) {
		this.name = name;
	}

	public CCDAServiceDeliveryLoc()
	{
		
	}
}
