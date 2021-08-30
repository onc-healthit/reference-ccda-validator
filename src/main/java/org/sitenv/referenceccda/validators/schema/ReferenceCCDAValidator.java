package org.sitenv.referenceccda.validators.schema;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.mdht.uml.cda.ClinicalDocument;
import org.eclipse.mdht.uml.cda.DocumentRoot;
import org.eclipse.mdht.uml.cda.util.CDADiagnostic;
import org.eclipse.mdht.uml.cda.util.CDAUtil;
import org.eclipse.mdht.uml.cda.util.ValidationResult;
import org.eclipse.mdht.uml.hl7.datatypes.II;
import org.hl7.security.ds4p.contentprofile.CONTENTPROFILEPackage;
import org.hl7.security.ds4p.contentprofile.util.DS4PUtil;
import org.openhealthtools.mdht.uml.cda.consol.CarePlan;
import org.openhealthtools.mdht.uml.cda.consol.ConsolPackage;
import org.openhealthtools.mdht.uml.cda.consol.ConsultationNote;
import org.openhealthtools.mdht.uml.cda.consol.ConsultationNote2;
import org.openhealthtools.mdht.uml.cda.consol.ContinuityOfCareDocument;
import org.openhealthtools.mdht.uml.cda.consol.ContinuityOfCareDocument2;
import org.openhealthtools.mdht.uml.cda.consol.DiagnosticImagingReport;
import org.openhealthtools.mdht.uml.cda.consol.DiagnosticImagingReport2;
import org.openhealthtools.mdht.uml.cda.consol.DischargeSummary;
import org.openhealthtools.mdht.uml.cda.consol.DischargeSummary2;
import org.openhealthtools.mdht.uml.cda.consol.HistoryAndPhysicalNote;
import org.openhealthtools.mdht.uml.cda.consol.HistoryAndPhysicalNote2;
import org.openhealthtools.mdht.uml.cda.consol.OperativeNote;
import org.openhealthtools.mdht.uml.cda.consol.OperativeNote2;
import org.openhealthtools.mdht.uml.cda.consol.ProcedureNote;
import org.openhealthtools.mdht.uml.cda.consol.ProcedureNote2;
import org.openhealthtools.mdht.uml.cda.consol.ProgressNote;
import org.openhealthtools.mdht.uml.cda.consol.ProgressNote2;
import org.openhealthtools.mdht.uml.cda.consol.ReferralNote;
import org.openhealthtools.mdht.uml.cda.consol.TransferSummary;
import org.openhealthtools.mdht.uml.cda.consol.USRealmHeaderPatientGeneratedDocument;
import org.openhealthtools.mdht.uml.cda.consol.UnstructuredDocument;
import org.openhealthtools.mdht.uml.cda.consol.UnstructuredDocument2;
import org.openhealthtools.mdht.uml.cda.mu2consol.Mu2consolPackage;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.XPathIndexer;
import org.sitenv.referenceccda.validators.enums.UsrhSubType;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.vocabularies.constants.VocabularyConstants;
import org.sitenv.vocabularies.constants.VocabularyConstants.SeverityLevel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.xml.sax.SAXException;

@RequestScope
@Component
public class ReferenceCCDAValidator extends BaseCCDAValidator implements CCDAValidator {
	private static Logger logger = Logger.getLogger(ReferenceCCDAValidator.class);
	
	private static final String IG_ISSUE_ID = "a.consol", MU_ISSUE_ID = "a.mu2con", DS4P_ISSUE_ID = "ds4p";
	private static final boolean CAPTURE_VALIDATION_STATISTICS = true;
	private boolean isValidationObjectiveMu2Type = false;
	private boolean isValidationObjectiveDS4PType = false;	
	private String ccdaDocumentType = CCDATypes.UNKNOWN_DOC_TYPE;
	private CCDAVersion ccdaVersion = CCDAVersion.NOT_CCDA;
	private long totalConformanceErrorChecks = -1L;
	
	public boolean isValidationObjectiveMu2Type() { 
		return isValidationObjectiveMu2Type; 
	}
	
	public boolean isValidationObjectiveDS4PType() { 
		return isValidationObjectiveDS4PType; 
	}
	
	public String getCcdaDocumentType() {
		return ccdaDocumentType;
	}
	
	public CCDAVersion getCcdaVersion() {
		return ccdaVersion;
	}
	
	public long getTotalConformanceErrorChecks() {
		return totalConformanceErrorChecks;
	}

	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName,
			String ccdaFile) throws SAXException, Exception {
		return validateFile(validationObjective, referenceFileName, ccdaFile, SeverityLevel.INFO);
	}
	
	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName,
			String ccdaFile, SeverityLevel severityLevel) throws SAXException, Exception {
		final XPathIndexer xpathIndexer = new XPathIndexer();
		ValidationResult result = new ValidationResult(CAPTURE_VALIDATION_STATISTICS);
		InputStream in = null, in2 = null;
		trackXPathsInXML(xpathIndexer, ccdaFile);
		try {
			in = IOUtils.toInputStream(ccdaFile, "UTF-8");
			in2 = IOUtils.toInputStream(ccdaFile, "UTF-8");
			validateDocumentByTypeUsingMDHTApi(in, in2, validationObjective, result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeInputStreams(new ArrayList<InputStream>(Arrays.asList(in, in2)));
		}
		if(result.getAllDiagnostics().isEmpty()) {
			logAndThrowException("The MDHT ValidationResult object was not populated for an unknown reason. "
					+ "Please ensure that the XML document sent is a valid C-CDA R1.0, R1.1, or R2.1 file. "
					+ "This error may be related to but is not limited to: "
					+ "An invalid document type templateId/@root, an invalid templateId/@extension, "
					+ "or invalid combination of the two. Note: C-CDA R2.0 is not supported by the validator.");
		}
		logger.info("Processing and returning MDHT validation results");
		return processValidationResults(xpathIndexer, result, severityLevel);
	}
	
	private static void closeInputStreams(List<InputStream> inputStreams) {
		for (InputStream is : inputStreams) {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}	

	private void validateDocumentByTypeUsingMDHTApi(InputStream in, InputStream in2, String validationObjective,
			ValidationResult result) throws Exception {
		if(StringUtils.isEmpty(validationObjective)) {
			logAndThrowException("The validationObjective given is " + (validationObjective == null ? "null" : "empty"),
					"The validationObjective given was null or empty. Please try one of the following valid Strings instead: "
					+ ValidationObjectives.getObjectives() + " " + CCDATypes.getTypes());
		}
		
		String mdhtValidationObjective = mapMdhtValidationObjective(validationObjective);		
		//implementing a temporary work-around to allow invalid objectives to apply a default for the ETT
		if(mdhtValidationObjective == null) {
			validationObjective = ValidationObjectives.Sender.C_CDA_IG_PLUS_VOCAB;
			mdhtValidationObjective = CCDATypes.NON_SPECIFIC_CCDAR2;
		}		
		logger.info("Mapped mdhtValidationObjective: " + (mdhtValidationObjective != null ? mdhtValidationObjective : "null objective"));
		
		ClinicalDocument clinicalDocument = null;
		if(mdhtValidationObjective != null) {
			//populate the field for reuse
			isValidationObjectiveMu2Type = isValidationObjectiveMu2Type(mdhtValidationObjective);
			isValidationObjectiveDS4PType = isValidationObjectiveDS4PType(mdhtValidationObjective);
			if (isValidationObjectiveCCDAType(mdhtValidationObjective)) {
				Mu2consolPackage.eINSTANCE.unload();
				CONTENTPROFILEPackage.eINSTANCE.unload();
				ConsolPackage.eINSTANCE.eClass();
				logger.info("Loading mdhtValidationObjective: " + mdhtValidationObjective
						+ " mapped from valdationObjective: " + validationObjective);
				clinicalDocument = CDAUtil.load(in, result);
			} else if (isValidationObjectiveMu2Type) {
				CONTENTPROFILEPackage.eINSTANCE.unload();
				Mu2consolPackage.eINSTANCE.reload();
				Mu2consolPackage.eINSTANCE.eClass();
				EClass docType = null;
				if (mdhtValidationObjective.equalsIgnoreCase(CCDATypes.CLINICAL_OFFICE_VISIT_SUMMARY)) {
					docType = Mu2consolPackage.eINSTANCE.getClinicalOfficeVisitSummary();
				} else if (mdhtValidationObjective.equalsIgnoreCase(CCDATypes.TRANSITIONS_OF_CARE_AMBULATORY_SUMMARY)) {
					docType = Mu2consolPackage.eINSTANCE.getTransitionOfCareAmbulatorySummary();
				} else if (mdhtValidationObjective.equalsIgnoreCase(CCDATypes.TRANSITIONS_OF_CARE_INPATIENT_SUMMARY)) {
					docType = Mu2consolPackage.eINSTANCE.getTransitionOfCareInpatientSummary();
				} else if (mdhtValidationObjective.equalsIgnoreCase(CCDATypes.VDT_AMBULATORY_SUMMARY)) {
					docType = Mu2consolPackage.eINSTANCE.getVDTAmbulatorySummary();
				} else if (mdhtValidationObjective.equalsIgnoreCase(CCDATypes.VDT_INPATIENT_SUMMARY)) {
					docType = Mu2consolPackage.eINSTANCE.getVDTInpatientSummary();
				}
				boolean isDocTypeNull = docType == null || docType.getName() == null;
				logger.info("Loading mdhtValidationObjective: " + mdhtValidationObjective
						+ " as MU2 docType: " + (!isDocTypeNull ? docType : "null docType")
						+ " mapped from valdationObjective: " + validationObjective);
				if(!isDocTypeNull) {
					clinicalDocument = CDAUtil.loadAs(in, docType, result);
				} else {
					logAndThrowException("docType == null", "The MU2 docType EClass could not be assigned "
							+ "from mdhtValidationObjective: " + mdhtValidationObjective);
				}
			} else if (isValidationObjectiveDS4PType) {
				Mu2consolPackage.eINSTANCE.unload();
				CONTENTPROFILEPackage.eINSTANCE.reload();
				CONTENTPROFILEPackage.eINSTANCE.eClass();
				logger.info("Loading mdhtValidationObjective: " + mdhtValidationObjective
						+ " mapped from valdationObjective: " + validationObjective);
				DS4PUtil.validateAsDS4P(in, result);
				clinicalDocument = CDAUtil.load(in2, result);
			}
		} else {
			//Note: This is dead code for now as null values are temporarily populated to a default
			logAndThrowException("The validationObjective given is invalid", 
					"The validationObjective given was invalid. Please try one of the following valid Strings instead: "
					+ ValidationObjectives.getObjectives() + " " + CCDATypes.getTypes());
		}
		ccdaDocumentType = determineCcdaDocumentType(clinicalDocument);
		ccdaVersion = determineCcdaDocumentVersion(clinicalDocument);		
		logger.info("ccdaVersion identified as: " + ccdaVersion.getVersion());
	}
	 
	private CCDAVersion determineCcdaDocumentVersion(ClinicalDocument clinicalDocument) {
		CCDAVersion version = CCDAVersion.NOT_CCDA;
		for (II templateIdInHeader : clinicalDocument.getTemplateIds()) {
			String xmlRoot = templateIdInHeader.getRoot();
			String xmlExt = templateIdInHeader.getExtension();
			if (xmlRoot != null && !xmlRoot.isEmpty()) {
				for (String officialRoot : CCDATemplateIds.USRH_AND_ALL_DOC_ROOTS) {
					if (xmlRoot.equals(officialRoot)) {
						if (xmlExt != null && !xmlExt.isEmpty()) {
							if (xmlExt.equals(CCDATemplateIds.R21_EXTENSION)) {
								return CCDAVersion.R21;								
							} else if (xmlExt.equals(CCDATemplateIds.R20_EXTENSION)) {
								return CCDAVersion.R20;
							}
						} else {
							for (String r20NewDocsNoExtRoot : CCDATemplateIds.R20_NEW_DOCS_NO_EXT) {
								if (xmlRoot.equals(r20NewDocsNoExtRoot)) {
									logger.info("returning version R20 for NEW doc");
									return CCDAVersion.R20;
								}
							}
							// Set vs return because ordering of IIs can be random, and some docs have both R11 and R21
							// included (which counts as R21) - need to allow another version to overwrite it.
							version = CCDAVersion.R11;
						}
					}
				}
			}
		}
		logger.info("returning version by determineCcdaDocumentVersion method end");
		return version;
	}	
	
	private String determineCcdaDocumentType(ClinicalDocument clinicalDocument) {
		UsrhSubType usrhSubType = null;
		String docType = null;
		
		if(clinicalDocument != null) {
			if (clinicalDocument instanceof ConsultationNote || clinicalDocument instanceof ConsultationNote2) {
				usrhSubType = UsrhSubType.CONSULTATION_NOTE;
			} else if (clinicalDocument instanceof ContinuityOfCareDocument || clinicalDocument instanceof ContinuityOfCareDocument2) {
				usrhSubType = UsrhSubType.CONTINUITY_OF_CARE_DOCUMENT;
			} else if (clinicalDocument instanceof DiagnosticImagingReport || clinicalDocument instanceof DiagnosticImagingReport2) {
				usrhSubType = UsrhSubType.DIAGNOSTIC_IMAGING_REPORT;
			} else if (clinicalDocument instanceof DischargeSummary || clinicalDocument instanceof DischargeSummary2) {
				usrhSubType = UsrhSubType.DISCHARGE_SUMMARY;
			} else if (clinicalDocument instanceof HistoryAndPhysicalNote || clinicalDocument instanceof HistoryAndPhysicalNote2) {
				usrhSubType = UsrhSubType.HISTORY_AND_PHYSICAL_NOTE;
			} else if (clinicalDocument instanceof OperativeNote || clinicalDocument instanceof OperativeNote2) {
				usrhSubType = UsrhSubType.OPERATIVE_NOTE;
			} else if (clinicalDocument instanceof ProcedureNote || clinicalDocument instanceof ProcedureNote2) {
				usrhSubType = UsrhSubType.PROCEDURE_NOTE;
			} else if (clinicalDocument instanceof ProgressNote || clinicalDocument instanceof ProgressNote2) {
				usrhSubType = UsrhSubType.PROGRESS_NOTE;
			} else if (clinicalDocument instanceof UnstructuredDocument || clinicalDocument instanceof UnstructuredDocument2) {
				usrhSubType = UsrhSubType.UNSTRUCTURED_DOCUMENT;
			} else if (clinicalDocument instanceof CarePlan) {
				usrhSubType = UsrhSubType.CARE_PLAN;
			} else if (clinicalDocument instanceof ReferralNote) {
				usrhSubType = UsrhSubType.REFERRAL_NOTE;
			} else if (clinicalDocument instanceof TransferSummary) {
				usrhSubType = UsrhSubType.TRANSFER_SUMMARY;
			} else if (clinicalDocument instanceof USRealmHeaderPatientGeneratedDocument) {
				usrhSubType = UsrhSubType.US_REALM_HEADER_PATIENT_GENERATED_DOCUMENT;
			}
		} else {			
			if(isValidationObjectiveDS4PType) {
				docType = CCDATypes.DS4P;
			} else {
				docType = CCDATypes.UNKNOWN_DOC_TYPE;
			}
		}

		if(usrhSubType != null) {
			docType = usrhSubType.getName();
		} else {
			if(docType == null) {
				docType = CCDATypes.UNKNOWN_DOC_TYPE;
			}
		}
		
		return docType;
	}	
	
	private static String mapMdhtValidationObjective(String validationObjectivePOSTed) throws Exception {
		if (isValidationObjectiveACertainType(validationObjectivePOSTed, CCDATypes.NON_SPECIFIC_CCDA_TYPES)
				|| isValidationObjectiveACertainType(validationObjectivePOSTed, CCDATypes.MU2_TYPES)
				|| isValidationObjectiveACertainType(validationObjectivePOSTed, CCDATypes.DS4P_TYPES)) {
			// we already have a *specific* MDHT objective (it was sent directly so no re-mapping required)
			return validationObjectivePOSTed;
		} else if (isValidationObjectiveACertainType(validationObjectivePOSTed, ValidationObjectives.ALL_UNIQUE)) {
			// convert to a *generic* MDHT objective (runs R2.1 or R1.1 MDHT validation using the consol2 model)
			return CCDATypes.NON_SPECIFIC_CCDAR2;
		}
		logger.warn("An invalid validationObjective was POSTed");
		return null;
	}
	
	private static void logAndThrowException(String dualErrorMessage) throws Exception {
		logAndThrowException(dualErrorMessage, dualErrorMessage);
	}	
	
	private static void logAndThrowException(String internalErrorMessage, String userErrorMessage) throws Exception {
		logger.error(internalErrorMessage);
		throw new Exception(userErrorMessage);
	}
	
	public static boolean isValidationObjectiveACertainType(String validationObjective, Collection<String> types) {
		for(String type: types) {
			if(validationObjective.equalsIgnoreCase(type)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isValidationObjectiveMu2Type(String validationObjective) {
		return isValidationObjectiveACertainType(validationObjective, CCDATypes.MU2_TYPES);
	}
	
	private static boolean isValidationObjectiveDS4PType(String validationObjective) {
		return isValidationObjectiveACertainType(validationObjective, CCDATypes.DS4P_TYPES);
	}	
	
	private static boolean isValidationObjectiveCCDAType(String validationObjective) {
		return isValidationObjectiveACertainType(validationObjective, CCDATypes.NON_SPECIFIC_CCDA_TYPES);
	}

	private ArrayList<RefCCDAValidationResult> processValidationResults(final XPathIndexer xpathIndexer,
			ValidationResult result, SeverityLevel severityLevel) {
		totalConformanceErrorChecks = result.getValidationStatistics().shallCount;
		
		ArrayList<RefCCDAValidationResult> results = new ArrayList<RefCCDAValidationResult>();
		switch (severityLevel) {
		case ERROR:
			logger.info("limiting MDHT results to errors only");
			addValidationResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR, result.getErrorDiagnostics(), xpathIndexer);
			break;			
		case WARNING:
			logger.info("limiting MDHT results to errors and warnings only");
			addValidationResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR, result.getErrorDiagnostics(), xpathIndexer);
			addValidationResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_WARN, result.getWarningDiagnostics(), xpathIndexer);			
			break;
		default:
		// break excluded and default above info on purpose in order to flow into next case/add all results/not duplicate code if there's no match
		case INFO:
			logger.info("adding all MDHT results, errors, warnings, and info");
			addValidationResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR, result.getErrorDiagnostics(), xpathIndexer);
			addValidationResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_WARN, result.getWarningDiagnostics(), xpathIndexer);
			addValidationResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_INFO, result.getInfoDiagnostics(), xpathIndexer);			
			break;
		}
		return results;
	}	
	
	private void addValidationResults(ArrayList<RefCCDAValidationResult> results, ValidationResultType currentValidationResultType,
			List<Diagnostic> diagnosticsOfCurrentSeverity, final XPathIndexer xpathIndexer) {
		for (Diagnostic diagnostic : diagnosticsOfCurrentSeverity) {
			results.add(buildValidationResult(diagnostic, xpathIndexer, currentValidationResultType));
		}
	}

	private RefCCDAValidationResult buildValidationResult(Diagnostic diagnostic, XPathIndexer xPathIndexer,
			ValidationResultType resultType) {
		CDADiagnostic diag = new CDADiagnostic(diagnostic);
		String lineNumber = getLineNumberInXMLUsingXpath(xPathIndexer, diagnostic);
		MDHTResultDetails mdhtResultDetails = populateMDHTResultDetails(diag, resultType);
		return createNewValidationResult(diag, resultType, lineNumber, mdhtResultDetails);
	}
	
	private MDHTResultDetails populateMDHTResultDetails(CDADiagnostic diag, ValidationResultType resultType) {
		MDHTResultDetails mdhtResultDetails = new MDHTResultDetails();
		if (diag.getSource() != null) {
			boolean isIGIssue = diag.getSource().contains(IG_ISSUE_ID);
			boolean isMUIssue = isValidationObjectiveMu2Type ? diag.getSource().contains(MU_ISSUE_ID) : false;
			boolean isDS4PIssue = isValidationObjectiveDS4PType ? diag.getSource().contains(DS4P_ISSUE_ID) : false;
			//IG/MU2 - all severities
			if(resultType == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR || 
					resultType == ValidationResultType.CCDA_MDHT_CONFORMANCE_WARN || 
					resultType == ValidationResultType.CCDA_MDHT_CONFORMANCE_INFO) {
				if (isIGIssue) {
					mdhtResultDetails.setIGIssue(true);
					return mdhtResultDetails;
				} else if (isMUIssue) {
					mdhtResultDetails.setMUIssue(true);
					return mdhtResultDetails;
				} else if (isDS4PIssue) {
					mdhtResultDetails.setDS4PIssue(true);
					return mdhtResultDetails;					
				} else {
					//schema - errors only
					if(resultType == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR) {
						// javax.xml.validation.Validator, org.eclipse.emf.ecore, etc.
						mdhtResultDetails.setSchemaError(true);
						if (diag.getPath() != null && diag.getCode() > 0) {
							// org.eclipse.emf.ecore, etc.
							mdhtResultDetails.setDataTypeSchemaError(true);
							return mdhtResultDetails;
						}
					}					
				}
			}
		}
		return mdhtResultDetails;
	}

	private String getLineNumberInXMLUsingXpath(final XPathIndexer xpathIndexer, Diagnostic diagnostic) {
		String generatedPath = "";
		if (diagnostic.getData().size() > 0 && diagnostic.getData().get(0) instanceof EObject) {
			generatedPath = getPath((EObject) diagnostic.getData().get(0));
		}
		XPathIndexer.ElementLocationData eld = xpathIndexer.getElementLocationByPath(generatedPath.toUpperCase());
		String lineNumber = eld != null ? Integer.toString(eld.line) : "Line number not available";
		return lineNumber;
	}

	public String getPath(EObject eObject) {
		String path = "";
		while (eObject != null && !(eObject instanceof DocumentRoot)) {
			EStructuralFeature feature = eObject.eContainingFeature();
			EObject container = eObject.eContainer();
			Object value = container.eGet(feature);
			if (feature.isMany()) {
				List<?> list = (List<?>) value;
				int index = list.indexOf(eObject) + 1;
				path = "/" + feature.getName() + "[" + index + "]" + path;
			} else {
				path = "/" + feature.getName() + "[1]" + path;
			}
			eObject = eObject.eContainer();
		}
		return path;
	}

	private RefCCDAValidationResult createNewValidationResult(CDADiagnostic cdaDiag, ValidationResultType resultType,
			String resultLineNumber, MDHTResultDetails mdhtResultDetails) {
		return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(
				cdaDiag.getMessage(), cdaDiag.getPath(), null, resultType, resultLineNumber)
				.mdhtResultDetails(mdhtResultDetails)
				.build();
	}
}
