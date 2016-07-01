package org.sitenv.referenceccda.validators.content;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.CCDALabResult;
import org.sitenv.referenceccda.model.CCDARefModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class LabTestParser {
	
	private static Logger log = Logger.getLogger(LabTestParser.class.getName());
	
	public static void parse(Document doc, CCDARefModel model) throws XPathExpressionException {
    	
    	log.info(" *** Parsing Lab Test *** ");
    	model.setLabTests(retrieveLabTests(doc));	
	}
	
	public static CCDALabResult retrieveLabTests(Document doc) throws XPathExpressionException
	{
		CCDALabResult labTests = null;
		
		Element sectionElement = (Element) CCDAConstants.RESULTS_EXPRESSION.evaluate(doc, XPathConstants.NODE);
		if(sectionElement != null)
		{
			log.info("Lab Test Created ");
			labTests = new CCDALabResult();
			labTests.setResultSectionTempalteIds(ParserUtilities.readTemplateIdList((NodeList) CCDAConstants.REL_TEMPLATE_ID_EXP.
								evaluate(sectionElement, XPathConstants.NODESET)));
			
			labTests.setSectionCode(ParserUtilities.readCode((Element) CCDAConstants.REL_CODE_EXP.
					evaluate(sectionElement, XPathConstants.NODE)));
		
			labTests.setResultOrg(LabResultParser.readResultOrganizer((NodeList) CCDAConstants.REL_LAB_TEST_ORG_EXPRESSION.
													evaluate(sectionElement, XPathConstants.NODESET)));
			labTests.setIsLabTestInsteadOfResult(true);
		}
		return labTests;
	}

}
