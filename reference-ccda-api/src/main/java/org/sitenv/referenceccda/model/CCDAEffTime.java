package org.sitenv.referenceccda.model;

public class CCDAEffTime {

	private CCDADataElement low;
	private Boolean         lowPresent;
	private CCDADataElement high;
	private Boolean         highPresent;
	private CCDADataElement value;
	private Boolean         valuePresent;
	
	public CCDADataElement getLow() {
		return low;
	}

	public void setLow(CCDADataElement low) {
		this.low = low;
	}

	public Boolean getLowPresent() {
		return lowPresent;
	}

	public void setLowPresent(Boolean lowPresent) {
		this.lowPresent = lowPresent;
	}

	public CCDADataElement getHigh() {
		return high;
	}

	public void setHigh(CCDADataElement high) {
		this.high = high;
	}

	public Boolean getHighPresent() {
		return highPresent;
	}

	public void setHighPresent(Boolean highPresent) {
		this.highPresent = highPresent;
	}

	public CCDADataElement getValue() {
		return value;
	}

	public void setValue(CCDADataElement value) {
		this.value = value;
	}

	public Boolean getValuePresent() {
		return valuePresent;
	}

	public void setValuePresent(Boolean valuePresent) {
		this.valuePresent = valuePresent;
	}

	public CCDAEffTime()
	{
		
	}
}
