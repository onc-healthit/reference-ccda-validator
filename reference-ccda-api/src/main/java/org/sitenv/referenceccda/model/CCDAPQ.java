package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

public class CCDAPQ extends CCDADataElement {
	
	private static Logger log = Logger.getLogger(CCDAPQ.class.getName());
	
	private String  value;
	private String  units;
	private String  xsiType;
	
	public void log() {
		
		log.info(" Value = " + value);
		log.info(" Units = " + units);
		log.info(" xsiType = " + xsiType);
	}
	
	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}



	public String getUnits() {
		return units;
	}



	public void setUnits(String units) {
		this.units = units;
	}

	public CCDAPQ(String value)
	{
	  this.value = value;
	}
	
	public CCDAPQ(String value, String xsiType)
	{
	  this.value = value;
	  this.xsiType = xsiType;
	}
	
	public String getXsiType() {
		return xsiType;
	}



	public void setXsiType(String xsiType) {
		this.xsiType = xsiType;
	}



	public CCDAPQ()
	{
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		result = prime * result + ((xsiType == null) ? 0 : xsiType.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CCDAPQ other = (CCDAPQ) obj;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		if (xsiType == null) {
			if (other.xsiType != null)
				return false;
		} else if (!xsiType.equals(other.xsiType))
			return false;
		return true;
	}
}

