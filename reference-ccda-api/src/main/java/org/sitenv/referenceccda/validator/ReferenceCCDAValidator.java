package org.sitenv.referenceccda.validator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
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
import org.sitenv.referenceccda.validator.enums.ValidationResultType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ReferenceCCDAValidator {

	public static ArrayList<RefCCDAValidationResult> validateCCDAWithReferenceFileName(String validationObjective,
			String referenceFileName, String ccdaFile) {
		final XPathIndexer xpathIndexer = new XPathIndexer();
		ValidationResult result = new ValidationResult();
		InputStream in = null;
		createValidationResultObjectToCollectDiagnosticsProducedDuringValidation();
		try {
			// Identify the objectives and check for the document type
			// Check if all files are C-CDA compliant
			in = IOUtils.toInputStream(ccdaFile, "UTF-8");
			CDAUtil.load(in, result);
			trackXPathsInXML(xpathIndexer, ccdaFile);
			// If there are no schema errors, check for Vocabulary errors
			// If there are no vocab errors, then check for reference CCDA
			// errors
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
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

	private static void createValidationResultObjectToCollectDiagnosticsProducedDuringValidation() {
		ConsolPackage.eINSTANCE.eClass();
	}

	private static void trackXPathsInXML(XPathIndexer xpathIndexer, String xmlString) {
		try {
			XMLReader parser = XMLReaderFactory.createXMLReader();
			parser.setContentHandler(xpathIndexer);
			try {
				InputSource inputSource = new InputSource(new StringReader(xmlString));
				parser.parse(inputSource);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error In Line Number Routine: Bad filename, path or invalid document.");
			}
		} catch (SAXException e) {
			e.printStackTrace();
			System.out.println("Error In Line Number Routine: Unable to parse document for location data.");
		}
	}

	private static ArrayList<RefCCDAValidationResult> processValidationResults(final XPathIndexer xpathIndexer,
			ValidationResult result) {
		ArrayList<RefCCDAValidationResult> results = new ArrayList<RefCCDAValidationResult>();

		for (Diagnostic diagnostic : result.getErrorDiagnostics()) {
			results.add(buildValidationResult(diagnostic, xpathIndexer, ValidationResultType.CCDA_IG_CONFORMANCE_ERROR));
		}

		for (Diagnostic diagnostic : result.getWarningDiagnostics()) {
			results.add(buildValidationResult(diagnostic, xpathIndexer, ValidationResultType.CCDA_IG_CONFORMANCE_WARN));
		}

		for (Diagnostic diagnostic : result.getInfoDiagnostics()) {
			results.add(buildValidationResult(diagnostic, xpathIndexer, ValidationResultType.CCDA_IG_CONFORMANCE_INFO));
		}
		return results;
	}

	private static RefCCDAValidationResult buildValidationResult(Diagnostic diagnostic, XPathIndexer xPathIndexer,
			ValidationResultType resultType) {
		CDADiagnostic diag = new CDADiagnostic(diagnostic);
		String lineNumber = getLineNumberInXMLUsingXpath(xPathIndexer, diagnostic);
		return createNewValidationResult(diag, resultType, lineNumber);
	}

	private static String getLineNumberInXMLUsingXpath(final XPathIndexer xpathIndexer, Diagnostic diagnostic) {
		String generatedPath = "";
		if (diagnostic.getData().size() > 0 && diagnostic.getData().get(0) instanceof EObject) {
			generatedPath = getPath((EObject) diagnostic.getData().get(0));
		}
		XPathIndexer.ElementLocationData eld = xpathIndexer.getElementLocationByPath(generatedPath.toUpperCase());
		String lineNumber = eld != null ? Integer.toString(eld.line) : "Line number not available";
		return lineNumber;
	}

	public static String getPath(EObject eObject) {
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

	private static RefCCDAValidationResult createNewValidationResult(CDADiagnostic cdaDiag, ValidationResultType resultType,
			String resultLineNumber) {
		return new RefCCDAValidationResult(cdaDiag.getMessage(), cdaDiag.getPath(), resultType, resultLineNumber);
	}
}
