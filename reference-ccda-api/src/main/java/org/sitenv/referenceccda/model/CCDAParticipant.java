package org.sitenv.referenceccda.model;

public class CCDAParticipant {
	
	private CCDADataElement firstName;
	private CCDADataElement lastName;
	private CCDADataElement middleName;
	private CCDAAddress     address;
	private CCDADataElement telecom;
	
	public CCDAParticipant()
	{
		
	}

	public CCDADataElement getFirstName() {
		return firstName;
	}

	public void setFirstName(CCDADataElement firstName) {
		this.firstName = firstName;
	}

	public CCDADataElement getLastName() {
		return lastName;
	}

	public void setLastName(CCDADataElement lastName) {
		this.lastName = lastName;
	}

	public CCDADataElement getMiddleName() {
		return middleName;
	}

	public void setMiddleName(CCDADataElement middleName) {
		this.middleName = middleName;
	}

	public CCDAAddress getAddress() {
		return address;
	}

	public void setAddress(CCDAAddress address) {
		this.address = address;
	}

	public CCDADataElement getTelecom() {
		return telecom;
	}

	public void setTelecom(CCDADataElement telecom) {
		this.telecom = telecom;
	}
}
