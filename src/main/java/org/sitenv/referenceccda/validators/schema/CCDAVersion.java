package org.sitenv.referenceccda.validators.schema;

public enum CCDAVersion {
	R11("R1.1"), R20("R2.0"), R21("R2.1"), NOT_CCDA("Not a C-CDA document");

	private final String version;

	private CCDAVersion(final String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}
}
