package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.validators.content.CareTeamMemberParser;

public class CCDAParticipant {
	
	private static Logger log = Logger.getLogger(CCDAParticipant.class.getName());
	
	private CCDADataElement firstName;
	private CCDADataElement lastName;
	private CCDADataElement middleName;
	private CCDAAddress     address;
	private CCDADataElement telecom;
	
	public void log()
	{
		log.info(" First Name = " + ((firstName==null)?"No Data":firstName.getValue()));
		log.info(" Last Name = " + ((lastName==null)?"No Data":lastName.getValue()));
		log.info(" Middle Name = " + ((middleName==null)?"No Data":middleName.getValue()));
		
		if(address != null)
			address.log();
		
		log.info(" Telecom = " + ((telecom==null)?"No Data":telecom.getValue()));
		
	}
	
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

	public void setPreviousName(CCDADataElement readTextContext) {
		// No need for the data element.
		
	}

	public void setSuffix(CCDADataElement readTextContext) {
		//No need for the data element.
		
	}
}
