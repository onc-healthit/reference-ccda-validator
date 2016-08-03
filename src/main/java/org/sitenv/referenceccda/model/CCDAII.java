package org.sitenv.referenceccda.model;

import java.util.ArrayList;

public class CCDAII extends CCDADataElement{
	private String  rootValue;
	private String  extValue;
	
	public Boolean isPartOf(ArrayList<CCDAII> list) {
		
		for( CCDAII item : list) {
			
			// Both Root and Extensions are present.
			if( (rootValue != null) && (item.getRootValue() != null) && 
				(extValue != null)  && (item.getExtValue() != null) && 
				(rootValue.equalsIgnoreCase(item.getRootValue())) && 
				(extValue.equalsIgnoreCase(item.getExtValue()))) {
				return true;
			}
			// Only Root value are present
			else if( (rootValue != null) && (item.getRootValue() != null) && 
					(extValue == null)  && (item.getExtValue() == null) && 
					(rootValue.equalsIgnoreCase(item.getRootValue()))) {
				return true;
			}
			
			// continue through the list
		}
		
		//if we never hit the postive case
		return false;
	}
	
	public String getRootValue() {
		return rootValue;
	}

	public void setRootValue(String rootValue) {
		this.rootValue = rootValue;
	}

	public String getExtValue() {
		return extValue;
	}

	public void setExtValue(String extValue) {
		this.extValue = extValue;
	}

	public CCDAII()
	{
	}
}
