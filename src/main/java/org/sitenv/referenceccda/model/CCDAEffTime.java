package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

public class CCDAEffTime {
	
	private static Logger log = Logger.getLogger(CCDAEffTime.class.getName());

	private CCDADataElement low;
	private Boolean         lowPresent;
	private CCDADataElement high;
	private Boolean         highPresent;
	private CCDADataElement value;
	private Boolean         valuePresent;
	private String 			singleAdministration;
	
	public void log() {
		
		log.info("Eff Time Low = " + (lowPresent ? low.getValue() : "No Low"));
		log.info("Eff Time High = " + (highPresent ? high.getValue() : "No High"));
		log.info("Eff Time Value = " + (valuePresent ? value.getValue() : "No Value"));
		log.info(" Single Admin = " + singleAdministration);
	}
	
	public String getSingleAdministration() {
		return singleAdministration;
	}

	public void setSingleAdministration(String singleAdministration) {
		this.singleAdministration = singleAdministration;
	}

	public CCDADataElement getLow() {
		return low;
	}

	public void setLow(CCDADataElement l) {
		
		if(l != null)
		{
			this.low = l;
			lowPresent = true;
		}
	}

	public Boolean getLowPresent() {
		return lowPresent;
	}

	public CCDADataElement getHigh() {
		return high;
	}

	public void setHigh(CCDADataElement h) {
		
		if(h != null)
		{
			this.high = h;
			highPresent = true;
		}
	}

	public Boolean getHighPresent() {
		return highPresent;
	}

	public CCDADataElement getValue() {
		return value;
	}

	public void setValue(CCDADataElement v) {
		
		if(v != null)
		{
			this.value = v;
			valuePresent = true;
		}
	}

	public Boolean getValuePresent() {
		return valuePresent;
	}

	public CCDAEffTime()
	{
		valuePresent = false;
		lowPresent = false;
		highPresent = false;
	}
}
