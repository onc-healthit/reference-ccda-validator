package org.sitenv.referenceccda.validators.content;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.CCDAPQ;
import org.sitenv.referenceccda.model.CCDAVitalObs;
import org.sitenv.referenceccda.model.CCDAVitalOrg;
import org.sitenv.referenceccda.model.CCDAVitalSigns;

import org.sitenv.referenceccda.model.CCDARefModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class VitalSignParser {
	
	private static Logger log = Logger.getLogger(VitalSignParser.class.getName());
	
	public static void parse(Document doc, CCDARefModel model) throws XPathExpressionException {
    	
    	log.info(" *** Parsing Vital Signs *** ");
    	model.setVitalSigns(retrieveVitalSigns(doc));	
	}
	
	public static CCDAVitalSigns retrieveVitalSigns(Document doc) throws XPathExpressionException
	{
		CCDAVitalSigns vitalSigns = null;
		Element sectionElement = (Element) CCDAConstants.VITALSIGNS_EXPRESSION.evaluate(doc, XPathConstants.NODE);
		if(sectionElement != null)
		{
			log.info("Creating Vital Sign ");
			vitalSigns = new CCDAVitalSigns();
			vitalSigns.setTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
					evaluate(sectionElement, XPathConstants.NODESET)));
			vitalSigns.setSectionCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(sectionElement, XPathConstants.NODE)));
			vitalSigns.setVitalsOrg(readVitalOrganizer((NodeList) CCDAConstants.REL_VITAL_ORG_EXPRESSION.
					evaluate(sectionElement, XPathConstants.NODESET)));
		}
		return vitalSigns;
	}
	
	
	public static ArrayList<CCDAVitalOrg> readVitalOrganizer(NodeList vitalOrganizerNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAVitalOrg> vitalOrganizerList = new ArrayList<>();
		CCDAVitalOrg vitalOrganizer;
		for (int i = 0; i < vitalOrganizerNodeList.getLength(); i++) {
			
			log.info("Creating Vital Organizer ");
			vitalOrganizer = new CCDAVitalOrg();
			
			Element vitalOrganizerElement = (Element) vitalOrganizerNodeList.item(i);
			
			vitalOrganizer.setTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
										evaluate(vitalOrganizerElement, XPathConstants.NODESET)));
			
			vitalOrganizer.setOrgCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(vitalOrganizerElement, XPathConstants.NODE)));
			
			vitalOrganizer.setTranslationCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_TRANS_EXP.
					evaluate(vitalOrganizerElement, XPathConstants.NODE)));
			
			vitalOrganizer.setStatusCode(ParserUtilities.readCode((Element) CCDAConstants.REL_STATUS_CODE_EXP.
					evaluate(vitalOrganizerElement, XPathConstants.NODE)));
			
			vitalOrganizer.setEffTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
					evaluate(vitalOrganizerElement, XPathConstants.NODE)));
			
			vitalOrganizer.setVitalObs(readVitalObservation((NodeList) CCDAConstants.REL_COMP_OBS_EXP.
					evaluate(vitalOrganizerElement, XPathConstants.NODESET)));
			vitalOrganizerList.add(vitalOrganizer);
		}
		return vitalOrganizerList;
	}
	
	public static ArrayList<CCDAVitalObs> readVitalObservation(NodeList vitalObservationNodeList) throws XPathExpressionException
	{
		
		ArrayList<CCDAVitalObs> vitalObservationList = new ArrayList<>();
		CCDAVitalObs vitalObservation;
		for (int i = 0; i < vitalObservationNodeList.getLength(); i++) {
			
			log.info("Creating Vital Observation ");
			vitalObservation = new CCDAVitalObs();
			
			Element resultObservationElement = (Element) vitalObservationNodeList.item(i);
			vitalObservation.setTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
					evaluate(resultObservationElement, XPathConstants.NODESET)));
			
			vitalObservation.setVsCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(resultObservationElement, XPathConstants.NODE)));
			
			vitalObservation.setStatusCode(ParserUtilities.readCode((Element) CCDAConstants.REL_STATUS_CODE_EXP.
					evaluate(resultObservationElement, XPathConstants.NODE)));
			
			vitalObservation.setMeasurementTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
					evaluate(resultObservationElement, XPathConstants.NODE)));
			
			vitalObservation.setInterpretationCode(ParserUtilities.readCode((Element) CCDAConstants.REL_INT_CODE_EXP.
					evaluate(resultObservationElement, XPathConstants.NODE)));
			
			Element vsResult = (Element) CCDAConstants.REL_VAL_EXP.
					evaluate(resultObservationElement, XPathConstants.NODE);
			
			if(vsResult != null)
			{
				if(!ParserUtilities.isEmpty(vsResult.getAttribute("xsi:type")))
				{
					String xsiType = vsResult.getAttribute("xsi:type");
					if (xsiType.equalsIgnoreCase("ST") && (vsResult.getFirstChild() != null))
					{
						log.info("Vital Value is ST ");
						String value = vsResult.getFirstChild().getNodeValue();
						vitalObservation.setVsResult(new CCDAPQ(value,"ST"));
					}else if(xsiType.equalsIgnoreCase("PQ"))
					{
						vitalObservation.setVsResult(ParserUtilities.readQuantity(vsResult));
					}
					else
					{
						log.info("Unknown Lab Value");
					}
				}
			}
			
			NodeList referenceRangeNodeList = (NodeList) CCDAConstants.REL_REF_RANGE_EXP.
					evaluate(resultObservationElement, XPathConstants.NODESET);
			
			ArrayList<CCDAPQ> referenceValueList = new ArrayList<>();
			for (int j = 0; j < referenceRangeNodeList.getLength(); j++) { 
				
				Element referenceRangeElement = (Element) referenceRangeNodeList.item(j);
				
				if(referenceRangeElement != null)
				{
					referenceValueList.add(ParserUtilities.readQuantity((Element) CCDAConstants.REL_LOW_EXP.
		    				evaluate(referenceRangeElement, XPathConstants.NODE)));
					referenceValueList.add(ParserUtilities.readQuantity((Element) CCDAConstants.REL_HIGH_EXP.
		    				evaluate(referenceRangeElement, XPathConstants.NODE)));
				}
			}
			vitalObservation.setReferenceValue(referenceValueList);
			vitalObservationList.add(vitalObservation);
		}
		return vitalObservationList;
	}
}
