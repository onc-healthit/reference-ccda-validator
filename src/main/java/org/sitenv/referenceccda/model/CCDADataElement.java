package org.sitenv.referenceccda.model;

public class CCDADataElement {

	private String  value;
	private Integer lineNumber;
	private String  xpath;
	
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
