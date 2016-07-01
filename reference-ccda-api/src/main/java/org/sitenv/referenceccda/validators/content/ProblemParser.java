package org.sitenv.referenceccda.validators.content;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.CCDAProblem;
import org.sitenv.referenceccda.model.CCDAProblemConcern;
import org.sitenv.referenceccda.model.CCDAProblemObs;
import org.sitenv.referenceccda.model.CCDARefModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ProblemParser {
	
	private static Logger log = Logger.getLogger(ProblemParser.class.getName());
	
    public static void parse(Document doc, CCDARefModel model) throws XPathExpressionException {
    	
    	log.info(" *** Parsing Problems *** ");
    	model.setProblem(retrieveProblemDetails(doc));	
	}
	
	public static CCDAProblem retrieveProblemDetails(Document doc) throws XPathExpressionException
	{
		CCDAProblem problems = null;
		Element sectionElement = (Element) CCDAConstants.PROBLEM_EXPRESSION.evaluate(doc, XPathConstants.NODE);
		
		if(sectionElement != null)
		{
			log.info(" Found Problem Section ");
			problems = new CCDAProblem();
			problems.setSectionTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
											evaluate(sectionElement, XPathConstants.NODESET)));
			
			problems.setSectionCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(sectionElement, XPathConstants.NODE)));
			
			problems.setProblemConcerns(readProblemConcern((NodeList) CCDAConstants.REL_ACT_ENTRY_EXP.
					evaluate(sectionElement, XPathConstants.NODESET)));
		}
		return problems;
	}
	
	public static ArrayList<CCDAProblemConcern> readProblemConcern(NodeList problemConcernNodeList) throws XPathExpressionException
	{
		ArrayList<CCDAProblemConcern> problemConcernList = new ArrayList<>();
		CCDAProblemConcern problemConcern;
		for (int i = 0; i < problemConcernNodeList.getLength(); i++) {
			
			log.info("Adding Problem Concern ");
			problemConcern = new CCDAProblemConcern();
			Element problemConcernElement = (Element) problemConcernNodeList.item(i);
			
			problemConcern.setTemplateId(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
										evaluate(problemConcernElement, XPathConstants.NODESET)));
			
			problemConcern.setConcernCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(problemConcernElement, XPathConstants.NODE)));
			
			problemConcern.setStatusCode(ParserUtilities.readCode((Element) CCDAConstants.REL_STATUS_CODE_EXP.
					evaluate(problemConcernElement, XPathConstants.NODE)));
			
			problemConcern.setEffTime(ParserUtilities.readEffectiveTime((Element) CCDAConstants.REL_EFF_TIME_EXP.
								evaluate(problemConcernElement, XPathConstants.NODE)));
			
			problemConcern.setProblems(readProblemObservation((NodeList) CCDAConstants.REL_PROBLEM_OBS_EXPRESSION.
					evaluate(problemConcernElement, XPathConstants.NODESET)));
			
			problemConcernList.add(problemConcern);
		}
		return problemConcernList;
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
