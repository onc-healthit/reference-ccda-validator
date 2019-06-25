package org.sitenv.referenceccda.test.other;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.sitenv.referenceccda.test.other.ReferenceValidationLogger.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.dto.ValidationResultsMetaData;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.CCDATypes;
import org.sitenv.referenceccda.validators.schema.ReferenceCCDAValidator;
import org.sitenv.referenceccda.validators.schema.ValidationObjectives;
import org.sitenv.vocabularies.test.other.VocabularyValidationTester;
import org.xml.sax.SAXException;

public class ReferenceValidationTester extends VocabularyValidationTester {
	
	protected String handleServiceErrorTest(ValidationResultsDto results) {
		return handleServiceErrorTest(results, true);
	}
	
	protected static String handleServiceErrorTest(ValidationResultsDto results, boolean expectException) {
		boolean isServiceError = results.getResultsMetaData().isServiceError()
				&& (results.getResultsMetaData().getServiceErrorMessage() != null && !results
						.getResultsMetaData().getServiceErrorMessage()
						.isEmpty());
		if (isServiceError) {
			println("Service Error Message: "
					+ results.getResultsMetaData().getServiceErrorMessage());
		}
		if (expectException) {
			assertTrue(
					"The results are supposed to contain a service error since the snippet sent is invalid",
					isServiceError);
		} else {
			assertFalse(
					"The results are NOT supposed to contain a service error the XML file sent is valid",
					isServiceError);
		}
		return results.getResultsMetaData().getServiceErrorMessage();
	}	
	
	protected static void handlePackageSwitchAndBackTestChoice(String firstTestCCDATypesType, String ccdaFileAsString) {
		List<RefCCDAValidationResult> results = validateDocumentAndReturnResults(ccdaFileAsString, firstTestCCDATypesType);
		assertTrue("MDHT Errors must be returned", hasMDHTValidationErrors(results));
		
		if (firstTestCCDATypesType.equals(CCDATypes.NON_SPECIFIC_CCDAR2)
				|| firstTestCCDATypesType.equals(CCDATypes.NON_SPECIFIC_CCDA)) {
			println("check original results to ensure there are NO MU2 results (or that there ARE MU2 Results if MU2 type");
			List<RefCCDAValidationResult> mdhtErrors = getMDHTErrorsFromResults(results);
			printResults(mdhtErrors, false, false, false);
			assertFalse("Since this was not an MU2 validation, Mu2consolPackage results should NOT have been returned",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.CCDAR11_MU2));
			println("run a new validation against MU2 and ensure there ARE MU2 results");
			mdhtErrors = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
					ccdaFileAsString, CCDATypes.TRANSITIONS_OF_CARE_AMBULATORY_SUMMARY));		
			printResults(mdhtErrors, false, false, false);
			assertTrue("Since this WAS an MU2 validation, Mu2consolPackage results SHOULD have been returned",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.CCDAR11_MU2));
			println("run a final validation against Consol and ensure there are NO MU2 results and that there ARE Consol Results");
			mdhtErrors = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
					ccdaFileAsString, CCDATypes.NON_SPECIFIC_CCDA)); 
			printResults(mdhtErrors, false, false, false);
			assertFalse("Since this was a Consol validation (reverted from MU2), Mu2consolPackage results should NOT have been returned",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.CCDAR11_MU2));
			assertTrue("ConsolPackage results SHOULD have been returned since the document was tested against Consol and contains errors",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.CCDAR21_OR_CCDAR11));			
		} else if(firstTestCCDATypesType.equals(CCDATypes.TRANSITIONS_OF_CARE_AMBULATORY_SUMMARY)) {
			println("check original results to ensure there ARE MU2 results");
			List<RefCCDAValidationResult> mdhtErrors = getMDHTErrorsFromResults(results);
			printResults(mdhtErrors, false, false, false);
			assertTrue("Since this is an MU2 validation, Mu2consolPackage results SHOULD have been returned",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.CCDAR11_MU2));			
			println("run a new validation against Consol and ensure there are NO MU2 results)");
			mdhtErrors = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
					ccdaFileAsString, CCDATypes.NON_SPECIFIC_CCDAR2));		
			printResults(mdhtErrors, false, false, false);
			assertFalse("Since this was NOT an MU2 validation, Mu2consolPackage results should NOT have been returned",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.CCDAR11_MU2));			
			println("run a final validation against MU2 and ensure the MU2 results HAVE RETURNED");
			mdhtErrors = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
					ccdaFileAsString, CCDATypes.TRANSITIONS_OF_CARE_INPATIENT_SUMMARY)); 
			printResults(mdhtErrors, false, false, false);
			assertTrue("Since this was an MU2 validation (reverted from Consol), Mu2consolPackage results SHOULD have been returned",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.CCDAR11_MU2));
			assertTrue("ConsolPackage results SHOULD have been returned as well since MU2 inherits from consol "
					+ "and the doc tested has base level errors",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.CCDAR21_OR_CCDAR11));
		} else if(firstTestCCDATypesType.equals(ValidationObjectives.Receiver.B8_DS4P_AMB_170_315)) {
			println("check original results to ensure there ARE DS4P results");
			List<RefCCDAValidationResult> mdhtErrors = getMDHTErrorsFromResults(results);
			printResults(mdhtErrors, false, false, false);
			assertTrue("Since this is a DS4P validation, CONTENTPROFILEPackage results SHOULD have been returned",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.DS4P));			
			println("run a new validation against Consol and ensure there are NO DS4P results)");
			mdhtErrors = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
					ccdaFileAsString, CCDATypes.NON_SPECIFIC_CCDAR2));		
			printResults(mdhtErrors, false, false, false);
			assertFalse("Since this was NOT a DS4P validation, CONTENTPROFILEPackage results should NOT have been returned",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.DS4P));			
			println("run a final validation against DS4P and ensure the DS4P results HAVE RETURNED");
			mdhtErrors = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
					ccdaFileAsString, ValidationObjectives.Receiver.B8_DS4P_INP_170_315)); 
			printResults(mdhtErrors, false, false, false);
			assertTrue("Since this was a DS4P validation (reverted from Consol), CONTENTPROFILEPackage results SHOULD have been returned",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.DS4P));
			assertFalse("ConsolPackage results should NOT have been returned as well since DS4P does not inherit from consol "
					+ "but instead from CDA",
					mdhtErrorsHaveProvidedPackageResult(mdhtErrors, CCDATypes.CCDAR21_OR_CCDAR11));
		}
	}
	
	protected static void testDocumentTypeIdentificationAndObjectiveSentMetaDataUsingServiceNonVocab(ValidationResultsMetaData resultsMetaData, 
			final String docTypeExpected, final String docTypeSet, final String objectiveExpected, final String objectiveSet) {
		
		assertTrue("The document type should be '" + docTypeExpected + "' but it is '" + docTypeSet + "' instead",
				docTypeSet.equals(docTypeExpected));
		println("docTypeExpected: " + docTypeExpected);
		println("docTypeSet: " + docTypeSet);
		
		assertTrue("The given validation objective should be '" + objectiveExpected + "' but it is '" + objectiveSet + "' instead",
				objectiveSet.equals(objectiveExpected));
	}
	
	private static boolean hasMDHTValidationErrors(List<RefCCDAValidationResult> results) {
		return !getMDHTErrorsFromResults(results).isEmpty();
	}

	protected static List<RefCCDAValidationResult> getMDHTErrorsFromResults(List<RefCCDAValidationResult> results) {
		List<RefCCDAValidationResult> mdhtErrors = new ArrayList<RefCCDAValidationResult>();
		for (RefCCDAValidationResult result : results) {
			if (result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR) {
				mdhtErrors.add(result);
			}
		}
		return mdhtErrors;
	}
	
	protected static List<RefCCDAValidationResult> getSpecificIssuesFromResults(List<RefCCDAValidationResult> results,
			ValidationResultType infoType, ValidationResultType warnType, ValidationResultType errorType) {
		List<RefCCDAValidationResult> mdhtIssues = new ArrayList<RefCCDAValidationResult>();
		for (RefCCDAValidationResult result : results) {
			if (result.getType() == infoType || result.getType() == warnType || result.getType() == errorType) {
				mdhtIssues.add(result);
			}
		}
		return mdhtIssues;
	}

	protected static List<RefCCDAValidationResult> getMDHTIssuesFromResults(List<RefCCDAValidationResult> results) {
		return getSpecificIssuesFromResults(results, ValidationResultType.CCDA_MDHT_CONFORMANCE_INFO,
				ValidationResultType.CCDA_MDHT_CONFORMANCE_WARN, ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR);
	}

	protected static List<RefCCDAValidationResult> getVocabIssuesFromResults(List<RefCCDAValidationResult> results) {
		return getSpecificIssuesFromResults(results, ValidationResultType.CCDA_VOCAB_CONFORMANCE_INFO,
				ValidationResultType.CCDA_VOCAB_CONFORMANCE_WARN, ValidationResultType.CCDA_VOCAB_CONFORMANCE_ERROR);
	}

	protected static List<RefCCDAValidationResult> getContentIssuesFromResults(List<RefCCDAValidationResult> results) {
		return getSpecificIssuesFromResults(results, ValidationResultType.REF_CCDA_INFO,
				ValidationResultType.REF_CCDA_WARN, ValidationResultType.REF_CCDA_ERROR);
	}
	
	protected static int countSpecifcResults(List<RefCCDAValidationResult> results, ValidationResultType type) {
		int issueTypeCount = 0;
		for(RefCCDAValidationResult result : results) {
			if (result.getType() == type) {
				issueTypeCount++;
			}
		}
		return issueTypeCount;
	}
	
	private static boolean mdhtErrorsHaveProvidedPackageResult(List<RefCCDAValidationResult> mdhtErrors, String ccdaTypeToCheckFor) {
		boolean hasMu2 = false, hasConsol = false, hasDs4p = false;
		for(RefCCDAValidationResult mdhtError :  mdhtErrors) {
			if(mdhtError.getDescription().contains("Mu2consol")) {
				hasMu2 = true;
			}
			if(mdhtError.getDescription().contains("Consol")) {
				hasConsol = true;
			}
			if(mdhtError.getDescription().contains("CONTENTPROFILE")) {
				hasDs4p = true;
			}			
		}
		if(ccdaTypeToCheckFor.equals(CCDATypes.CCDAR11_MU2)) {
			return hasMu2;
		} else if(ccdaTypeToCheckFor.equals(CCDATypes.CCDAR21_OR_CCDAR11)) {
			return hasConsol;
		} else if(ccdaTypeToCheckFor.equals(CCDATypes.DS4P)) {
			return hasDs4p;
		}
		return false;
	}

	protected static String convertCCDAFileToString(URI ccdaFileURL) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(ccdaFileURL.getPath()));
			String sCurrentLine = "";
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}
		} catch (Exception e) {
			println(e.toString());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				if(logResults) e.printStackTrace();
			}
		}
		return sb.toString();
	}

	protected static ArrayList<RefCCDAValidationResult> validateDocumentAndReturnResults(String ccdaFileAsString) {
		return validateDocumentAndReturnResults(ccdaFileAsString, CCDATypes.NON_SPECIFIC_CCDAR2);
	}
	
	protected static ArrayList<RefCCDAValidationResult> validateDocumentAndReturnResults(String ccdaFileAsString, 
			String validationObjective) {
		ReferenceCCDAValidator referenceCCDAValidator = new ReferenceCCDAValidator();
		ArrayList<RefCCDAValidationResult> results = null;
		try {
			results = referenceCCDAValidator.validateFile(validationObjective, "Test",
					ccdaFileAsString);
		} catch (SAXException e) {
			if(logResults) e.printStackTrace();
		} catch (Exception e) {
			if(logResults) e.printStackTrace();
		}
		return results;
	}
	
	protected boolean mdhtResultsHaveSchemaError(
			List<RefCCDAValidationResult> mdhtResults) {
		for (RefCCDAValidationResult result : mdhtResults) {
			if (result.isSchemaError()) {
				return true;
			}
		}
		return false;
	}

}
