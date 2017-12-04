package org.sitenv.referenceccda.validators.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CCDATemplateIds {
	// C-CDA Header roots
	public static final String US_REALM_HEADER_ROOT = "2.16.840.1.113883.10.20.22.1.1";
	public static final String US_REALM_HEADER_V2_ROOT = US_REALM_HEADER_ROOT;
	// Non-C-CDA Header roots
	public static final String C32_MAIN_ROOT = "2.16.840.1.113883.3.88.11.32.1";
	// R1.1 Document Type roots
	public static final String CONSULTATION_NOTE_ROOT = "2.16.840.1.113883.10.20.22.1.4";
	public static final String CONTINUITY_OF_CARE_DOCUMENT_ROOT = "2.16.840.1.113883.10.20.22.1.2";
	public static final String DIAGNOSTIC_IMAGING_REPORT_ROOT = "2.16.840.1.113883.10.20.22.1.5";
	public static final String DISCHARGE_SUMMARY_ROOT = "2.16.840.1.113883.10.20.22.1.8";
	public static final String HISTORY_AND_PHYSICAL_NOTE_ROOT = "2.16.840.1.113883.10.20.22.1.3";
	public static final String OPERATIVE_NOTE_ROOT = "2.16.840.1.113883.10.20.22.1.7";
	public static final String PROCEDURE_NOTE_ROOT = "2.16.840.1.113883.10.20.22.1.6";
	public static final String PROGRESS_NOTE_ROOT = "2.16.840.1.113883.10.20.22.1.9";
	public static final String UNSTRUCTURED_DOCUMENT_ROOT = "2.16.840.1.113883.10.20.22.1.10";
	public static final List<String> R1_DOCUMENT_ROOTS = new ArrayList<String>(
			Arrays.asList(CONSULTATION_NOTE_ROOT, CONTINUITY_OF_CARE_DOCUMENT_ROOT, DIAGNOSTIC_IMAGING_REPORT_ROOT,
					DISCHARGE_SUMMARY_ROOT, HISTORY_AND_PHYSICAL_NOTE_ROOT, OPERATIVE_NOTE_ROOT, PROCEDURE_NOTE_ROOT,
					PROGRESS_NOTE_ROOT, UNSTRUCTURED_DOCUMENT_ROOT));

	// R2.0/1 extensions
	public static final String R20_EXTENSION = "2014-06-09";
	public static final String R21_EXTENSION = "2015-08-01";
	// R2.0/1 versioned Document Type roots
	public static final String CONSULTATION_NOTE_V2_ROOT = CONSULTATION_NOTE_ROOT;
	public static final String CONTINUITY_OF_CARE_DOCUMENT_V2_ROOT = CONTINUITY_OF_CARE_DOCUMENT_ROOT;
	public static final String DIAGNOSTIC_IMAGING_REPORT_V2_ROOT = DIAGNOSTIC_IMAGING_REPORT_ROOT;
	public static final String DISCHARGE_SUMMARY_V2_ROOT = DISCHARGE_SUMMARY_ROOT;
	public static final String HISTORY_AND_PHYSICAL_NOTE_V2_ROOT = HISTORY_AND_PHYSICAL_NOTE_ROOT;
	public static final String OPERATIVE_NOTE_V2_ROOT = OPERATIVE_NOTE_ROOT;
	public static final String PROCEDURE_NOTE_V2_ROOT = PROCEDURE_NOTE_ROOT;
	public static final String PROGRESS_NOTE_V2_ROOT = PROGRESS_NOTE_ROOT;
	public static final String UNSTRUCTURED_DOCUMENT_V2_ROOT = UNSTRUCTURED_DOCUMENT_ROOT;
	// R2 NEW documents
	// Note: Since these are 'NEW', they don't have an extension for R2.0, but
	// they do for R2.1 since they were versioned from 2.0
	public static final String CARE_PLAN_ROOT = "2.16.840.1.113883.10.20.22.1.15";
	public static final String REFERRAL_NOTE_ROOT = "2.16.840.1.113883.10.20.22.1.14";
	public static final String TRANSFER_SUMMARY_ROOT = "2.16.840.1.113883.10.20.22.1.13";
	public static final String US_REALM_HEADER_PATIENT_GENERATED_DOCUMENT_ROOT = "2.16.840.1.113883.10.20.29.1";
	public static final List<String> R20_NEW_DOCS_NO_EXT = new ArrayList<String>(Arrays.asList(CARE_PLAN_ROOT,
			REFERRAL_NOTE_ROOT, TRANSFER_SUMMARY_ROOT, US_REALM_HEADER_PATIENT_GENERATED_DOCUMENT_ROOT));
	public static final List<String> R2_DOCUMENT_ROOTS = new ArrayList<String>(Arrays.asList(CONSULTATION_NOTE_V2_ROOT,
			CONTINUITY_OF_CARE_DOCUMENT_V2_ROOT, DIAGNOSTIC_IMAGING_REPORT_V2_ROOT, DISCHARGE_SUMMARY_V2_ROOT,
			HISTORY_AND_PHYSICAL_NOTE_V2_ROOT, OPERATIVE_NOTE_V2_ROOT, PROCEDURE_NOTE_V2_ROOT, PROGRESS_NOTE_V2_ROOT,
			UNSTRUCTURED_DOCUMENT_V2_ROOT, CARE_PLAN_ROOT, REFERRAL_NOTE_ROOT, TRANSFER_SUMMARY_ROOT,
			US_REALM_HEADER_PATIENT_GENERATED_DOCUMENT_ROOT));

	public static final List<String> USRH_AND_ALL_DOC_ROOTS = new ArrayList<String>();
	static {
		USRH_AND_ALL_DOC_ROOTS.add(US_REALM_HEADER_V2_ROOT);
		USRH_AND_ALL_DOC_ROOTS.addAll(R2_DOCUMENT_ROOTS);
	}

	public static void main(String[] args) {
		System.out.println(USRH_AND_ALL_DOC_ROOTS);
	}
}
