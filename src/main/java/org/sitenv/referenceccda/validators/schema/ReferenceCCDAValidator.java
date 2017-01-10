package org.sitenv.referenceccda.validators.schema;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.mdht.uml.cda.DocumentRoot;
import org.eclipse.mdht.uml.cda.util.CDADiagnostic;
import org.eclipse.mdht.uml.cda.util.CDAUtil;
import org.eclipse.mdht.uml.cda.util.ValidationResult;
import org.openhealthtools.mdht.uml.cda.consol.ConsolPackage;
import org.openhealthtools.mdht.uml.cda.mu2consol.Mu2consolPackage;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.XPathIndexer;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class ReferenceCCDAValidator extends BaseCCDAValidator implements CCDAValidator {
	private static Logger logger = Logger.getLogger(ReferenceCCDAValidator.class);
	
	private static final String IG_ISSUE_ID = "a.consol", MU_ISSUE_ID = "a.mu2con";
	private boolean isValidationObjectiveMu2Type = false;
	
	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective,
			String referenceFileName, String ccdaFile) throws SAXException, Exception {
		final XPathIndexer xpathIndexer = new XPathIndexer();
		ValidationResult result = new ValidationResult();
		InputStream in = null;
		trackXPathsInXML(xpathIndexer, ccdaFile);
		try {
			in = IOUtils.toInputStream(ccdaFile, "UTF-8");
			validateDocumentByTypeUsingMDHTApi(in, validationObjective, result);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(result.getAllDiagnostics().isEmpty()) {
			logAndThrowException("The MDHT ValidationResult object was not populated for an unknown reason.");
		}
		logger.info("Processing and returning MDHT validation results");
		return processValidationResults(xpathIndexer, result);
	}

	private void validateDocumentByTypeUsingMDHTApi(InputStream in, String validationObjective, 
			ValidationResult result) throws Exception {
		if(StringUtils.isEmpty(validationObjective)) {
			logAndThrowException("The validationObjective given is " + (validationObjective == null ? "null" : "empty"),
					"The validationObjective given was null or empty. Please try one of the following valid Strings instead: "
					+ ValidationObjectives.getObjectives() + " " + CCDATypes.getTypes());
		}
		
		String mdhtValidationObjective = mapMdhtValidationObjective(validationObjective);
		logger.info("Mapped mdhtValidationObjective: " + (mdhtValidationObjective != null ? mdhtValidationObjective : "null objective"));
		if(mdhtValidationObjective != null) {
			//populate the field for reuse
			isValidationObjectiveMu2Type = isValidationObjectiveMu2Type(mdhtValidationObjective);
			if (isValidationObjectiveCCDAType(mdhtValidationObjective)) {
				Mu2consolPackage.eINSTANCE.unload();
				ConsolPackage.eINSTANCE.eClass();
				logger.info("Loading mdhtValidationObjective: " + mdhtValidationObjective
						+ " mapped from valdationObjective: " + validationObjective);
				CDAUtil.load(in, result);
			} else if (isValidationObjectiveMu2Type) {
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
					CDAUtil.loadAs(in, docType, result);
				} else {
					logAndThrowException("docType == null", "The MU2 docType EClass could not be assigned "
							+ "from mdhtValidationObjective: " + mdhtValidationObjective);
				}
			}
		} else {
			logAndThrowException("The validationObjective given is invalid", 
					"The validationObjective given was invalid. Please try one of the following valid Strings instead: "
					+ ValidationObjectives.getObjectives() + " " + CCDATypes.getTypes());
		}
	}
	
	private static String mapMdhtValidationObjective(String validationObjectivePOSTed) throws Exception {
		if (isValidationObjectiveACertainType(validationObjectivePOSTed, CCDATypes.NON_SPECIFIC_CCDA_TYPES) || 
				isValidationObjectiveACertainType(validationObjectivePOSTed, CCDATypes.MU2_TYPES)) {
			// we already have a *specific* MDHT objective (it was sent directly so no re-mapping required)
			return validationObjectivePOSTed;
		} else if (isValidationObjectiveACertainType(validationObjectivePOSTed,
				ValidationObjectives.ALL)) {
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
	
	public static boolean isValidationObjectiveACertainType(String validationObjective, List<String> types) {
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
	
	private static boolean isValidationObjectiveCCDAType(String validationObjective) {
		return isValidationObjectiveACertainType(validationObjective, CCDATypes.NON_SPECIFIC_CCDA_TYPES);
	}

	private ArrayList<RefCCDAValidationResult> processValidationResults(final XPathIndexer xpathIndexer,
			ValidationResult result) {
		ArrayList<RefCCDAValidationResult> results = new ArrayList<RefCCDAValidationResult>();
		addValidationResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR, result.getErrorDiagnostics(), xpathIndexer);
		addValidationResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_WARN, result.getWarningDiagnostics(), xpathIndexer);
		addValidationResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_INFO, result.getInfoDiagnostics(), xpathIndexer);		
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
		MDHTResultDetails mdhtResultDetails = new MDHTResultDetails(false, false, false, false);
		if (diag.getSource() != null) {
			boolean isIGIssue = diag.getSource().contains(IG_ISSUE_ID);
			boolean isMUIssue = isValidationObjectiveMu2Type ? diag.getSource().contains(MU_ISSUE_ID) : false;
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
				cdaDiag.getMessage(), cdaDiag.getPath(), null, resultType, resultLineNumber, mdhtResultDetails).build();
	}
}
