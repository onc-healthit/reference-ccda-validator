package org.sitenv.referenceccda.validators.content;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;

public class SocialHistoryParser {
	
	private static Logger log = Logger.getLogger(SocialHistoryParser.class.getName());
	
	public static void parse(Document doc, CCDARefModel model) throws XPathExpressionException {
    	
    	log.info(" *** Parsing Social History *** ");
    	model.setSmokingStatus(retrieveSmokingStatusDetails(doc));	
	}
	
	public static CCDASocialHistory retrieveSmokingStatusDetails(Document doc) throws XPathExpressionException
	{
		CCDASocialHistory socailHistory = null;
		Element sectionElement = (Element) CCDAConstants.SOCIAL_HISTORY_EXPRESSION.evaluate(doc, XPathConstants.NODE);
		if(sectionElement != null)
		{
			log.info("Adding Social History ");
			socailHistory = new CCDASocialHistory();
			socailHistory.setSectionTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
						evaluate(sectionElement, XPathConstants.NODESET)));
			
			socailHistory.setSectionCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(sectionElement, XPathConstants.NODE)));
			
			NodeList smokingStatusNodeList = (NodeList) CCDAConstants.REL_SMOKING_STATUS_EXP.
				evaluate(sectionElement, XPathConstants.NODESET);
		
			socailHistory.setSmokingStatus(readSmokingStatus(smokingStatusNodeList));
		
			NodeList tobaccoUseNodeList = (NodeList) CCDAConstants.REL_TOBACCO_USE_EXP.
				evaluate(sectionElement, XPathConstants.NODESET);
		
			socailHistory.setTobaccoUse(readTobaccoUse(tobaccoUseNodeList));
			
			NodeList bsList = (NodeList) CCDAConstants.REL_BIRTHSEX_OBS_EXP.
					evaluate(sectionElement, XPathConstants.NODESET);
	
			socailHistory.setBirthSex(readBirthSex(bsList));
		}
		
		return socailHistory;
	}
	
	public static CCDABirthSexObs readBirthSex(NodeList bsList) throws XPathExpressionException
	{
		CCDABirthSexObs birthSex  = null;
		for (int i = 0; i < bsList.getLength(); i++) {
			
			log.info("Adding Birth Sex ");
			birthSex = new CCDABirthSexObs();
			
			Element bsElement = (Element) bsList.item(i);
			
			birthSex.setTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
														evaluate(bsElement, XPathConstants.NODESET)));
			
			birthSex.setBirthSexObsCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(bsElement, XPathConstants.NODE)));
			
			birthSex.setSexCode(ParserUtilities.readCode((Element) CCDAConstants.REL_VAL_EXP.
					evaluate(bsElement, XPathConstants.NODE)));
			
			birthSex.setObservationTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
					evaluate(bsElement, XPathConstants.NODE)));
			
		}
		
		return birthSex;

	}
	
	public static ArrayList<CCDASmokingStatus> readSmokingStatus(NodeList smokingStatusNodeList) throws XPathExpressionException
	{
		ArrayList<CCDASmokingStatus> smokingStatusList = null;
		if(!ParserUtilities.isNodeListEmpty(smokingStatusNodeList))
		{
			smokingStatusList = new ArrayList<>();
		}
		CCDASmokingStatus smokingStatus;
		for (int i = 0; i < smokingStatusNodeList.getLength(); i++) {
			
			log.info("Adding Smoking Status");
			smokingStatus = new CCDASmokingStatus();
			
			Element smokingStatusElement = (Element) smokingStatusNodeList.item(i);
			
			smokingStatus.setSmokingStatusTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
														evaluate(smokingStatusElement, XPathConstants.NODESET)));
			
			smokingStatus.setSmokingStatusCode(ParserUtilities.readCode((Element) CCDAConstants.REL_VAL_EXP.
					evaluate(smokingStatusElement, XPathConstants.NODE)));
			
			smokingStatus.setObservationTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
					evaluate(smokingStatusElement, XPathConstants.NODE)));
			
			smokingStatusList.add(smokingStatus);
		}
		return smokingStatusList;
	}
	
	public static ArrayList<CCDATobaccoUse> readTobaccoUse(NodeList tobaccoUseNodeList) throws XPathExpressionException
	{
		ArrayList<CCDATobaccoUse> tobaccoUseList = null;
		if(!ParserUtilities.isNodeListEmpty(tobaccoUseNodeList))
		{
			tobaccoUseList = new ArrayList<>();
		}
		CCDATobaccoUse tobaccoUse;
		for (int i = 0; i < tobaccoUseNodeList.getLength(); i++) {
			
			log.info("Adding Tobacco Use");
			tobaccoUse = new CCDATobaccoUse();
			
			Element tobaccoUseElement = (Element) tobaccoUseNodeList.item(i);
			
			tobaccoUse.setTobaccoUseTemplateIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
														evaluate(tobaccoUseElement, XPathConstants.NODESET)));
			
			tobaccoUse.setTobaccoUseCode(ParserUtilities.readCode((Element) CCDAConstants.REL_VAL_EXP.
					evaluate(tobaccoUseElement, XPathConstants.NODE)));
			
			tobaccoUse.setTobaccoUseTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
					evaluate(tobaccoUseElement, XPathConstants.NODE)));
			
			tobaccoUseList.add(tobaccoUse);
		}
		return tobaccoUseList;
	}

}
