package org.sitenv.referenceccda.validators.schema;

import static org.sitenv.referenceccda.validators.schema.CCDAIssueStates.getCurrentDataTypeSchemaErrorState;
import static org.sitenv.referenceccda.validators.schema.CCDAIssueStates.getCurrentSchemaErrorState;
import static org.sitenv.referenceccda.validators.schema.CCDAIssueStates.resetUniqueResultValues;
import static org.sitenv.referenceccda.validators.schema.CCDAIssueStates.setIsDataTypeSchemaError;
import static org.sitenv.referenceccda.validators.schema.CCDAIssueStates.setIsIGIssue;
import static org.sitenv.referenceccda.validators.schema.CCDAIssueStates.setIsSchemaError;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.openhealthtools.mdht.uml.cda.DocumentRoot;
import org.openhealthtools.mdht.uml.cda.consol.ConsolPackage;
import org.openhealthtools.mdht.uml.cda.util.CDADiagnostic;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.cda.util.ValidationResult;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.XPathIndexer;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class ReferenceCCDAValidator extends BaseCCDAValidator implements CCDAValidator {

	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective,
			String referenceFileName, String ccdaFile) throws SAXException {
		final XPathIndexer xpathIndexer = new XPathIndexer();
		ValidationResult result = new ValidationResult();
		InputStream in = null;
		createValidationResultObjectToCollectDiagnosticsProducedDuringValidation();
		try {
			in = IOUtils.toInputStream(ccdaFile, "UTF-8");
			CDAUtil.load(in, result);
			trackXPathsInXML(xpathIndexer, ccdaFile);
		}  catch (Exception e) {
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

	private void createValidationResultObjectToCollectDiagnosticsProducedDuringValidation() {
		ConsolPackage.eINSTANCE.eClass();
	}

	private ArrayList<RefCCDAValidationResult> processValidationResults(final XPathIndexer xpathIndexer,
			ValidationResult result) {
		ArrayList<RefCCDAValidationResult> results = new ArrayList<RefCCDAValidationResult>();
		CCDAIssueStates.resetHasSchemaError();
		for (Diagnostic diagnostic : result.getErrorDiagnostics()) {
			results.add(buildValidationResult(diagnostic, xpathIndexer, ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR));
		}

		for (Diagnostic diagnostic : result.getWarningDiagnostics()) {
			results.add(buildValidationResult(diagnostic, xpathIndexer, ValidationResultType.CCDA_MDHT_CONFORMANCE_WARN));
		}

		for (Diagnostic diagnostic : result.getInfoDiagnostics()) {
			results.add(buildValidationResult(diagnostic, xpathIndexer, ValidationResultType.CCDA_MDHT_CONFORMANCE_INFO));
		}
		return results;
	}

	private RefCCDAValidationResult buildValidationResult(Diagnostic diagnostic, XPathIndexer xPathIndexer,
			ValidationResultType resultType) {
		CDADiagnostic diag = new CDADiagnostic(diagnostic);
		String lineNumber = getLineNumberInXMLUsingXpath(xPathIndexer, diagnostic);
		if(resultType == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR) {
			setIssueType(diag);
		}
		return createNewValidationResult(diag, resultType, lineNumber);
	}
	
	private void setIssueType(CDADiagnostic cDiag) {
		resetUniqueResultValues();
		if (cDiag.getSource() != null) {
			if (cDiag.getSource().contains("a.consol")) {
				setIsIGIssue(true);
			} else {
				// javax.xml.validation.Validator, org.eclipse.emf.ecore, etc.
				setIsSchemaError(true);
				if (cDiag.getPath() != null && cDiag.getCode() > 0) {
					// org.eclipse.emf.ecore, etc.
					setIsDataTypeSchemaError(true);
				}
			}
		}
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
			String resultLineNumber) {
		return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(
				cdaDiag.getMessage(), cdaDiag.getPath(), null, resultType,
				resultLineNumber, getCurrentSchemaErrorState(),
				getCurrentDataTypeSchemaErrorState()).build();
	}
}
