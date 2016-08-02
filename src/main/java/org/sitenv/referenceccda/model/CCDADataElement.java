package org.sitenv.referenceccda.model;

import java.util.ArrayList;

import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;

public class CCDADataElement {

	private String  value;
	private Integer lineNumber;
	private String  xpath;
	private String  use;
	
	public Boolean matches(CCDADataElement cd, ArrayList<RefCCDAValidationResult> results, String elementName) {
		
		if( (value != null) && (cd.getValue() != null) &&
			(value.equalsIgnoreCase(cd.getValue())) ) {
			
			return true;
		}
		else if((value == null) && (cd.getValue() == null))
		{
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	public CCDADataElement(String value)
	{
		this.value = value;
	}
	
	public CCDADataElement()
	{
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}
	
	
}
