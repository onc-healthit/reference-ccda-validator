package org.sitenv.referenceccda.validators.schema;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
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
		return processValidationResults(xpathIndexer, result);
	}

	private static void validateDocumentByTypeUsingMDHTApi(InputStream in, String validationObjective, 
			ValidationResult result) throws Exception{	
		if (validationObjective.equalsIgnoreCase(CCDATypes.NON_SPECIFIC_CCDAR2) || 
				validationObjective.equalsIgnoreCase(CCDATypes.NON_SPECIFIC_CCDA)) {
			Mu2consolPackage.eINSTANCE.unload();
			ConsolPackage.eINSTANCE.eClass();
			logger.info("Loading valdationObjective: " + validationObjective);
			CDAUtil.load(in, result);
		} else {
			Mu2consolPackage.eINSTANCE.reload();
			Mu2consolPackage.eINSTANCE.eClass();
			EClass docType = null;
			if (validationObjective.equalsIgnoreCase(CCDATypes.CLINICAL_OFFICE_VISIT_SUMMARY)) {
				docType = Mu2consolPackage.eINSTANCE.getClinicalOfficeVisitSummary();
			} else if (validationObjective.equalsIgnoreCase(CCDATypes.TRANSITIONS_OF_CARE_AMBULATORY_SUMMARY)) {
				docType = Mu2consolPackage.eINSTANCE.getTransitionOfCareAmbulatorySummary();
			} else if (validationObjective.equalsIgnoreCase(CCDATypes.TRANSITIONS_OF_CARE_INPATIENT_SUMMARY)) {
				docType = Mu2consolPackage.eINSTANCE.getTransitionOfCareInpatientSummary();
			} else if (validationObjective.equalsIgnoreCase(CCDATypes.VDT_AMBULATORY_SUMMARY)) {
				docType = Mu2consolPackage.eINSTANCE.getVDTAmbulatorySummary();
			} else if (validationObjective.equalsIgnoreCase(CCDATypes.VDT_INPATIENT_SUMMARY)) {
				docType = Mu2consolPackage.eINSTANCE.getVDTInpatientSummary();
			}
			logger.info("Loading valdationObjective: " + validationObjective + " as MU2 docType: " + docType.getName());
			CDAUtil.loadAs(in, docType, result);
		}
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
		boolean isResultIGIssue = false;
		boolean isResultMUIssue = false;
		boolean isResultSchemaError = false;
		boolean isResultDataTypeSchemaError = false;
		boolean isDocumentSchemaError = false;
		CDADiagnostic diag = new CDADiagnostic(diagnostic);
		String lineNumber = getLineNumberInXMLUsingXpath(xPathIndexer, diagnostic);
		if(resultType == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR) {
			if (diag.getSource() != null) {
				if (diag.getSource().contains("a.consol")) {
					isResultIGIssue = true;
				} else {
					// javax.xml.validation.Validator, org.eclipse.emf.ecore, etc.
					isResultSchemaError = true;
					if (diag.getPath() != null && diag.getCode() > 0) {
						// org.eclipse.emf.ecore, etc.
						isResultDataTypeSchemaError = true;
					}
				}
			}
		}
		return createNewValidationResult(diag, resultType, lineNumber, isResultSchemaError, isResultDataTypeSchemaError);
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
			String resultLineNumber, boolean isResultSchemaError, boolean isResultDataTypeSchemaError) {
		return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(
				cdaDiag.getMessage(), cdaDiag.getPath(), null, resultType,
				resultLineNumber, isResultSchemaError,
				isResultDataTypeSchemaError).build();
	}
}
