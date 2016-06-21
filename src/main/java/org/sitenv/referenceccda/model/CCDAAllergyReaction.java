package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAAllergyReaction {
	
	private ArrayList<CCDAII>			templateIds;
	private CCDACode					reactionCode;
	
	public ArrayList<CCDAII> getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(ArrayList<CCDAII> templateIds) {
		this.templateIds = templateIds;
	}

	public CCDACode getReactionCode() {
		return reactionCode;
	}

	public void setReactionCode(CCDACode reactionCode) {
		this.reactionCode = reactionCode;
	}

	public CCDAAllergyReaction()
	{
		
	}

}
