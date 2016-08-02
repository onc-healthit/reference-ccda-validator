package org.sitenv.referenceccda.validators.content;

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.sitenv.referenceccda.model.CCDARefModel;

public class CCDAConstants {

    private final static CCDAConstants constants = new CCDAConstants(); 
    
	static public XPath CCDAXPATH;
	static public XPathExpression PATIENT_ROLE_EXP;
	static public XPathExpression REL_ADDR_EXP;
	static public XPathExpression REL_STREET_ADDR1_EXP;
	static public XPathExpression REL_STREET_ADDR2_EXP;
	static public XPathExpression REL_CITY_EXP;
	static public XPathExpression REL_STATE_EXP;
	static public XPathExpression REL_POSTAL_EXP;
	static public XPathExpression REL_COUNTRY_EXP;
	static public XPathExpression REL_PATIENT_BIRTHPLACE_EXP;
	static public XPathExpression REL_PATIENT_NAME_EXP;
	static public XPathExpression REL_PATIENT_PREV_NAME_EXP;
	static public XPathExpression REL_PLAY_ENTITY_NAME_EXP;
	static public XPathExpression REL_GIVEN_NAME_EXP;
	static public XPathExpression REL_MIDDLE_NAME_EXP;
	static public XPathExpression REL_FAMILY_NAME_EXP;
	static public XPathExpression REL_GIVEN_PREV_NAME_EXP;
	static public XPathExpression REL_SUFFIX_EXP;
	static public XPathExpression REL_PATIENT_ADMINGEN_EXP;
	static public XPathExpression REL_PATIENT_BIRTHTIME_EXP;
	static public XPathExpression REL_PATIENT_MARITAL_EXP;
	static public XPathExpression REL_PATIENT_RELIGION_EXP;
	static public XPathExpression REL_PATIENT_RACE_EXP;
	static public XPathExpression REL_PATIENT_ETHNICITY_EXP;
	static public XPathExpression REL_PATIENT_LANGUAGE_EXP;
	static public XPathExpression REL_TELECOM_EXP;
	static public XPathExpression REL_LANG_CODE_EXP;
	static public XPathExpression REL_LANG_MODE_EXP;
	static public XPathExpression REL_LANG_PREF_EXP;

	static public XPathExpression REL_TEMPLATE_ID_EXP;
	static public XPathExpression REL_CODE_EXP;
	static public XPathExpression REL_CODE_TRANS_EXP;
	static public XPathExpression REL_VAL_EXP;
	static public XPathExpression REL_STATUS_CODE_EXP;
	static public XPathExpression REL_INT_CODE_EXP;
	static public XPathExpression REL_REF_RANGE_EXP;
	
	static public XPathExpression REL_EFF_TIME_EXP;
	static public XPathExpression REL_EFF_TIME_LOW_EXP;
	static public XPathExpression REL_EFF_TIME_HIGH_EXP;
	static public XPathExpression REL_LOW_EXP;
	static public XPathExpression REL_HIGH_EXP;
	
	//Encounter stuff
	static public XPathExpression REL_ENC_ENTRY_EXP;
	static public XPathExpression ENCOUNTER_EXPRESSION;
	
	//Problem Stuff
	static public XPathExpression PROBLEM_EXPRESSION; 
	static public XPathExpression REL_PROBLEM_OBS_EXPRESSION;
	
	//Medication stuff
	static public XPathExpression MEDICATION_EXPRESSION;
	static public XPathExpression REL_MED_ENTRY_EXP;
	static public XPathExpression REL_ROUTE_CODE_EXP;
	static public XPathExpression REL_DOSE_EXP;
	static public XPathExpression REL_RATE_EXP;
	static public XPathExpression REL_APP_SITE_CODE_EXP;
	static public XPathExpression REL_ADMIN_UNIT_CODE_EXP;
	static public XPathExpression REL_CONSUM_EXP; 
	static public XPathExpression REL_MMAT_CODE_EXP;
	static public XPathExpression REL_MMAT_CODE_TRANS_EXP;
	static public XPathExpression REL_MANU_ORG_NAME_EXP;
	static public XPathExpression REL_MMAT_LOT_EXP;
	
	// Labs
	public static XPathExpression RESULTS_EXPRESSION; 
    public static XPathExpression REL_LAB_RESULT_ORG_EXPRESSION;
    public static XPathExpression REL_LAB_TEST_ORG_EXPRESSION;
    public static XPathExpression REL_COMP_OBS_EXP;
    public static XPathExpression IMMUNIZATION_EXPRESSION;
    public static XPathExpression VITALSIGNS_EXPRESSION;
    public static XPathExpression REL_VITAL_ORG_EXPRESSION;
    
    // Procedure
    public static XPathExpression PROCEDURE_EXPRESSION;
    public static XPathExpression REL_PROCEDURE_UDI_EXPRESSION; 
    public static XPathExpression REL_PROCEDURE_SDL_EXPRESSION; 
    public static XPathExpression REL_PROC_ACT_PROC_EXP;
    public static XPathExpression REL_TARGET_SITE_CODE_EXP;
    public static XPathExpression REL_PERF_ENTITY_EXP;
    public static XPathExpression REL_PERF_ENTITY_ORG_EXP;
    public static XPathExpression REL_REP_ORG_EXP;
    public static XPathExpression REL_NAME_EXP;
    public static XPathExpression REL_ID_EXP;
    public static XPathExpression REL_PLAYING_DEV_CODE_EXP;
    public static XPathExpression REL_SCOPING_ENTITY_ID_EXP; 
	
	// Allergies 
	static public XPathExpression ALLERGIES_EXPRESSION;
	static public XPathExpression REL_ALLERGY_REACTION_EXPRESSION;
	static public XPathExpression REL_ALLERGY_SEVERITY_EXPRESSION;
	
	// CTM
	static public XPathExpression CARE_TEAM_EXPRESSION;
	
	// Social history
	static public XPathExpression SOCIAL_HISTORY_EXPRESSION;
	static public XPathExpression REL_SMOKING_STATUS_EXP;
	static public XPathExpression REL_TOBACCO_USE_EXP;
	static public XPathExpression REL_BIRTHSEX_OBS_EXP;
	
	//Generic
	static public XPathExpression REL_ENTRY_RELSHIP_ACT_EXP;
	static public XPathExpression REL_ENTRY_EXP;
	static public XPathExpression REL_SBDM_ENTRY_EXP;
	static public XPathExpression REL_PART_ROLE_EXP;
	static public XPathExpression REL_ENTRY_RELSHIP_OBS_EXP;
	static public XPathExpression REL_ACT_ENTRY_EXP;
	static public XPathExpression REL_PART_PLAY_ENTITY_CODE_EXP;
	static public XPathExpression REL_ASSN_ENTITY_ADDR;
	static public XPathExpression REL_ASSN_ENTITY_PERSON_NAME;
	static public XPathExpression REL_ASSN_ENTITY_TEL_EXP;
	
	static public String RACE_EL_NAME = "raceCode"; 
	
	
	private CCDAConstants()
	{
		initialize();
	}
	
	public static CCDAConstants getInstance()
	{
		return constants;
	}
	
	private void initialize() {
		
		CCDAXPATH = XPathFactory.newInstance().newXPath();
		
		try {
			
			PATIENT_ROLE_EXP = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/recordTarget/patientRole[not(@nullFlavor)]");
			REL_ADDR_EXP = CCDAConstants.CCDAXPATH.compile("./addr[not(@nullFlavor)]");
			REL_STREET_ADDR1_EXP = CCDAConstants.CCDAXPATH.compile("./streetAddressLine[not(@nullFlavor)]");
			REL_STREET_ADDR2_EXP = CCDAConstants.CCDAXPATH.compile("./streetAddressLine2[not(@nullFlavor)]");
			REL_CITY_EXP = CCDAConstants.CCDAXPATH.compile("./city[not(@nullFlavor)]");
			REL_STATE_EXP = CCDAConstants.CCDAXPATH.compile("./state[not(@nullFlavor)]");
			REL_POSTAL_EXP = CCDAConstants.CCDAXPATH.compile("./postalCode[not(@nullFlavor)]");
			REL_COUNTRY_EXP = CCDAConstants.CCDAXPATH.compile("./country[not(@nullFlavor)]");
			REL_PATIENT_BIRTHPLACE_EXP = CCDAConstants.CCDAXPATH.compile("./patient/birthplace/place/addr[not(@nullFlavor)]");
			REL_PATIENT_NAME_EXP = CCDAConstants.CCDAXPATH.compile("(./patient/name[not(@nullFlavor)])[1]");
			REL_PATIENT_PREV_NAME_EXP = CCDAConstants.CCDAXPATH.compile("(./patient/name[not(@nullFlavor)])[2]");
			REL_PLAY_ENTITY_NAME_EXP = CCDAConstants.CCDAXPATH.compile("./playingEntity/name[not(@nullFlavor)]");
			REL_GIVEN_NAME_EXP = CCDAConstants.CCDAXPATH.compile("(./given[not(@nullFlavor)])[1]");
			REL_MIDDLE_NAME_EXP = CCDAConstants.CCDAXPATH.compile("(./given[not(@nullFlavor)])[2]");
			REL_GIVEN_PREV_NAME_EXP = CCDAConstants.CCDAXPATH.compile("(./given[not(@nullFlavor) and @qualifier='BR'])[1]");
			REL_FAMILY_NAME_EXP = CCDAConstants.CCDAXPATH.compile("./family[not(@nullFlavor)]");
			REL_SUFFIX_EXP = CCDAConstants.CCDAXPATH.compile("./suffix[not(@nullFlavor)]");
			REL_PATIENT_ADMINGEN_EXP = CCDAConstants.CCDAXPATH.compile("./patient/administrativeGenderCode[not(@nullFlavor)]");
			REL_PATIENT_BIRTHTIME_EXP = CCDAConstants.CCDAXPATH.compile("./patient/birthTime[not(@nullFlavor)]");
			REL_PATIENT_MARITAL_EXP = CCDAConstants.CCDAXPATH.compile("./patient/maritalStatusCode[not(@nullFlavor)]");
			REL_PATIENT_RELIGION_EXP = CCDAConstants.CCDAXPATH.compile("./patient/religiousAffiliationCode[not(@nullFlavor)]");
			REL_PATIENT_RACE_EXP = CCDAConstants.CCDAXPATH.compile("./patient/raceCode[not(@nullFlavor)]");
			REL_PATIENT_ETHNICITY_EXP = CCDAConstants.CCDAXPATH.compile("./patient/ethnicGroupCode[not(@nullFlavor)]");
			REL_PATIENT_LANGUAGE_EXP = CCDAConstants.CCDAXPATH.compile("./patient/languageCommunication[not(@nullFlavor)]");
			REL_LANG_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./languageCode[not(@nullFlavor)]");
			REL_LANG_MODE_EXP = CCDAConstants.CCDAXPATH.compile("./modeCode[not(@nullFlavor)]");
			REL_LANG_PREF_EXP = CCDAConstants.CCDAXPATH.compile("./preferenceInd[not(@nullFlavor)]");
			REL_TELECOM_EXP = CCDAConstants.CCDAXPATH.compile("./telecom[not(@nullFlavor)]");
			
			ENCOUNTER_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/component/structuredBody/component/section[not(@nullFlavor) and code[@code='46240-8']]");
			REL_ENC_ENTRY_EXP = CCDAConstants.CCDAXPATH.compile("./entry/encounter[not(@nullFlavor)]");
			PROBLEM_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/component/structuredBody/component/section[not(@nullFlavor) and code[@code='11450-4']]");
			REL_PROBLEM_OBS_EXPRESSION = CCDAConstants.CCDAXPATH.compile("./entryRelationship/observation[not(@nullFlavor) and templateId[@root='2.16.840.1.113883.10.20.22.4.4']]");
			MEDICATION_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/component/structuredBody/component/section[not(@nullFlavor) and code[@code='10160-0']]");
			REL_MED_ENTRY_EXP = CCDAConstants.CCDAXPATH.compile("./entry/substanceAdministration[not(@nullFlavor)]");
			REL_ROUTE_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./routeCode[not(@nullFlavor)]");
			REL_DOSE_EXP = CCDAConstants.CCDAXPATH.compile("./doseQuantity[not(@nullFlavor)]");
			REL_RATE_EXP = CCDAConstants.CCDAXPATH.compile("./rateQuantity[not(@nullFlavor)]");
			REL_APP_SITE_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./approachSiteCode[not(@nullFlavor)]");
			REL_ADMIN_UNIT_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./administrationUnitCode[not(@nullFlavor)]");
			REL_CONSUM_EXP = CCDAConstants.CCDAXPATH.compile("./consumable/manufacturedProduct[not(@nullFlavor)]");
			REL_MMAT_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./manufacturedMaterial/code[not(@nullFlavor)]");
			REL_MMAT_CODE_TRANS_EXP = CCDAConstants.CCDAXPATH.compile("./manufacturedMaterial/code/translation[not(@nullFlavor)]");
			REL_MANU_ORG_NAME_EXP = CCDAConstants.CCDAXPATH.compile("./manufacturerOrganization/name[not(@nullFlavor)]");
			REL_MMAT_LOT_EXP = CCDAConstants.CCDAXPATH.compile("./manufacturedMaterial/lotNumberText[not(@nullFlavor)]");
			ALLERGIES_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/component/structuredBody/component/section[not(@nullFlavor) and code[@code='48765-2']]");
			REL_ALLERGY_REACTION_EXPRESSION = CCDAConstants.CCDAXPATH.compile("./entryRelationship/observation[not(@nullFlavor) and templateId[@root='2.16.840.1.113883.10.20.22.4.9']]");
		    REL_ALLERGY_SEVERITY_EXPRESSION = CCDAConstants.CCDAXPATH.compile("./entryRelationship/observation[not(@nullFlavor) and templateId[@root='2.16.840.1.113883.10.20.22.4.8']]");
			
		    SOCIAL_HISTORY_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/component/structuredBody/component/section[not(@nullFlavor) and code[@code='29762-2']]");
		    REL_SMOKING_STATUS_EXP = CCDAConstants.CCDAXPATH.compile("./entry/observation[not(@nullFlavor) and templateId[@root='2.16.840.1.113883.10.20.22.4.78']]");
		    REL_TOBACCO_USE_EXP = CCDAConstants.CCDAXPATH.compile("./entry/observation[not(@nullFlavor) and templateId[@root='2.16.840.1.113883.10.20.22.4.85']]");
		    REL_BIRTHSEX_OBS_EXP = CCDAConstants.CCDAXPATH.compile("./entry/observation[not(@nullFlavor) and templateId[@root='2.16.840.1.113883.10.20.22.4.200']]");
		    
		    RESULTS_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/component/structuredBody/component/section[not(@nullFlavor) and code[@code='30954-2']]");
		    REL_LAB_RESULT_ORG_EXPRESSION = CCDAConstants.CCDAXPATH.compile("./entry/organizer[not(@nullFlavor) and statusCode[@code='completed']]");
		    REL_LAB_TEST_ORG_EXPRESSION = CCDAConstants.CCDAXPATH.compile("./entry/organizer[not(@nullFlavor) and statusCode[@code='active']]");
		    REL_COMP_OBS_EXP = CCDAConstants.CCDAXPATH.compile("./component/observation[not(@nullFlavor)]");
		    IMMUNIZATION_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/component/structuredBody/component/section[not(@nullFlavor) and code[@code='11369-6']]");
		    VITALSIGNS_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/component/structuredBody/component/section[not(@nullFlavor) and code[@code='8716-3']]");
		    REL_VITAL_ORG_EXPRESSION = CCDAConstants.CCDAXPATH.compile("./entry/organizer[not(@nullFlavor)]");
		    
		    PROCEDURE_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/component/structuredBody/component/section[not(@nullFlavor) and code[@code='47519-4']]");
		    REL_PROCEDURE_UDI_EXPRESSION = CCDAConstants.CCDAXPATH.compile("./participant[not(@nullFlavor) and @typeCode='DEV']/participantRole[not(@nullFlavor)]");
		    REL_PROCEDURE_SDL_EXPRESSION = CCDAConstants.CCDAXPATH.compile("./participant[not(@nullFlavor) and @typeCode='LOC']/participantRole[not(@nullFlavor)]");
		    REL_PROC_ACT_PROC_EXP = CCDAConstants.CCDAXPATH.compile("./entry/procedure[not(@nullFlavor)]");
		    REL_TARGET_SITE_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./targetSiteCode[not(@nullFlavor)]");
		    REL_PERF_ENTITY_EXP = CCDAConstants.CCDAXPATH.compile("./performer/assignedEntity[not(@nullFlavor)]");
		    REL_PERF_ENTITY_ORG_EXP = CCDAConstants.CCDAXPATH.compile("./performer/assignedEntity/representedOrganization[not(@nullFlavor)]");
		    REL_REP_ORG_EXP = CCDAConstants.CCDAXPATH.compile("./representedOrganization[not(@nullFlavor)]");
		    REL_NAME_EXP = CCDAConstants.CCDAXPATH.compile("./name[not(@nullFlavor)]");
		    REL_ID_EXP = CCDAConstants.CCDAXPATH.compile("./id[not(@nullFlavor)]");
		    REL_PLAYING_DEV_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./playingDevice/code[not(@nullFlavor)]");
		    REL_SCOPING_ENTITY_ID_EXP = CCDAConstants.CCDAXPATH.compile("./scopingEntity/id[not(@nullFlavor)]");
		    
		    CARE_TEAM_EXPRESSION = CCDAConstants.CCDAXPATH.compile("/ClinicalDocument/documentationOf/serviceEvent/performer[not(@nullFlavor)]");
		    
			REL_TEMPLATE_ID_EXP = CCDAConstants.CCDAXPATH.compile("./templateId[not(@nullFlavor)]");
			REL_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./code[not(@nullFlavor)]");
			REL_CODE_TRANS_EXP = CCDAConstants.CCDAXPATH.compile("./code/translation[not(@nullFlavor)]");
			REL_VAL_EXP = CCDAConstants.CCDAXPATH.compile("./value[not(@nullFlavor)]");
			REL_STATUS_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./statusCode[not(@nullFlavor)]");
			REL_INT_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./interpretationCode[not(@nullFlavor)]");
			REL_REF_RANGE_EXP = CCDAConstants.CCDAXPATH.compile("./referenceRange/observationRange/value[@type='IVL_PQ']");
			REL_LOW_EXP = CCDAConstants.CCDAXPATH.compile("./low[not(@nullFlavor)]");
			REL_HIGH_EXP = CCDAConstants.CCDAXPATH.compile("./high[not(@nullFlavor)]");
			
			
			
			REL_ACT_ENTRY_EXP = CCDAConstants.CCDAXPATH.compile("./entry/act[not(@nullFlavor)]");
			REL_ENTRY_EXP = CCDAConstants.CCDAXPATH.compile("./entry[not(@nullFlavor)]");
			REL_SBDM_ENTRY_EXP = CCDAConstants.CCDAXPATH.compile("./substanceAdministration[not(@nullFlavor)]");
			REL_ENTRY_RELSHIP_ACT_EXP = CCDAConstants.CCDAXPATH.compile("./entryRelationship/act[not(@nullFlavor)]");
			REL_PART_ROLE_EXP = CCDAConstants.CCDAXPATH.compile("./participant/participantRole[not(@nullFlavor)]");
			REL_PART_PLAY_ENTITY_CODE_EXP = CCDAConstants.CCDAXPATH.compile("./participant/participantRole/playingEntity/code[not(@nullFlavor)]");
			REL_EFF_TIME_EXP = CCDAConstants.CCDAXPATH.compile("./effectiveTime[not(@nullFlavor)]");
			REL_EFF_TIME_LOW_EXP = CCDAConstants.CCDAXPATH.compile("./low[not(@nullFlavor)]");
			REL_EFF_TIME_HIGH_EXP = CCDAConstants.CCDAXPATH.compile("./high[not(@nullFlavor)]");
			REL_ENTRY_RELSHIP_OBS_EXP = CCDAConstants.CCDAXPATH.compile("./entryRelationship/observation[not(@nullFlavor)]");
			REL_ASSN_ENTITY_ADDR = CCDAConstants.CCDAXPATH.compile("./assignedEntity/addr[not(@nullFlavor)]");
			REL_ASSN_ENTITY_PERSON_NAME = CCDAConstants.CCDAXPATH.compile("./assignedEntity/assignedPerson/name[not(@nullFlavor)]");
			REL_ASSN_ENTITY_TEL_EXP = CCDAConstants.CCDAXPATH.compile("./assignedEntity/telecom[not(@nullFlavor)]");
			
			
			
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	NamespaceContext ctx = new NamespaceContext() {
	    public String getNamespaceURI(String prefix) {
	    	if(prefix.contentEquals("hl7"))
	    	{
	    		return "urn:hl7-org:v3"; 
	    	}
	    	else if(prefix.contentEquals("hl7:sdtc"))
	    	{
	    		return "urn:hl7-org:v3:sdtc";
	    	}
	    	else
	    		return null;
	    }
	    public Iterator getPrefixes(String val) {
	        return null;
	    }
	    public String getPrefix(String uri) {
	        return null;
	    }
	};
	
}
