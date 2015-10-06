package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAAddress {
	
	private CCDADataElement addressLine1;
	private CCDADataElement addressLine2;
	private CCDADataElement city;
	private CCDADataElement state;
	private CCDADataElement postalCode;
	private CCDADataElement country;
	
	public CCDAAddress(){}

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
	
}
