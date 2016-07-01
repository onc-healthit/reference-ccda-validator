package org.sitenv.referenceccda.validators.content;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.*;
import org.sitenv.referenceccda.model.CCDARefModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ReferenceContent {
	
	private static final Logger log = Logger.getLogger(ReferenceContent.class);
	
	private HashMap<String, CCDARefModel>   referenceModels;
	private final static ReferenceContent refContent = new ReferenceContent();
	private static Boolean initFlag = false;
	
	@Autowired
	private CCDAParser parser;
	
	private ReferenceContent()
	{
		log.info("Initializing reference content ");
		referenceModels = new HashMap<String, CCDARefModel>();
		//initialize();
	}
	
	public static ReferenceContent getInstance()
	{	
		return refContent;
	}
	
	public CCDARefModel getCCDARefModel(String scenarioName)
	{
		Set<String> keys = referenceModels.keySet();
		
		for (String s : keys) {
		    
			log.info("Comparing " + scenarioName + " to " + s);
			if(scenarioName.contains(s)) {
				log.info("Returning Content Model for Comparison " + s );
				return referenceModels.get(s);
			}
		}
		
		return null;
	}
	
	public void addCCDARefModel(String scenario, CCDARefModel m)
	{
		referenceModels.put(scenario,  m);
	}
	/*
	private void initialize()
	{
		//setInitFlag(true);
		if(referenceModels == null)
			referenceModels = new HashMap<String, CCDARefModel>();
		
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
					
					// CCDARefModel m = parser.parse(content);
					
					// log.info("Adding Model");
					
					// addCCDARefModel(fname, m);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.info(e.getMessage());
					e.printStackTrace();
				}
	        }
	    }
	}*/

	public static Boolean getInitFlag() {
		return initFlag;
	}

	public static void setInitFlag(Boolean initFlag) {
		ReferenceContent.initFlag = initFlag;
	}
}
