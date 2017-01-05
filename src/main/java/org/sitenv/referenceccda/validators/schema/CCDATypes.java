package org.sitenv.referenceccda.validators.schema;

public final class CCDATypes {
	// higher level validation versions - not an objective
	public static final String CCDAR21_OR_CCDAR11 = "C-CDA R2.1 or R1.1 Document";
	public static final String CCDAR11_MU2 = "C-CDA R1.1 MU2 Document";
	
	/// all below here are an actual validationObjective
	// generic CCDA base level
	public static final String NON_SPECIFIC_CCDA = "NonSpecificCCDA";
	public static final String NON_SPECIFIC_CCDAR2 = "NonSpecificCCDAR2";

	// most common mu2
	public static final String TRANSITIONS_OF_CARE_AMBULATORY_SUMMARY = "TransitionsOfCareAmbulatorySummary";

	// other mu2
	public static final String CLINICAL_OFFICE_VISIT_SUMMARY = "ClinicalOfficeVisitSummary";
	public static final String TRANSITIONS_OF_CARE_INPATIENT_SUMMARY = "TransitionsOfCareInpatientSummary";
	public static final String VDT_AMBULATORY_SUMMARY = "VDTAmbulatorySummary";
	public static final String VDT_INPATIENT_SUMMARY = "VDTInpatientSummary";

	// CCDA document level (non-mu2)
	public static final String CONSULTATION_NOTE = "ConsultationNote";
	public static final String CONTINUITY_OF_CARE_DOCUMENT = "ContinuityOfCareDocument";
	public static final String DIAGNOSTIC_IMAGING_REPORT = "DiagnosticImagingReport";
	public static final String DISCHARGE_SUMMARY = "DischargeSummary";
	public static final String HISTORY_AND_PHYSICAL_NOTE = "HistoryAndPhysicalNote";
	public static final String OPERATIVE_NOTE = "OperativeNote";
	public static final String PROCEDURE_NOTE = "ProcedureNote";
	public static final String PROGRESS_NOTE = "ProgressNote";
	public static final String UNSTRUCTURED_DOCUMENT = "UnstructuredDocument";
}
