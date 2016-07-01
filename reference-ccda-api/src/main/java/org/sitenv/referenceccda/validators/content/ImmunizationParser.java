package org.sitenv.referenceccda.validators.content;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.CCDAImmunization;
import org.sitenv.referenceccda.model.CCDAImmunizationActivity;
import org.sitenv.referenceccda.model.CCDAOrganization;
import org.sitenv.referenceccda.model.CCDARefModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ImmunizationParser {
	
	private static Logger log = Logger.getLogger(ImmunizationParser.class.getName());
	
    public static void parse(Document doc, CCDARefModel model) throws XPathExpressionException {
    	
    	log.info(" *** Parsing Immunizations *** ");
    	model.setImmunization(retrieveImmunizationDetails(doc));	
	}
	
	public static CCDAImmunization retrieveImmunizationDetails(Document doc) throws XPathExpressionException
	{
		CCDAImmunization immunizations = null;
		Element sectionElement = (Element) CCDAConstants.IMMUNIZATION_EXPRESSION.evaluate(doc, XPathConstants.NODE);
		
		if(sectionElement != null)
		{
			log.info("Adding Immunization ");
			immunizations = new CCDAImmunization();
			immunizations.setTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
					evaluate(sectionElement, XPathConstants.NODESET)));
			
			immunizations.setSectionCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(sectionElement, XPathConstants.NODE)));
			immunizations.setImmActivity(readImmunization((NodeList) CCDAConstants.REL_ENTRY_EXP.
					evaluate(sectionElement, XPathConstants.NODESET)));
		}
		return immunizations;
	}
	
	public static ArrayList<CCDAImmunizationActivity> readImmunization(NodeList entryNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAImmunizationActivity> immunizationActivityList = new ArrayList<>();
		CCDAImmunizationActivity immunizationActivity;
		
		for (int i = 0; i < entryNodeList.getLength(); i++) {
			
			log.info("Adding Immunization Activity ");
			immunizationActivity = new CCDAImmunizationActivity();
			Element entryElement = (Element) entryNodeList.item(i);
			
			Element immunizationActivityElement = (Element) CCDAConstants.REL_SBDM_ENTRY_EXP.
					evaluate(entryElement, XPathConstants.NODE);
			
			if(immunizationActivityElement != null)
			{
			
				log.info("Adding imm activity ");
				immunizationActivity.setTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
													evaluate(immunizationActivityElement, XPathConstants.NODESET)));
				
				immunizationActivity.setTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
						evaluate(immunizationActivityElement, XPathConstants.NODE)));
				
				immunizationActivity.setRouteCode(ParserUtilities.readCode((Element) CCDAConstants.REL_ROUTE_CODE_EXP.
						evaluate(immunizationActivityElement, XPathConstants.NODE)));
				
				immunizationActivity.setDoseQuantity(ParserUtilities.readQuantity((Element) CCDAConstants.REL_DOSE_EXP.
						evaluate(immunizationActivityElement, XPathConstants.NODE)));
				
				immunizationActivity.setApproachSiteCode(ParserUtilities.readCode((Element) CCDAConstants.REL_APP_SITE_CODE_EXP.
							evaluate(immunizationActivityElement, XPathConstants.NODE)));
				
				immunizationActivity.setAdminUnitCode(ParserUtilities.readCode((Element) CCDAConstants.REL_ADMIN_UNIT_CODE_EXP.
							evaluate(immunizationActivityElement, XPathConstants.NODE)));
				
				immunizationActivity.setConsumable(MedicationParser.readMedicationInformation((Element) CCDAConstants.REL_CONSUM_EXP.
						   evaluate(immunizationActivityElement, XPathConstants.NODE)));
				
				
				Element represntOrgElement = (Element) CCDAConstants.REL_PERF_ENTITY_ORG_EXP.
											evaluate(immunizationActivityElement, XPathConstants.NODE);
				if(represntOrgElement != null)
				{
					CCDAOrganization representedOrg = new  CCDAOrganization();
					
					representedOrg.setAddress(ParserUtilities.readAddressList((NodeList) CCDAConstants.REL_ADDR_EXP.
							evaluate(represntOrgElement, XPathConstants.NODESET)));
					
					representedOrg.setTelecom(ParserUtilities.readDataElementList((NodeList) CCDAConstants.REL_TELECOM_EXP.
							evaluate(represntOrgElement, XPathConstants.NODESET)));
					
					representedOrg.setNames( ParserUtilities.readTextContentList((NodeList) CCDAConstants.REL_NAME_EXP.
							evaluate(represntOrgElement, XPathConstants.NODESET)));
					
					immunizationActivity.setOrganization(representedOrg);
				}
				immunizationActivityList.add(immunizationActivity);
			}
		}
		return immunizationActivityList;
	}

}
