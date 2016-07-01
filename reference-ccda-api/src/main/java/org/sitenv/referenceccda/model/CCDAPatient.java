package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;

public class CCDAPatient {
	
	private static Logger log = Logger.getLogger(CCDAPatient.class.getName());
	
	private CCDADataElement firstName;
	private CCDADataElement lastName;
	private CCDADataElement middleName;
	private CCDADataElement previousName;
	private CCDADataElement suffix;
	private CCDADataElement dob;
	private ArrayList<CCDAAddress> addresses;
	private CCDAAddress     birthPlace;
	private ArrayList<CCDACode> raceCodes;
	private ArrayList<CCDACode> raceCodeExt;
	private CCDACode ethnicity;
	private CCDACode sex;
	private ArrayList<CCDADataElement> telecom;
	private CCDACode                   adminGender;
	private CCDACode                   maritalStatus;
	private CCDACode                   religiousAffiliation;
	private ArrayList<CCDAPreferredLanguage>          languageCommunication;
	
	public void log() {
		
		log.info("*** Logging Patient Details ****");
		log.info("First Name = " + (firstName==null ? "No Data" : firstName.getValue()));
		log.info("Middle Name = " + (middleName==null ? "No Data" : middleName.getValue()));
		log.info("Last Name = " + (lastName==null ? "No Data" : lastName.getValue()));
		log.info("Previous Name = " + (previousName==null ? "No Data" : previousName.getValue()));
		log.info("Suffix = " + (suffix==null ? "No Data" : suffix.getValue()));
		log.info("Date of Birth " + (dob==null ? "No Data" : dob.getValue()));
		
		for(int i = 0; i < addresses.size(); i++) {
			addresses.get(i).log();
		}
		
		if(birthPlace != null)
		{
			log.info("Birth Place Address ");
			birthPlace.log();
		}
		else
		{
			log.info("Birth Place Address : No Data");
		}
		
		for(int j = 0; j < raceCodes.size(); j++) {
			log.info(" Race Code [" + j + "] = " + raceCodes.get(j).getCode());
		}
		
		for(int k = 0; k < raceCodeExt.size(); k++) {
			log.info(" Race Code Ext [" + k + "] = " + raceCodeExt.get(k).getCode());
		}
		
		if(ethnicity != null) {
			log.info(" Ethnicity : " + ethnicity.getCode());
		}
		
		if(sex != null) {
			log.info(" Sex : " + sex.getValue());
		}
		
		for(int l = 0; l < telecom.size(); l++) {
			log.info(" Telecom [" + l + "] = " + telecom.get(l).getValue());
		}
		
		if(adminGender != null) {
			log.info(" Admin Gender " + adminGender.getCode());
		}
		
		if(maritalStatus != null) {
			log.info(" Marital Status " + maritalStatus.getCode());
		}
		
		if(religiousAffiliation != null) {
			log.info(" Religious Affiliation " + religiousAffiliation.getCode());
		}
		
		for(int m = 0; m < languageCommunication.size(); m++) {
			log.info(" Language Communication [" + m + "] = " );
			languageCommunication.get(m).log();
		}
	}
	
	public CCDACode getReligiousAffiliation() {
		return religiousAffiliation;
	}

	public void setReligiousAffiliation(CCDACode religiousAffiliation) {
		this.religiousAffiliation = religiousAffiliation;
	}

	public CCDACode getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(CCDACode maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public ArrayList<CCDADataElement> getTelecom() {
		return telecom;
	}

	public void setTelecom(ArrayList<CCDADataElement> tels) {
		if(tels != null)
			this.telecom = tels;
	}

	public CCDACode getAdminGender() {
		return adminGender;
	}

	public void setAdminGender(CCDACode adminGender) {
		this.adminGender = adminGender;
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

	public void setAddresses(ArrayList<CCDAAddress> addr) {
		if(addr != null)
			this.addresses = addr;
	}

	public ArrayList<CCDAPreferredLanguage> getPreferredLanguage() {
		return languageCommunication;
	}

	public void addPreferredLanguage(CCDAPreferredLanguage language) {
		this.languageCommunication.add(language);
	}

	public ArrayList<CCDACode> getRaceCodes() {
		return raceCodes;
	}

	public ArrayList<CCDACode> getRaceCodeExt() {
		return raceCodeExt;
	}

	public void setRaceCodeExt(ArrayList<CCDACode> rext) {
		
		if(rext != null)
			this.raceCodeExt = rext;
	}

	public void setRaceCodes(ArrayList<CCDACode> rc) {
		
		if(rc != null)
			this.raceCodes = rc;
	}
	
	public void addRaceCode(CCDACode raceCode) {
		raceCodes.add(raceCode);
	}
	
	public void addRaceCodeExt(CCDACode raceCode) {
		raceCodeExt.add(raceCode);
	}

	public CCDACode getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(CCDACode ethnicity) {
		this.ethnicity = ethnicity;
	}

	public CCDACode getSex() {
		return sex;
	}

	public void setSex(CCDACode sex) {
		this.sex = sex;
	}

	public CCDAPatient()
	{
		addresses = new ArrayList<CCDAAddress>();
		raceCodes = new ArrayList<CCDACode>();
		raceCodeExt = new ArrayList<CCDACode>();
		telecom = new ArrayList<CCDADataElement>();
		languageCommunication = new ArrayList<CCDAPreferredLanguage>();
	}

	public ArrayList<CCDAPreferredLanguage> getLanguageCommunication() {
		return languageCommunication;
	}

	public void setLanguageCommunication(ArrayList<CCDAPreferredLanguage> lc) {
		
		if(lc != null)
			this.languageCommunication = lc;
	}

	public void setBirthPlace(CCDAAddress add) {
		birthPlace = add;
	}
	
	public CCDAAddress getBirthPlace() { 
		return birthPlace;
	}
	
	public Boolean containsRaceCode(CCDACode code) {
		
		if (raceCodes == null)
			return false;
		
		for(CCDACode c : raceCodes) {
			if( (c.getCode().equalsIgnoreCase(code.getCode())) && 
				(c.getCodeSystem().equalsIgnoreCase(code.getCodeSystem())))
				return true;
		}
				
		return false;
		
		
	}

	public Boolean containsRaceCodeExt(CCDACode code) {
		
		if (raceCodeExt == null)
			return false;
		
		for(CCDACode c : raceCodeExt) {
			if( (c.getCode().equalsIgnoreCase(code.getCode())) && 
				(c.getCodeSystem().equalsIgnoreCase(code.getCodeSystem())))
				return true;
		}
				
		return false;
		
	}
	
	
	public Boolean containsLanguage(CCDAPreferredLanguage lang) {
		
		if (languageCommunication == null)
			return false;
		
		String[] language = lang.getLanguageCode().getCode().split("-");
		String langPart = null;
		if(language.length > 0)
			langPart = language[0];
		
		for(CCDAPreferredLanguage l : languageCommunication) {
			if(l.getLanguageCode().getCode().contains(langPart))
				return true;
		}
		
		
		return false;
		
		
	}

	public void compare(CCDAPatient patient, ArrayList<RefCCDAValidationResult> results) {
	
		compareNames(patient, results);
		compareMiscellaneous(patient, results);
		compareRaceAndEthnicity(patient, results);
	}
	
	private void compareRaceAndEthnicity(CCDAPatient patient, ArrayList<RefCCDAValidationResult> results) {
		
		// Compare Race Code
		for(CCDACode c : raceCodes) {
			if(!patient.containsRaceCode(c))
			{
				String errorMessage = "Patient Race Code = " + c.getCode() 
						+ " expected but, submitted file does not contain the expected race code";
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}
		}

		// Compare Race Code Ext
		for(CCDACode c : raceCodeExt) {
			if(!patient.containsRaceCodeExt(c))
			{
				String errorMessage = "Patient Granular Race Code = " + c.getCode() 
						+ " expected in sdtc:raceCode extension but, submitted file does not contain the expected granular race code";
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}
		}
		
		// Compare Ethnicity
		if((ethnicity != null) && (patient.getEthnicity() != null) ) {
			if( !(ethnicity.getCode().equalsIgnoreCase(patient.getEthnicity().getCode())))
			{
				String errorMessage = "Patient Ethnicity code Expected = " + ethnicity.getCode() 
				+ " but, submitted file contains ethnicity code of " + patient.getEthnicity().getCode();
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}					
		}
		else if( (ethnicity == null) && (patient.getEthnicity() != null)) {
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario does not require patient ethnicity information, but submitted file does have patient ethnicity information", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if( (ethnicity != null) && (patient.getEthnicity() == null)){
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario requires patient ethnicity information, but submitted file does not have patient ethnicitiy information", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		
		
	}

	private void compareMiscellaneous(CCDAPatient patient, ArrayList<RefCCDAValidationResult> results) {
		
		// Compare date of birth
		if((dob != null) && (patient.getDob() != null) ) {
			if( !(dob.getValue().equalsIgnoreCase(patient.getDob().getValue())))
			{
				String errorMessage = "Patient Date of Birth Expected = " + dob.getValue() 
				+ " but, submitted file contains Date of Birth of " + patient.getDob().getValue();
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}					
		}
		else if( (dob == null) && (patient.getDob() != null)) {
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario does not require patient date of birth information, but submitted file does have patient date of birth information", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if( (dob != null) && (patient.getDob() == null)){
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario requires patient date of birth information, but submitted file does not have patient date of birth information", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		
		// Add Address
		// Add Telecom
		
		// Compare Preferred Language
		for(CCDAPreferredLanguage lang : languageCommunication) {
			if(!patient.containsLanguage(lang))
			{
				String errorMessage = "Patient Language = " + lang.getLanguageCode().getCode() 
				+ " expected but, submitted file does not contain the expected language code";
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}
		}
		
	}

	private void compareNames(CCDAPatient patient, ArrayList<RefCCDAValidationResult> results) {
		
		// Compare First Name
		if((firstName != null) && (patient.getFirstName() != null) ) {
			if( !(firstName.getValue().equalsIgnoreCase(patient.getFirstName().getValue())))
			{
				String errorMessage = "Patient First Name Expected = " + firstName.getValue() 
					+ " but, submitted file contains first name of " + patient.getFirstName().getValue();
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}					
		}
		else if( (firstName == null) && (patient.getFirstName() != null)) {
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario does not require patient first name, but submitted file does have patient first name", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if( (firstName != null) && (patient.getFirstName() == null)){
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario requires patient first name, but submitted file does not have patient first name", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		
		// Compare Last Name
		if((lastName != null) && (patient.getLastName() != null) ) {
			if( !(lastName.getValue().equalsIgnoreCase(patient.getLastName().getValue())))
			{
				String errorMessage = "Patient Last Name Expected = " + lastName.getValue() 
					+ " but, submitted file contains last name of " + patient.getLastName().getValue();
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}					
		}
		else if( (lastName == null) && (patient.getLastName() != null)) {
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario does not require patient last name, but submitted file does have patient last name", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if( (lastName != null) && (patient.getLastName() == null)){
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario requires patient last name, but submitted file does not have patient last name", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		
		// Compare Middle Name
		if((middleName != null) && (patient.getMiddleName() != null) ) {
			if( !(middleName.getValue().equalsIgnoreCase(patient.getMiddleName().getValue())))
			{
				String errorMessage = "Patient Middle Name Expected = " + middleName.getValue() 
				+ " but, submitted file contains middle name of " + patient.getMiddleName().getValue();
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}					
		}
		else if( (middleName == null) && (patient.getMiddleName() != null)) {
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario does not require patient middle name, but submitted file does have patient middle name", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if( (middleName != null) && (patient.getMiddleName() == null)){
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario requires patient middle name, but submitted file does not have patient middle name", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		
		// Compare Previous Name
		if((previousName != null) && (patient.getPreviousName() != null) ) {
			if( !(previousName.getValue().equalsIgnoreCase(patient.getPreviousName().getValue())))
			{
				String errorMessage = "Patient Previous Name Expected = " + previousName.getValue() 
				+ " but, submitted file contains previous name of " + patient.getPreviousName().getValue();
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}					
		}
		else if( (previousName == null) && (patient.getPreviousName() != null)) {
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario does not require patient previous name, but submitted file does have patient previous name", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if( (previousName != null) && (patient.getPreviousName() == null)){
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario requires patient previous name, but submitted file does not have patient previous name", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		
		// Compare Suffix
		if((suffix != null) && (patient.getSuffix() != null) ) {
			if( !(suffix.getValue().equalsIgnoreCase(patient.getSuffix().getValue())))
			{
				String errorMessage = "Patient Suffix Expected = " + suffix.getValue() 
				+ " but, submitted file contains suffix of " + patient.getSuffix().getValue();
				RefCCDAValidationResult rs = new RefCCDAValidationResult(errorMessage, ValidationResultType.REF_CCDA_ERROR, 
						"/ClinicalDocument", "0");
				results.add(rs);
			}					
		}
		else if( (suffix == null) && (patient.getSuffix() != null)) {
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario does not require patient suffix, but submitted file does have patient sufix", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
		else if( (suffix != null) && (patient.getSuffix() == null)){
			RefCCDAValidationResult rs = new RefCCDAValidationResult("The scenario requires patient suffix, but submitted file does not have patient suffix", ValidationResultType.REF_CCDA_ERROR, "/ClinicalDocument", "0" );
			results.add(rs);
		}
	}
}
