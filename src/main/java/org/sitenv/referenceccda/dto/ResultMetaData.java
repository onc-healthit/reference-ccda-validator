package org.sitenv.referenceccda.dto;

public class ResultMetaData {
	private String type;
	private int count;

	public ResultMetaData(String type, int count) {
		this.type = type;
		this.count = count;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	// ------------------------- INTERNAL CODE CHANGE START
	private String severity;
	
	public ResultMetaData(String type, int count, String severity) {
		this(type, count); 
		this.severity = severity;
	}

	
	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	// ------------------------- INTERNAL CODE CHANGE END
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (type == null ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ResultMetaData other = (ResultMetaData) obj;
		if (type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

}
