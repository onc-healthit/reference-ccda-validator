package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

public class CCDAAddress {
	
	private static Logger log = Logger.getLogger(CCDAPatient.class.getName());
	
	private CCDADataElement addressLine1;
	private CCDADataElement addressLine2;
	private CCDADataElement city;
	private CCDADataElement state;
	private CCDADataElement postalCode;
	private CCDADataElement country;
	private String postalAddressUse;
	
	public CCDAAddress(){}

	public void log() {
		
		log.info("*** Logging Addreess ****");
		log.info("Address Line 1 = " + (addressLine1==null ? "No Data" : addressLine1.getValue()));
		log.info("Address Line 2 = " + (addressLine2==null ? "No Data" : addressLine2.getValue()));
		log.info("City = " + (city==null ? "No Data" : city.getValue()));
		log.info("State = " + (state==null ? "No Data" : state.getValue()));
		log.info("Postal Code = " + (postalCode==null ? "No Data" : postalCode.getValue()));
		log.info("Country = " + (country==null ? "No Data" : country.getValue()));
		log.info("Postal Address Use = " + postalAddressUse);
	}
	
	public CCDADataElement getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(CCDADataElement addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public CCDADataElement getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(CCDADataElement addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public CCDADataElement getCity() {
		return city;
	}

	public void setCity(CCDADataElement city) {
		this.city = city;
	}

	public CCDADataElement getState() {
		return state;
	}

	public void setState(CCDADataElement state) {
		this.state = state;
	}

	public CCDADataElement getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(CCDADataElement postalCode) {
		this.postalCode = postalCode;
	}

	public CCDADataElement getCountry() {
		return country;
	}

	public void setCountry(CCDADataElement country) {
		this.country = country;
	}
	
	public String getPostalAddressUse() {
		return postalAddressUse;
	}

	public void setPostalAddressUse(String postalAddressUse) {
		this.postalAddressUse = postalAddressUse;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this) { 
			return true; 
		} 
		if (obj == null || obj.getClass() != this.getClass()) { 
			return false; 
		} 
		CCDAAddress obj2 = (CCDAAddress) obj; 
		return (this.addressLine1 == obj2.getAddressLine1() || (this.addressLine1 != null && this.addressLine1.equals(obj2.getAddressLine1()))) && 
				(this.addressLine2 == obj2.getAddressLine2() || (this.addressLine2 != null && this.addressLine2 .equals(obj2.getAddressLine2())))&&
				(this.city == obj2.getCity() || (this.city != null && this.city.equals(obj2.getCity()))) && 
				(this.state == obj2.getState() || (this.state != null && this.state .equals(obj2.getState()))) &&
				(this.country == obj2.getCountry() || (this.country != null && this.country.equals(obj2.getCountry()))) && 
				(this.postalCode == obj2.getPostalCode() || (this.postalCode != null && this.postalCode .equals(obj2.getPostalCode()))) &&
				(this.postalAddressUse == obj2.getPostalAddressUse() || (this.postalAddressUse != null && this.postalAddressUse .equals(obj2.getPostalAddressUse())));
	}
	
}
