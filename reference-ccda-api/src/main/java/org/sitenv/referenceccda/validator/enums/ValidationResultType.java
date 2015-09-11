package org.sitenv.referenceccda.validator.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ValidationResultType {
	CCDA_IG_CONFORMANCE_ERROR("CCDA_IG_ERROR"), CCDA_IG_CONFORMANCE_WARN("CCDA_IG_WARN"), CCDA_IG_CONFORMANCE_INFO("CCDA_IG_INFO"), CCDA_VOCAB_CONFORMANCE_ERROR(
			"CCDA_VOCAB_ERROR"), CCDA_VOCAB_CONFORMANCE_WARN("CCDA_VOCAB_WARN"), CCDA_VOCAB_CONFORMANCE_INFO("CCDA_VOCAB_INFO"), REF_CCDA_ERROR(
			"REF_CCDA_ERROR"), REF_CCDA_WARN("REF_CCDA_WARN"), REF_CCDA_INFO("REF_CCDA_INFO");

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