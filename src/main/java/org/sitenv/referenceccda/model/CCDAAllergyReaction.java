package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;

public class CCDAAllergyReaction {
	
	private static Logger log = Logger.getLogger(CCDAAllergyReaction.class.getName());
	
	private ArrayList<CCDAII>			templateIds;
	private CCDACode					reactionCode;
	
	public void log() {
		
		log.info("***Allergy Reaction ***");
		
		if(reactionCode != null)
			log.info("Allergy Reaction Code = " + reactionCode.getCode());
		
		for(int j = 0; j < templateIds.size(); j++) {
			log.info(" Tempalte Id [" + j + "] = " + templateIds.get(j).getRootValue());
			log.info(" Tempalte Id Ext [" + j + "] = " + templateIds.get(j).getExtValue());
		}	
	}
	
	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> ids) {
		
		if(ids != null)
			this.templateIds = ids;
	}

	public CCDACode getReactionCode() {
		return reactionCode;
	}

	public void setReactionCode(CCDACode reactionCode) {
		this.reactionCode = reactionCode;
	}

	public CCDAAllergyReaction()
	{
		templateIds = new ArrayList<CCDAII>();
	}

}
