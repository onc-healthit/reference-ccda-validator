package org.sitenv.referenceccda.validators.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ValidationResultType {
	CCDA_IG_CONFORMANCE_ERROR("C-CDA IG ERROR"), CCDA_IG_CONFORMANCE_WARN("C-CDA IG WARN"), CCDA_IG_CONFORMANCE_INFO("C-CDA IG INFO"), CCDA_VOCAB_CONFORMANCE_ERROR(
			"C-CDA VOCAB ERROR"), CCDA_VOCAB_CONFORMANCE_WARN("C-CDA VOCAB WARN"), CCDA_VOCAB_CONFORMANCE_INFO("C-CDA VOCAB INFO"), REF_CCDA_ERROR(
			"REF C-CDA ERROR"), REF_CCDA_WARN("REF C-CDA WARN"), REF_CCDA_INFO("REF C-CDA INFO");

	private String errorTypePrettyName;

	private ValidationResultType(String type) {
		errorTypePrettyName = type;
	}

	@JsonValue
	public String getTypePrettyName() {
		return errorTypePrettyName;
	}

	public String getValidationResultType() {
		return name();
	}

}