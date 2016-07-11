package org.sitenv.referenceccda.validators.content;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.CCDACareTeamMember;
import org.sitenv.referenceccda.model.CCDAParticipant;
import org.sitenv.referenceccda.model.CCDARefModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;

public class CareTeamMemberParser {
	
	private static Logger log = Logger.getLogger(CareTeamMemberParser.class.getName());
	
    public static void parse(Document doc, CCDARefModel model) throws XPathExpressionException {
    	
    	log.info(" *** Parsing Care Team Members *** ");
    	model.setMembers(retrieveCTMDetails(doc));	
	}
	
	public static CCDACareTeamMember retrieveCTMDetails(Document doc) throws XPathExpressionException
	{
		NodeList performerNodeList = (NodeList) CCDAConstants.CARE_TEAM_EXPRESSION.evaluate(doc, XPathConstants.NODESET);
		CCDACareTeamMember careTeamMember = new CCDACareTeamMember();
		ArrayList<CCDAParticipant> participantList = new ArrayList<>();
		CCDAParticipant participant ;
		for (int i = 0; i < performerNodeList.getLength(); i++) {
			
			log.info("Creating PArticipant");
			participant = new CCDAParticipant();
			Element performerElement = (Element) performerNodeList.item(i);
			
			participant.setAddress(ParserUtilities.readAddress((Element) CCDAConstants.REL_ASSN_ENTITY_ADDR.
					evaluate(performerElement, XPathConstants.NODE)));
			
			readName((Element) CCDAConstants.REL_ASSN_ENTITY_PERSON_NAME.
	    				evaluate(performerElement, XPathConstants.NODE), participant);
			
			participant.setTelecom(ParserUtilities.readDataElement((Element) CCDAConstants.REL_ASSN_ENTITY_TEL_EXP.
					evaluate(performerElement, XPathConstants.NODE)));
			participantList.add(participant);
		}
		careTeamMember.setMembers(participantList);
		
		return careTeamMember;
	}
	
	public static void readName(Element nameElement,CCDAParticipant participant) throws XPathExpressionException
	{
		if(nameElement != null)
		{
			NodeList giveNameNodeList = (NodeList) CCDAConstants.REL_GIVEN_NAME_EXP.
					evaluate(nameElement, XPathConstants.NODESET);
			
			for (int i = 0; i < giveNameNodeList.getLength(); i++) {
				Element givenNameElement = (Element) giveNameNodeList.item(i);
				if(!ParserUtilities.isEmpty(givenNameElement.getAttribute("qualifier")))
				{
					participant.setPreviousName(ParserUtilities.readTextContext(givenNameElement));
				}else if (i == 0) {
					participant.setFirstName(ParserUtilities.readTextContext(givenNameElement));
				}else {
					participant.setMiddleName(ParserUtilities.readTextContext(givenNameElement));
				}
			}
			
			participant.setLastName(ParserUtilities.readTextContext((Element) CCDAConstants.REL_FAMILY_NAME_EXP.
					evaluate(nameElement, XPathConstants.NODE)));
			participant.setSuffix(ParserUtilities.readTextContext((Element) CCDAConstants.REL_SUFFIX_EXP.
					evaluate(nameElement, XPathConstants.NODE)));
		}
	}

}
