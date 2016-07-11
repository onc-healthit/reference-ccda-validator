package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAHealthConcerns {

	private static Logger log = Logger.getLogger(CCDAHealthConcerns.class.getName());
    
	private ArrayList<CCDAII>     templateIds;
	
	public CCDAHealthConcerns() {
		templateIds = new ArrayList<CCDAII>();
	}
	
	
	public void log() { 
		
		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}	
	}
}
