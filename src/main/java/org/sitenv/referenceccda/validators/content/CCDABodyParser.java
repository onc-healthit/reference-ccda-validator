package org.sitenv.referenceccda.validators.content;

import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.CCDARefModel;
import org.w3c.dom.Document;

import javax.xml.xpath.XPathExpressionException;

public class CCDABodyParser {
	
	private static Logger log = Logger.getLogger(CCDABodyParser.class.getName());
	
	static public void parseBody(Document doc, CCDARefModel model) throws XPathExpressionException{
	
		log.info(" Parsing Encounters ");
		EncounterDiagnosesParser.parse(doc,model);
		
		log.info(" Parsing Problems ");
		ProblemParser.parse(doc, model);
		
		log.info(" Parsing Medications ");
		MedicationParser.parse(doc, model);
		
		log.info("Parsing Allergies ");
		AllergiesParser.parse(doc,model);
		
		log.info("Parsing Social History ");
		SocialHistoryParser.parse(doc, model);
		
		log.info(" Parsing lab Results ");
		LabResultParser.parse(doc, model);
		
		log.info(" Parsing lab tests ");
		LabTestParser.parse(doc, model);
		
		log.info("Parsing Vitals ");
		VitalSignParser.parse(doc, model);
		
		log.info("Parsing Procedures ");
		ProcedureParser.parse(doc, model);
		
		log.info("Parsing Care Team Members ");
		CareTeamMemberParser.parse(doc, model);
		
		log.info("Parsing Immunizations ");
		ImmunizationParser.parse(doc, model);
		
		
	}
}