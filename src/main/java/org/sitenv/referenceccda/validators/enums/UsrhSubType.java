package org.sitenv.referenceccda.validators.enums;

public enum UsrhSubType {
	CONSULTATION_NOTE("Consultation Note"), CONTINUITY_OF_CARE_DOCUMENT("CCD"), DIAGNOSTIC_IMAGING_REPORT("Diagnostic Imaging Report"), 
	DISCHARGE_SUMMARY("Discharge Summary"), HISTORY_AND_PHYSICAL_NOTE("History and Physical Note"), OPERATIVE_NOTE("Operative Note"), 
	PROCEDURE_NOTE("Procedure Note"), PROGRESS_NOTE("Progress Note"), UNSTRUCTURED_DOCUMENT("Unstructured Document"), 
	CARE_PLAN("Care Plan"), REFERRAL_NOTE("Referral Note"), TRANSFER_SUMMARY("Transfer Summary"), 
	US_REALM_HEADER_PATIENT_GENERATED_DOCUMENT("USRH Patient Generated Document");

	private String name;
	
	private UsrhSubType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
