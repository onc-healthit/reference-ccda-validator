package org.sitenv.referenceccda.validators.content;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;

public class EncounterDiagnosesParser {
	
	private static Logger log = Logger.getLogger(EncounterDiagnosesParser.class.getName());
	
    public static void parse(Document doc, CCDARefModel model) throws XPathExpressionException {
    	
    	model.setEncounter(retrieveEncounterDetails(doc));	
	}
	
	public static CCDAEncounter retrieveEncounterDetails(Document doc) throws XPathExpressionException
	{
		Element sectionElement = (Element) CCDAConstants.ENCOUNTER_EXPRESSION.evaluate(doc, XPathConstants.NODE);
		CCDAEncounter encounters = null;
		
		if(sectionElement != null)
		{
			log.info(" Adding Encounter ");
			encounters = new CCDAEncounter();
			
			//Get Template Ids
			encounters.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
					evaluate(sectionElement, XPathConstants.NODESET)));
			
			// Get Section Code
			encounters.setSectionCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(sectionElement, XPathConstants.NODE)));
			
			// Get Entries
			encounters.setEncActivities(readEncounterActivity((NodeList) CCDAConstants.REL_ENC_ENTRY_EXP.
					evaluate(sectionElement, XPathConstants.NODESET)));
		}
		return encounters;
	}
	
	
	public static ArrayList<CCDAEncounterActivity> readEncounterActivity(NodeList encounterActivityNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAEncounterActivity> encounterActivityList = new ArrayList<>();
		CCDAEncounterActivity encounterActivity;
		for (int i = 0; i < encounterActivityNodeList.getLength(); i++) {
			
			Element encounterActivityElement = (Element) encounterActivityNodeList.item(i);
			
			if(encounterActivityElement != null)
			{
				log.info(" Adding Encounter Activity");
				encounterActivity = new CCDAEncounterActivity();
				
				//Get Tempalte Ids
				encounterActivity.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
						evaluate(encounterActivityElement, XPathConstants.NODESET)));

				encounterActivity.setEncounterTypeCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
										evaluate(encounterActivityElement, XPathConstants.NODE)));
				
				encounterActivity.setEffectiveTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
															evaluate(encounterActivityElement, XPathConstants.NODE)));
				
				encounterActivity.setSdLocs(readServiceDeliveryLocators((NodeList) CCDAConstants.REL_PART_ROLE_EXP.
																evaluate(encounterActivityElement, XPathConstants.NODESET)));
				
				NodeList encounterDiagnosisNodeList = (NodeList) CCDAConstants.REL_ENTRY_RELSHIP_ACT_EXP.
								evaluate(encounterActivityElement, XPathConstants.NODESET);
				
				encounterActivity.setDiagnoses(readEncounterDiagnosis(encounterDiagnosisNodeList));
				
				NodeList indicationNodeList = (NodeList) CCDAConstants.REL_ENTRY_RELSHIP_OBS_EXP.
								evaluate(encounterActivityElement, XPathConstants.NODESET);
				
				encounterActivity.setIndications(readProblemObservation(indicationNodeList));
				
				encounterActivityList.add(encounterActivity);
			}
		}
		return encounterActivityList;
	}
	
	public static ArrayList<CCDAEncounterDiagnosis> readEncounterDiagnosis(NodeList encounterDiagnosisNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAEncounterDiagnosis> encounterDiagnosisList = null;
		if(encounterDiagnosisNodeList.getLength() > 0)
		{
			encounterDiagnosisList = new ArrayList<>();
		}
		CCDAEncounterDiagnosis encounterDiagnosis;
		for (int i = 0; i < encounterDiagnosisNodeList.getLength(); i++) {
			
			log.info(" Adding Encounter Diagnosis");
			
			Element encounterDiagnosisElement = (Element) encounterDiagnosisNodeList.item(i);
			encounterDiagnosis = new CCDAEncounterDiagnosis();
			encounterDiagnosis.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
					evaluate(encounterDiagnosisElement, XPathConstants.NODESET)));
			
			encounterDiagnosis.setEntryCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
									evaluate(encounterDiagnosisElement, XPathConstants.NODE)));
			
			NodeList problemObservationNodeList = (NodeList) CCDAConstants.REL_ENTRY_RELSHIP_OBS_EXP.
										evaluate(encounterDiagnosisElement, XPathConstants.NODESET);
			
			encounterDiagnosis.setProblemObs(readProblemObservation(problemObservationNodeList));
			encounterDiagnosisList.add(encounterDiagnosis);
		}
		
		return encounterDiagnosisList;
	}
	
	public static ArrayList<CCDAServiceDeliveryLoc> readServiceDeliveryLocators(NodeList serviceDeliveryLocNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAServiceDeliveryLoc> serviceDeliveryLocsList = null;
		if(serviceDeliveryLocNodeList.getLength() > 0)
		{
			serviceDeliveryLocsList = new ArrayList<>();
		}
		CCDAServiceDeliveryLoc serviceDeliveryLoc;
		for (int i = 0; i < serviceDeliveryLocNodeList.getLength(); i++) {
			
			
			log.info(" Adding Service Delivery  Location");
			serviceDeliveryLoc = new CCDAServiceDeliveryLoc();
			
			Element serviceDeliveryLocElement = (Element) serviceDeliveryLocNodeList.item(i);
			serviceDeliveryLoc.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
											evaluate(serviceDeliveryLocElement, XPathConstants.NODESET)));
			
			serviceDeliveryLoc.setLocationCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
											evaluate(serviceDeliveryLocElement, XPathConstants.NODE)));
			
			serviceDeliveryLoc.setName(ParserUtilities.readTextContext((Element) CCDAConstants.REL_PLAY_ENTITY_NAME_EXP.
					evaluate(serviceDeliveryLocElement, XPathConstants.NODE)));
			
			serviceDeliveryLoc.setTelecom(ParserUtilities.readDataElementList((NodeList) CCDAConstants.REL_TELECOM_EXP.
											evaluate(serviceDeliveryLocElement, XPathConstants.NODESET)));
			
			serviceDeliveryLoc.setAddress(ParserUtilities.readAddressList((NodeList) CCDAConstants.REL_ADDR_EXP.
											evaluate(serviceDeliveryLocElement, XPathConstants.NODESET)));
			
			serviceDeliveryLocsList.add(serviceDeliveryLoc);
		}
		
		return serviceDeliveryLocsList;
	}
	
	public static ArrayList<CCDAProblemObs> readProblemObservation(NodeList problemObservationNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAProblemObs> problemObservationList = null;
		if(problemObservationNodeList.getLength() > 0)
		{
			problemObservationList = new ArrayList<>();
		}
		CCDAProblemObs problemObservation;
		for (int i = 0; i < problemObservationNodeList.getLength(); i++) {
			
			log.info(" Adding Problem Observation as part of encounter ");
			problemObservation = new CCDAProblemObs();
			
			Element problemObservationElement = (Element) problemObservationNodeList.item(i);
			problemObservation.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
					evaluate(problemObservationElement, XPathConstants.NODESET)));
			
			problemObservation.setProblemType(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
									evaluate(problemObservationElement, XPathConstants.NODE)));
			
			problemObservation.setTranslationProblemType(ParserUtilities.readCodeList((NodeList) CCDAConstants.REL_CODE_TRANS_EXP.
									evaluate(problemObservationElement, XPathConstants.NODESET)));
			
			problemObservation.setEffTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
										evaluate(problemObservationElement, XPathConstants.NODE)));
			
			problemObservation.setProblemCode(ParserUtilities.readCode((Element) CCDAConstants.REL_VAL_EXP.
					evaluate(problemObservationElement, XPathConstants.NODE)));
			
			problemObservationList.add(problemObservation);
		}
		
		return problemObservationList;
	}

}
