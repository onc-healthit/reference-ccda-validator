package org.sitenv.referenceccda.model;

import org.apache.log4j.Logger;

public class CCDAFrequency {
	
	private static Logger log = Logger.getLogger(CCDAFrequency.class.getName());
	
	private boolean institutionSpecified;
	private String operator;
	private String value;
	private String unit;
	
	public void log() {
		
		log.info("Institution Specified = " + institutionSpecified );
		log.info("Operator = " + operator );
		log.info("Value = " + value );
		log.info("Unit = " + unit );
	}
	
	
	public boolean isInstitutionSpecified() {
		return institutionSpecified;
	}
	public void setInstitutionSpecified(boolean institutionSpecified) {
		this.institutionSpecified = institutionSpecified;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (institutionSpecified ? 1231 : 1237);
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		CCDAFrequency other = (CCDAFrequency) obj;
		if (institutionSpecified != other.institutionSpecified)
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (unit == null) {
			if (other.unit != null)
				return false;
		} else if (!unit.equals(other.unit))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
