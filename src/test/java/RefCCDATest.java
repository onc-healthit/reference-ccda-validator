import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.sitenv.contentvalidator.service.ContentValidatorService;
import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.services.ReferenceCCDAValidationService;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.content.ReferenceContentValidator;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.CCDATypes;
import org.sitenv.referenceccda.validators.schema.ReferenceCCDAValidator;
import org.sitenv.referenceccda.validators.schema.ValidationObjectives;
import org.sitenv.referenceccda.validators.vocabulary.VocabularyCCDAValidator;
import org.sitenv.vocabularies.validation.services.VocabularyValidationService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

public class RefCCDATest {

	private static final boolean LOG_RESULTS_TO_CONSOLE = false;
	
	private static final boolean SHOW_ERRORS_ONLY = false;
	
	private static final int HAS_SCHEMA_ERROR_INDEX = 1;
	private static final int LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX = 2;
	private static final int INVALID_SNIPPET_ONLY_INDEX = 3;
	private static final int NON_CCDA_XML_HTML_FILE_WITH_XML_EXTENSION_INDEX = 4;
	private static final int BLANK_EMPTY_DOCUMENT_INDEX = 5;
	private static final int HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR = 6;

	// feel free to add docs to the end but don't alter existing data
	// - the same sample is referenced twice due to a loop test
	private static URI[] CCDA_FILES = new URI[0];
	static {
		try {
			CCDA_FILES = new URI[] {
					RefCCDATest.class.getResource("/Sample.xml").toURI(),
					RefCCDATest.class
							.getResource("/Sample_addSchemaErrors.xml").toURI(),
					RefCCDATest.class.getResource("/Sample.xml").toURI(),
					RefCCDATest.class.getResource(
							"/Sample_invalid-SnippetOnly.xml").toURI(),
					RefCCDATest.class.getResource("/Sample_basicHTML.xml")
							.toURI(),
					RefCCDATest.class.getResource(
							"/Sample_blank_Empty_Document.xml").toURI(),
					RefCCDATest.class.getResource(
							"/Sample_CCDA_CCD_b1_Ambulatory_v2.xml").toURI()};
		} catch (URISyntaxException e) {
			if(LOG_RESULTS_TO_CONSOLE) e.printStackTrace();
		}
	}

	@Test
	public void stringConversionAndResultsSizeTest() {
		String ccdaFileAsString = convertCCDAFileToString(CCDA_FILES[LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX]);
		println("ccdaFileAsString: " + ccdaFileAsString);
		assertFalse(
				"The C-CDA file String conversion failed as no data was captured",
				ccdaFileAsString.isEmpty());

		ArrayList<RefCCDAValidationResult> results = validateDocumentAndReturnResults(ccdaFileAsString);
		;
		println("No of Entries = " + results.size());
		assertFalse("No results were returned", results.isEmpty());

		println("***************** No Exceptions were thrown during the test******************"
						+ System.lineSeparator() + System.lineSeparator());
	}

	@Test
	public void hasSchemaErrorsAndDatatypeSchemaErrorTest() {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[HAS_SCHEMA_ERROR_INDEX]));
		println("global result");
		assertTrue(
				"The document has a schema error yet the flag is set to false",
				mdhtResultsHaveSchemaError(results));
		println("and for sanity, check the single results as well");
		printResults(getMDHTErrorsFromResults(results));
		boolean schemaErrorInSingleResultFound = false, expectedDataTypeSchemaErrorInResultsFound = false;
		String expectedDatatypeSchemaErrorPrefix = "The feature 'author' of";
		for (RefCCDAValidationResult result : results) {
			if (result.isSchemaError()) {
				schemaErrorInSingleResultFound = true;
				final String msgPrefix = "A schema error cannot also be an ";
				assertFalse(msgPrefix + "IG Issue", result.isIGIssue());
				assertFalse(msgPrefix + "MU2 Issue", result.isMUIssue());		
			}
			if(result.getDescription().contains(
					expectedDatatypeSchemaErrorPrefix)) {
				expectedDataTypeSchemaErrorInResultsFound = true;
			}
		}
		assertTrue("The document has at least one schema error but no single result flagged it as such", 
				schemaErrorInSingleResultFound);
		assertTrue("The document is expected to return has the following specific data type schema error (prefix) :"
						+ expectedDatatypeSchemaErrorPrefix, expectedDataTypeSchemaErrorInResultsFound);
	}

	@Test
	public void doesNotHaveSchemaErrorTest() {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX]));
		println("global result");
		assertFalse(
				"The document does not have schema error yet the flag is set to true",
				mdhtResultsHaveSchemaError(results));
		println("and for sanity, check the single results as well");
		boolean schemaErrorInSingleResultFound = false;
		printResults(getMDHTErrorsFromResults(results));
		for (RefCCDAValidationResult result : results)
			if (result.isSchemaError())
				schemaErrorInSingleResultFound = true;
		assertFalse(
				"The document has no single schema error yet a single result flagged it as true",
				schemaErrorInSingleResultFound);
	}

	@Test
	public void multipleDocumentsWithAndWithoutSchemaErrorTest() {
		for (int curCCDAFileIndex = 0; curCCDAFileIndex < LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX + 1; curCCDAFileIndex++) {
			println("***************** Running multipleDocumentsWithAndWithoutSchemaErrorTest test #"
							+ (curCCDAFileIndex + 1)
							+ " ******************"
							+ System.lineSeparator());

			ArrayList<RefCCDAValidationResult> results = 
					validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[curCCDAFileIndex]));

			println(System.lineSeparator()
					+ "CCDAIssueStates.hasSchemaError(): "
					+ mdhtResultsHaveSchemaError(results)
					+ System.lineSeparator());
			if (curCCDAFileIndex == 0
					|| curCCDAFileIndex == LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX) {
				assertFalse(
						"The document does not have schema error yet the flag is set to true",
						mdhtResultsHaveSchemaError(results));
			} else {
				assertTrue(
						"The document has a schema error yet the flag is set to false",
						mdhtResultsHaveSchemaError(results));
			}

			for (RefCCDAValidationResult result : results) {
				if (SHOW_ERRORS_ONLY) {
					if (result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR) {
						printResults(result);
					}
				} else {
					printResults(result);
				}
			}

			println("***************** End results for test #"
					+ (curCCDAFileIndex + 1) + " ******************"
					+ System.lineSeparator() + System.lineSeparator());
		}
	}
	
	@Test
	public void igOrMu2SchemaErrorsFileTest() {		
		runIgOrMu2AndNotSchemaTests(HAS_SCHEMA_ERROR_INDEX, CCDATypes.CLINICAL_OFFICE_VISIT_SUMMARY, true);
	}
	
	@Test
	public void igOrMu2NoSchemaErrorsHasMU2ErrorsFileTest() {
		runIgOrMu2AndNotSchemaTests(HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR, 
				CCDATypes.TRANSITIONS_OF_CARE_AMBULATORY_SUMMARY, false);
	}
	
	private static void runIgOrMu2AndNotSchemaTests(final int ccdaFileIndex, String ccdaTypesObjective, 
			boolean shouldHaveSchemaErrors) {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[ccdaFileIndex]), 
						ccdaTypesObjective);
		
		if(SHOW_ERRORS_ONLY) {
			printResults(getMDHTErrorsFromResults(results));
		} else {
			printResults(results);
		}
		
		boolean hasSchemaError = false;
		for (RefCCDAValidationResult result : getMDHTErrorsFromResults(results)) {
			if (result.isSchemaError()) {
				hasSchemaError = true;
			}
		}
		if(shouldHaveSchemaErrors) {
			assertTrue("The document IS supposed to have a schema error but one was NOT flagged", hasSchemaError);
		} else {
			assertFalse("The document is NOT supposed to have a schema error but one WAS flagged", hasSchemaError);
		}
		final String msgSuffix = " Issue cannot also be a schema error of type: ";
		final String msgSuffixSchemaError = msgSuffix + "isSchemaError";
		final String msgSuffixDatatypeSchemaError = msgSuffix + "isDatatypeError";
		for (RefCCDAValidationResult result : results) {
			if(result.getDescription().startsWith("Consol")) {
				assertTrue("The issue (" + result.getDescription() + ") is an IG Issue but was not flagged as such", result.isIGIssue());
				assertFalse("The issue (" + result.getDescription() + ") is an IG Issue but was flagged as an MU Issue", result.isMUIssue());
				assertFalse("An IG" + msgSuffixSchemaError + " (" + result.getDescription() + ")", result.isSchemaError());
				assertFalse("An IG" + msgSuffixDatatypeSchemaError + " (" + result.getDescription() + ")", result.isDataTypeSchemaError());
			} else if(result.getDescription().startsWith("Mu2consol")) {
				assertTrue("The issue (" + result.getDescription() + ") is an MU Issue but was not flagged as such", result.isMUIssue());
				assertFalse("The issue (" + result.getDescription() + ") is an MU Issue but was flagged as an IG Issue", result.isIGIssue());
				assertFalse("An Mu2" + msgSuffixSchemaError + " (" + result.getDescription() + ")", result.isSchemaError());
				assertFalse("An Mu2" + msgSuffixDatatypeSchemaError + " (" + result.getDescription() + ")", result.isDataTypeSchemaError());				
			}
		}		
	}

	@Test
	public void invalidSnippetOnlyValidationResultsTest() {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[INVALID_SNIPPET_ONLY_INDEX]));
		assertTrue(
				"The results should be null because a SAXParseException should have been thrown",
				results == null);
		println("Note: As indicated by a pass, the SAXParseException is expected for the document tested.");
	}

	@Test
	public void invalidSnippetOnlyServiceErrorTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				CCDATypes.NON_SPECIFIC_CCDAR2, INVALID_SNIPPET_ONLY_INDEX);
		handleServiceErrorTest(results);
	}

	@Test
	public void classCastMDHTExceptionThrownServiceErrorTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				CCDATypes.NON_SPECIFIC_CCDAR2,
				NON_CCDA_XML_HTML_FILE_WITH_XML_EXTENSION_INDEX);
		handleServiceErrorTest(results);
	}

	@Test
	public void altSaxParseMDHTExceptionThrownServiceErrorTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				CCDATypes.NON_SPECIFIC_CCDAR2, BLANK_EMPTY_DOCUMENT_INDEX);
		handleServiceErrorTest(results);
	}
	
	private static void handleServiceErrorTest(ValidationResultsDto results) {
		handleServiceErrorTest(results, true);
	}
	
	private static void handleServiceErrorTest(ValidationResultsDto results, boolean expectException) {
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
	}	
	
	@Test
	public void consolOnlyResultsRemainAfterSwitchAndBackTest() {
		handlePackageSwitchAndBackTestChoice(
				CCDATypes.NON_SPECIFIC_CCDAR2,
				convertCCDAFileToString(CCDA_FILES[HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR]));
	}
	
	@Test
	public void mu2ResultsAreRemovedAfterSwitchAndBackTest() {
		handlePackageSwitchAndBackTestChoice(
				CCDATypes.TRANSITIONS_OF_CARE_AMBULATORY_SUMMARY,
				convertCCDAFileToString(CCDA_FILES[HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR]));
	}

	private static void handlePackageSwitchAndBackTestChoice(String firstTestCCDATypesType, String ccdaFileAsString) {
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
		}
	}
	
	@Test
	public void invalidValidationObjectiveSentTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				"INVALID VALIDATION OBJECTIVE", HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR);
		handleServiceErrorTest(results);
	}
	
	@Test
	public void emptyStringValidationObjectiveSentTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				"", HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR);
		handleServiceErrorTest(results);
	}
	
	@Test
	public void allPossibleValidValidationObjectivesSentTest() {
		for (String objective : ValidationObjectives.ALL) {
			List<RefCCDAValidationResult> results = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
					convertCCDAFileToString(CCDA_FILES[HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR]), objective));
			printResults(results, false, false, false);
			assertTrue(results != null && !results.isEmpty());
		}
	}
	
	@Test
	public void allPossibleValidCcdaTypesSentTest() {
		List<String> legacyAndMu2Types = new ArrayList<String>();		
		legacyAndMu2Types.addAll(CCDATypes.NON_SPECIFIC_CCDA_TYPES);
		legacyAndMu2Types.addAll(CCDATypes.MU2_TYPES);
		for (String type : legacyAndMu2Types) {
			List<RefCCDAValidationResult> results = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
					convertCCDAFileToString(CCDA_FILES[HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR]), type));
			printResults(results, false, false, false);
			assertTrue(results != null && !results.isEmpty());
		}
	}
	
	@Ignore
	@Test
	public void basicNoExceptionServiceTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				ValidationObjectives.Sender.B1_TOC_AMB_170_315, HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR);
		handleServiceErrorTest(results, false);
	}		
	
	private static boolean hasMDHTValidationErrors(List<RefCCDAValidationResult> results) {
		return !getMDHTErrorsFromResults(results).isEmpty();
	}

	private static List<RefCCDAValidationResult> getMDHTErrorsFromResults(List<RefCCDAValidationResult> results) {
		List<RefCCDAValidationResult> mdhtErrors = new ArrayList<RefCCDAValidationResult>();
		for (RefCCDAValidationResult result : results) {
			if (result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR) {
				mdhtErrors.add(result);
			}
		}
		return mdhtErrors;
	}	
	
	private static boolean mdhtErrorsHaveProvidedPackageResult(List<RefCCDAValidationResult> mdhtErrors, String ccdaTypeToCheckFor) {
		boolean hasMu2 = false, hasConsol = false;
		for(RefCCDAValidationResult mdhtError :  mdhtErrors) {
			if(mdhtError.getDescription().contains("Mu2consol")) {
				hasMu2 = true;
			}
			if(mdhtError.getDescription().contains("Consol")) {
				hasConsol = true;
			}
		}
		if(ccdaTypeToCheckFor.equals(CCDATypes.CCDAR11_MU2)) {
			return hasMu2;
		} else if(ccdaTypeToCheckFor.equals(CCDATypes.CCDAR21_OR_CCDAR11)) {
			return hasConsol;
		}
		return false;
	}

	private static String convertCCDAFileToString(URI ccdaFileURL) {
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
				if(LOG_RESULTS_TO_CONSOLE) e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	private static ValidationResultsDto runReferenceCCDAValidationServiceAndReturnResults(
			String validationObjective, final int XML_FILE_INDEX) {
//		MultipartFile mockSample = new MockMultipartFile("ccdaFileActualName", "ccdaFileOriginalName",
//		"text/xml", convertCCDAFileToString(CCDA_FILES[XML_FILE_INDEX]).getBytes());
		File file = new File(CCDA_FILES[XML_FILE_INDEX]);
		ReferenceCCDAValidationService referenceCcdaValidationService = null;
		MultipartFile mockSample = null;
		try(FileInputStream is = new FileInputStream(file)) {
			mockSample = new MockMultipartFile("ccdaFileActualName", "ccdaFileOriginalName",
					"text/xml", is);
			referenceCcdaValidationService = new ReferenceCCDAValidationService(
					new ReferenceCCDAValidator(), new VocabularyCCDAValidator(
							new VocabularyValidationService()),
					new ReferenceContentValidator(new ContentValidatorService()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(referenceCcdaValidationService == null || mockSample == null) {
			Assert.fail("referenceCcdaValidationService == null || mockSample == null");
			throw new NullPointerException("referenceCcdaValidationService is "
					+ (referenceCcdaValidationService == null ? "null" : "not null")
					+ ("mockSample is " + mockSample == null ? "null" : "not null"));
		}
		return referenceCcdaValidationService.validateCCDA(validationObjective, "", mockSample);
	}

	private static ArrayList<RefCCDAValidationResult> validateDocumentAndReturnResults(String ccdaFileAsString) {
		return validateDocumentAndReturnResults(ccdaFileAsString, CCDATypes.NON_SPECIFIC_CCDAR2);
	}
	
	private static ArrayList<RefCCDAValidationResult> validateDocumentAndReturnResults(String ccdaFileAsString, 
			String validationObjective) {
		ReferenceCCDAValidator referenceCCDAValidator = new ReferenceCCDAValidator();
		ArrayList<RefCCDAValidationResult> results = null;
		try {
			results = referenceCCDAValidator.validateFile(validationObjective, "Test",
					ccdaFileAsString);
		} catch (SAXException e) {
			if(LOG_RESULTS_TO_CONSOLE) e.printStackTrace();
		} catch (Exception e) {
			if(LOG_RESULTS_TO_CONSOLE) e.printStackTrace();
		}
		return results;
	}

	private static void printResults(List<RefCCDAValidationResult> results) {
			printResults(results, true, true, true);
	}
	
	private static void printResults(List<RefCCDAValidationResult> results, 
			boolean showSchema, boolean showType, boolean showIgOrMuType) {
		if (LOG_RESULTS_TO_CONSOLE) {
			if (!results.isEmpty()) {
				for (RefCCDAValidationResult result : results) {
					printResults(result, showSchema, showType, showIgOrMuType);
				}
				println();
			} else {
				println("There are no results to print as the list is empty.");
			}
		}
	}
	
	private static void printResults(RefCCDAValidationResult result) {
			printResults(result, true, true, true);
	}
	
	private static void printResults(RefCCDAValidationResult result, 
			boolean showSchema, boolean showType, boolean showIgOrMuType) {
		if (LOG_RESULTS_TO_CONSOLE) {
			println("Description : " + result.getDescription());
			if(showType) {
				println("Type : " + result.getType());
			}
			if(showSchema) {
				println("result.isSchemaError() : " + result.isSchemaError());
				println("result.isDataTypeSchemaError() : " + result.isDataTypeSchemaError());
			}
			if(showIgOrMuType) {
				println("result.isIGIssue() : " + result.isIGIssue());
				println("result.isMUIssue() : " + result.isMUIssue());				
			}
		}
	}

	private static void println() {
		if (LOG_RESULTS_TO_CONSOLE) System.out.println();		
	}
	
	private static void println(String message) {
		print(message);
		println();		
	}
	
	private static void print(String message) {
		if (LOG_RESULTS_TO_CONSOLE) System.out.print(message);		
	}

	private boolean mdhtResultsHaveSchemaError(
			List<RefCCDAValidationResult> mdhtResults) {
		for (RefCCDAValidationResult result : mdhtResults) {
			if (result.isSchemaError()) {
				return true;
			}
		}
		return false;
	}

}
