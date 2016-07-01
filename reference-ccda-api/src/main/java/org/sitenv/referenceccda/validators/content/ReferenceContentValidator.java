package org.sitenv.referenceccda.validators.content;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.sitenv.referenceccda.model.CCDARefModel;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class ReferenceContentValidator extends BaseCCDAValidator implements CCDAValidator  {
	
	private static Logger log = Logger.getLogger(ReferenceContentValidator.class.getName());

	@Autowired
	private CCDAParser parser;
	
	@Override
	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName,
			String ccdaFile) {
		
		log.info(" ***** CAME INTO THE REFERENCE VALIDATOR *****");
		
		if(!isObjectiveValidForContentValidation(validationObjective)) {
			log.warn("Content Validation not performed for objective " + validationObjective);
			return null;
		}
		
		initialize();
		log.info(" Val Obj " + validationObjective + " Ref File " + referenceFileName);
		
		// Parse passed in File
		CCDARefModel submittedCCDA = parser.parse(ccdaFile);
		
		
		CCDARefModel ref = null;
		if( (referenceFileName != null) 
			&& (!referenceFileName.isEmpty()) 
			&& (!(referenceFileName.trim()).isEmpty())) {
				ref = ReferenceContent.getInstance().getCCDARefModel(referenceFileName);
		}
		
		if((ref != null) && (submittedCCDA != null))
				return ref.compare(validationObjective, submittedCCDA );
		else {
			log.error(" Submitted Model = " + ((submittedCCDA==null)?" Model is null":submittedCCDA.toString()));
			log.error(" Reference Model = " + ((ref==null)?" Model is null":ref.toString()));
			log.error("Something is wrong, not able to find ref model for " + referenceFileName);
			return null;
		}
	}
	
	private Boolean isObjectiveValidForContentValidation(String valObj) 
	{
		if(valObj.equalsIgnoreCase("170.315_b1_ToC_Amb") || 
		   valObj.equalsIgnoreCase("170.315_b1_ToC_Inp") ||
		   valObj.equalsIgnoreCase("170.315_b2_CIRI_Amb") ||
		   valObj.equalsIgnoreCase("170.315_b2_CIRI_Inp") ||
		   valObj.equalsIgnoreCase("170.315_b4_CCDS_Amb") ||
		   valObj.equalsIgnoreCase("170.315_b4_CCDS_Inp") ||
		   valObj.equalsIgnoreCase("170.315_b6_DE_Amb") ||
		   valObj.equalsIgnoreCase("170.315_b6_DE_Inp") ||
		   valObj.equalsIgnoreCase("170.315_b9_CP_Amb") ||
		   valObj.equalsIgnoreCase("170.315_b9_CP_Inp") ||
		   valObj.equalsIgnoreCase("170.315_e1_VDT_Amb") ||
		   valObj.equalsIgnoreCase("170.315_e1_VDT_Inp") ||
		   valObj.equalsIgnoreCase("170.315_g9_APIAccess_Amb") || 
		   valObj.equalsIgnoreCase("170.315_g9_APIAccess_Inp") )
			return true;
		else
			return false;
	}
	
	private void initialize()
	{	
		ReferenceContent rf  = ReferenceContent.getInstance();
		
		if(rf.getInitFlag())
			return;
		
		log.info(" ***** Initializing Models *****");
		
		// Get each of the scenarios from resources folder and add them into the system.
		Enumeration<URL> en = null;
		try {
			en = getClass().getClassLoader().getResources("scenarios");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info(e.getMessage());
			e.printStackTrace();
		}
	    if (en.hasMoreElements()) {
	    	
	    	log.info("Found Scenario directory ");
	        URL metaInf=en.nextElement();
	     
	        File fileMetaInf = null;
			try {
				fileMetaInf = new File(metaInf.toURI());
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				log.info(e.getMessage());
				e.printStackTrace();
			} 
			
	        String[] filenames=fileMetaInf.list();
	        log.info(" Number of files = " + filenames.length);
	        
	        for(int i = 0; i < filenames.length;i++)
	        {
	        	// Read each scenario and populate the reference content.
	        	
	        	try {
	        		
	        		ClassLoader classLoader = getClass().getClassLoader();
	        		
	        		String fname = "scenarios/" + filenames[i];
	        		String content = IOUtils.toString(classLoader.getResourceAsStream(fname));
					
					log.info("Processing File = " + filenames[i]);
					CCDARefModel m = parser.parse(content);
					
					String modelName = FilenameUtils.getBaseName(fname);
					log.info("Adding Model " + modelName);
					rf.addCCDARefModel(modelName, m);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.info(e.getMessage());
					e.printStackTrace();
				}
	        }
	    }
	    
	    rf.setInitFlag(true);
	}

}
