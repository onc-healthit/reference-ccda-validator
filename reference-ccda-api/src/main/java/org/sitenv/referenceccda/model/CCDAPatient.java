package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAPatient {
	private CCDADataElement firstName;
	private CCDADataElement lastName;
	private CCDADataElement middleName;
	private CCDADataElement previousName;
	private CCDADataElement suffix;
	private CCDADataElement dob;
	private ArrayList<CCDAAddress> addresses;
	private CCDADataElement language;
	private ArrayList<CCDADataElement> raceCodes;
	private CCDADataElement ethnicity;
	private CCDADataElement sex;
	private ArrayList<CCDADataElement> telecom;
	
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

	public CCDADataElement getPreviousName() {
		return previousName;
	}

	public void setPreviousName(CCDADataElement previousName) {
		this.previousName = previousName;
	}

	public CCDADataElement getSuffix() {
		return suffix;
	}

	public void setSuffix(CCDADataElement suffix) {
		this.suffix = suffix;
	}

	public CCDADataElement getDob() {
		return dob;
	}

	public void setDob(CCDADataElement dob) {
		this.dob = dob;
	}

	public ArrayList<CCDAAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<CCDAAddress> addresses) {
		this.addresses = addresses;
	}

	public CCDADataElement getLanguage() {
		return language;
	}

	public void setLanguage(CCDADataElement language) {
		this.language = language;
	}

	public ArrayList<CCDADataElement> getRaceCodes() {
		return raceCodes;
	}

	public void setRaceCodes(ArrayList<CCDADataElement> raceCodes) {
		this.raceCodes = raceCodes;
	}

	public CCDADataElement getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(CCDADataElement ethnicity) {
		this.ethnicity = ethnicity;
	}

	public CCDADataElement getSex() {
		return sex;
	}

	public void setSex(CCDADataElement sex) {
		this.sex = sex;
	}

	public CCDAPatient()
	{
		addresses = new ArrayList<CCDAAddress>();
		raceCodes = new ArrayList<CCDADataElement>();
	}
}
