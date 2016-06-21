package org.sitenv.referenceccda.validators.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ValidationResultType {
	CCDA_MDHT_CONFORMANCE_ERROR("C-CDA MDHT Conformance Error"), CCDA_MDHT_CONFORMANCE_WARN("C-CDA MDHT Conformance Warning"), CCDA_MDHT_CONFORMANCE_INFO("C-CDA MDHT Conformance Info"), CCDA_VOCAB_CONFORMANCE_ERROR(
			"ONC 2015 S&CC Vocabulary Validation Conformance Error"), CCDA_VOCAB_CONFORMANCE_WARN("ONC 2015 S&CC Vocabulary Validation Conformance Warning"), CCDA_VOCAB_CONFORMANCE_INFO("ONC 2015 S&CC Vocabulary Validation Conformance Info"), REF_CCDA_ERROR(
			"ONC 2015 S&CC Reference C-CDA Validation Error"), REF_CCDA_WARN("ONC 2015 S&CC Reference C-CDA Validation Warning"), REF_CCDA_INFO("ONC 2015 S&CC Reference C-CDA Validation Info");

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