package org.sitenv.referenceccda.validators.content;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;

public class ProcedureParser {
	
	private static Logger log = Logger.getLogger(ProcedureParser.class.getName());
	
	public static void parse(Document doc, CCDARefModel model) throws XPathExpressionException {
	    	
	    	log.info(" *** Parsing Procedures *** ");
	    	model.setProcedure(retrievePrcedureDetails(doc));	
		}
	
	public static CCDAProcedure retrievePrcedureDetails( Document doc) throws XPathExpressionException
	{
		CCDAProcedure procedures = null;
		Element sectionElement = (Element) CCDAConstants.PROCEDURE_EXPRESSION.evaluate(doc, XPathConstants.NODE);
		if(sectionElement !=null)
		{
			log.info("Adding Procedures");
			procedures = new CCDAProcedure();
			procedures.setSectionTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
					evaluate(sectionElement, XPathConstants.NODESET)));
			
			procedures.setSectionCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(sectionElement, XPathConstants.NODE)));
			
			procedures.setProcActsProcs(readProcedures((NodeList) CCDAConstants.REL_PROC_ACT_PROC_EXP.
					evaluate(sectionElement, XPathConstants.NODESET)));
		}
		return procedures;
	}
	
	public static ArrayList<CCDAProcActProc> readProcedures(NodeList proceduresNodeList ) throws XPathExpressionException
	{
		ArrayList<CCDAProcActProc> proceduresList = null;
		if(!ParserUtilities.isNodeListEmpty(proceduresNodeList))
		{
			proceduresList = new ArrayList<>();
		}
		CCDAProcActProc procedure;
		for (int i = 0; i < proceduresNodeList.getLength(); i++) {
			
			log.info("Adding Proc Act Proc ");
			procedure = new CCDAProcActProc();
			Element procedureElement = (Element) proceduresNodeList.item(i);
			
			procedure.setSectionTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
										evaluate(procedureElement, XPathConstants.NODESET)));
			
			procedure.setProcCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(procedureElement, XPathConstants.NODE)));
			
			procedure.setProcStatus(ParserUtilities.readCode((Element) CCDAConstants.REL_STATUS_CODE_EXP.
					evaluate(procedureElement, XPathConstants.NODE)));
			
			procedure.setTargetSiteCode(ParserUtilities.readCode((Element) CCDAConstants.REL_TARGET_SITE_CODE_EXP.
					evaluate(procedureElement, XPathConstants.NODE)));
			
			NodeList performerNodeList = (NodeList) CCDAConstants.REL_PERF_ENTITY_EXP.
						evaluate(procedureElement, XPathConstants.NODESET);
			
			procedure.setPerformer(readPerformerList(performerNodeList));
			
			NodeList deviceNodeList = (NodeList) CCDAConstants.REL_PROCEDURE_UDI_EXPRESSION.
						evaluate(procedureElement, XPathConstants.NODESET);
			
			procedure.setPatientUDI(readUDI(deviceNodeList));
			
			NodeList serviceDeliveryNodeList = (NodeList) CCDAConstants.REL_PROCEDURE_SDL_EXPRESSION.
						evaluate(procedureElement, XPathConstants.NODESET);
			
			procedure.setSdLocs(readServiceDeliveryLocators(serviceDeliveryNodeList));
			
			
			proceduresList.add(procedure);
		}
		return proceduresList;
	}
	
	public static ArrayList<CCDAAssignedEntity> readPerformerList(NodeList performerEntityNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAAssignedEntity> assignedEntityList = null;
		if(!ParserUtilities.isNodeListEmpty(performerEntityNodeList))
		{
			assignedEntityList = new ArrayList<>();
		}
		CCDAAssignedEntity assignedEntity;
		
		for (int i = 0; i < performerEntityNodeList.getLength(); i++) {
			
			Element performerEntityElement = (Element) performerEntityNodeList.item(i);
			assignedEntity = new CCDAAssignedEntity();
			
			if(performerEntityElement != null)
			{
				log.info("Adding Performer");
				assignedEntity.setAddresses(ParserUtilities.readAddressList((NodeList) CCDAConstants.REL_ADDR_EXP.
													evaluate(performerEntityElement, XPathConstants.NODESET)));
				
				assignedEntity.setTelecom(ParserUtilities.readDataElementList((NodeList) CCDAConstants.REL_TELECOM_EXP.
													evaluate(performerEntityElement, XPathConstants.NODESET)));
					
				Element represntOrgElement = (Element) CCDAConstants.REL_REP_ORG_EXP.
													evaluate(performerEntityElement, XPathConstants.NODE);
				if(represntOrgElement != null)
				{
					CCDAOrganization representedOrg = new  CCDAOrganization();
						
					representedOrg.setAddress(ParserUtilities.readAddressList((NodeList) CCDAConstants.REL_ADDR_EXP.
								evaluate(represntOrgElement, XPathConstants.NODESET)));
						
					representedOrg.setTelecom(ParserUtilities.readDataElementList((NodeList) CCDAConstants.REL_TELECOM_EXP.
								evaluate(represntOrgElement, XPathConstants.NODESET)));
						
					representedOrg.setNames( ParserUtilities.readTextContentList((NodeList) CCDAConstants.REL_NAME_EXP.
								evaluate(represntOrgElement, XPathConstants.NODESET)));
						
					assignedEntity.setOrganization(representedOrg);
				}
			}
			
			assignedEntityList.add(assignedEntity);
		}
		
		return assignedEntityList;
		
	}
	
	public static ArrayList<CCDAUDI> readUDI(NodeList deviceNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAUDI> deviceList =  null;
		if(!ParserUtilities.isNodeListEmpty(deviceNodeList))
		{
			deviceList = new ArrayList<>();
		}
		CCDAUDI device;
		for (int i = 0; i < deviceNodeList.getLength(); i++) {
			
			log.info("Adding UDIs");
			device = new CCDAUDI();
			
			Element deviceElement = (Element) deviceNodeList.item(i);
			device.setTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
											evaluate(deviceElement, XPathConstants.NODESET)));
			
			device.setUDIValue(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_ID_EXP.
											evaluate(deviceElement, XPathConstants.NODESET)));
			device.setDeviceCode(ParserUtilities.readCode((Element) CCDAConstants.REL_PLAYING_DEV_CODE_EXP.
					evaluate(deviceElement, XPathConstants.NODE)));
			
			device.setScopingEntityId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_SCOPING_ENTITY_ID_EXP.
					evaluate(deviceElement, XPathConstants.NODESET)));
			
			
			deviceList.add(device);
			
		}
		
		return deviceList;
		
	}
	
	public static ArrayList<CCDAServiceDeliveryLoc> readServiceDeliveryLocators(NodeList serviceDeliveryLocNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAServiceDeliveryLoc> serviceDeliveryLocsList = null;
		if(!ParserUtilities.isNodeListEmpty(serviceDeliveryLocNodeList))
		{
			serviceDeliveryLocsList = new ArrayList<>();
		}
		CCDAServiceDeliveryLoc serviceDeliveryLoc;
		for (int i = 0; i < serviceDeliveryLocNodeList.getLength(); i++) {
			
			serviceDeliveryLoc = new CCDAServiceDeliveryLoc();
			
			Element serviceDeliveryLocElement = (Element) serviceDeliveryLocNodeList.item(i);
			serviceDeliveryLoc.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
											evaluate(serviceDeliveryLocElement, XPathConstants.NODESET)));
			
			serviceDeliveryLoc.setLocationCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(serviceDeliveryLocElement, XPathConstants.NODE)));
			
			serviceDeliveryLoc.setName(ParserUtilities.readCode((Element) CCDAConstants.REL_PLAY_ENTITY_NAME_EXP.
					evaluate(serviceDeliveryLocElement, XPathConstants.NODE)));
			
			serviceDeliveryLoc.setTelecom(ParserUtilities.readDataElementList((NodeList) CCDAConstants.REL_TELECOM_EXP.
					evaluate(serviceDeliveryLocElement, XPathConstants.NODESET)));
			serviceDeliveryLoc.setAddress(ParserUtilities.readAddressList((NodeList) CCDAConstants.REL_ADDR_EXP.
					evaluate(serviceDeliveryLocElement, XPathConstants.NODESET)));
			
			serviceDeliveryLocsList.add(serviceDeliveryLoc);
		}
		
		return serviceDeliveryLocsList;
		
	}

}
