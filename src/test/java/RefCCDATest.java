import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.sitenv.contentvalidator.service.ContentValidatorService;
import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.dto.ValidationResultsMetaData;
import org.sitenv.referenceccda.services.ReferenceCCDAValidationService;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.content.ReferenceContentValidator;
import org.sitenv.referenceccda.validators.enums.UsrhSubType;
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
	
	private static final boolean SHOW_ERRORS_ONLY = true;
	
	private static final int HAS_SCHEMA_ERROR_INDEX = 1, LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX = 2,
			INVALID_SNIPPET_ONLY_INDEX = 3, NON_CCDA_XML_HTML_FILE_WITH_XML_EXTENSION_INDEX = 4,
			BLANK_EMPTY_DOCUMENT_INDEX = 5, HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR = 6, DS4P_FROM_MDHT = 7,
			DS4P_AMB_1 = 8, DS4P_INP_1 = 9, CCD_R21 = 10, DS4P_WITH_NO_DS4P_DATA_AMB = 11, DS4P_WITH_NO_DS4P_DATA_INP = 12;

	// feel free to add docs to the end but don't alter existing data
	// - the same sample is referenced twice due to a loop test
	private static URI[] CCDA_FILES = new URI[0];
	static {
		try {
			CCDA_FILES = new URI[] {
					RefCCDATest.class.getResource("/Sample.xml").toURI(),
					RefCCDATest.class.getResource("/Sample_addSchemaErrors.xml").toURI(),
					RefCCDATest.class.getResource("/Sample.xml").toURI(),
					RefCCDATest.class.getResource("/Sample_invalid-SnippetOnly.xml").toURI(),
					RefCCDATest.class.getResource("/Sample_basicHTML.xml").toURI(),
					RefCCDATest.class.getResource("/Sample_blank_Empty_Document.xml").toURI(),
					RefCCDATest.class.getResource("/Sample_CCDA_CCD_b1_Ambulatory_v2.xml").toURI(),
					RefCCDATest.class.getResource("/Sample_DS4P_MDHTGen.xml").toURI(),
					RefCCDATest.class.getResource("/170.315_b8_ds4p_amb_sample1_v5.xml").toURI(),
					RefCCDATest.class.getResource("/170.315_b8_ds4p_inp_sample1_v5.xml").toURI(),
					RefCCDATest.class.getResource("/170.315_b1_toc_amb_ccd_r21_sample1_v8.xml").toURI(),
					RefCCDATest.class.getResource("/170.315_b8_ds4p_amb_sample2_v2.xml").toURI(),
					RefCCDATest.class.getResource("/170.315_b8_ds4p_inp_sample2_v2.xml").toURI()
			};
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
		runIgOrMu2OrDS4PAndNotSchemaTests(HAS_SCHEMA_ERROR_INDEX, CCDATypes.CLINICAL_OFFICE_VISIT_SUMMARY, true);
	}
	
	@Test
	public void igOrMu2NoSchemaErrorsHasMU2ErrorsFileTest() {
		runIgOrMu2OrDS4PAndNotSchemaTests(HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR, 
				CCDATypes.TRANSITIONS_OF_CARE_AMBULATORY_SUMMARY, false);
	}
	
	@Test
	public void ds4pGeneralTestAndHasErrors() {		
		List<RefCCDAValidationResult> mdhtErrors = getMDHTErrorsFromResults(
				runIgOrMu2OrDS4PAndNotSchemaTests(DS4P_FROM_MDHT, CCDATypes.NON_SPECIFIC_DS4P, true));
		assertTrue("The DS4P file does not contain errors as it should", mdhtErrors.size() > 0);
	}
	
	@Test
	public void ds4pOfficialAmbulatory() {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[DS4P_AMB_1]), 
						ValidationObjectives.Receiver.B8_DS4P_AMB_170_315);
		List<RefCCDAValidationResult> mdhtErrors = getMDHTErrorsFromResults(results);
		printResultsBasedOnFlags(results);
		assertTrue("The Ambulatory DS4P file has errors but it should not have any errors", mdhtErrors.isEmpty());
		
	}	
	
	@Test
	public void ds4pOfficialInpatient() {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[DS4P_INP_1]), 
						ValidationObjectives.Receiver.B8_DS4P_INP_170_315);
		List<RefCCDAValidationResult> mdhtErrors = getMDHTErrorsFromResults(results);
		printResultsBasedOnFlags(results);
		assertTrue("The Inpatient DS4P file has errors but it should not have any errors", mdhtErrors.isEmpty());
	}
	
	@Test
	public void ds4pTestAgainstDocWithNoDS4PContentAmbulatory() {
		ds4pTestAgainstDocWithNoDS4PContent(DS4P_WITH_NO_DS4P_DATA_AMB, "Ambulatory",
				ValidationObjectives.Sender.B7_DS4P_AMB_170_315);		
	}
	
	@Test
	public void ds4pTestAgainstDocWithNoDS4PContentInpatient() {
		ds4pTestAgainstDocWithNoDS4PContent(DS4P_WITH_NO_DS4P_DATA_INP, "Inpatient",
				ValidationObjectives.Sender.B7_DS4P_INP_170_315);
	}
	
	private static void ds4pTestAgainstDocWithNoDS4PContent(int ccdaFileIndex, String type,
			String validationObjective) {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[ccdaFileIndex]), 
						validationObjective);		
		assertTrue("The " + type + " DS4P file returned an empty RefCCDAValidationResult object", !results.isEmpty());
		List<RefCCDAValidationResult> mdhtInfo = new ArrayList<>();
		for (RefCCDAValidationResult result : results) {
			if (result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_INFO) {
				mdhtInfo.add(result);
			}
		}
		assertTrue("The " + type + " DS4P file did not return any info messages", !mdhtInfo.isEmpty());
		printResultsBasedOnFlags(results);
	}
	
	private static ArrayList<RefCCDAValidationResult> runIgOrMu2OrDS4PAndNotSchemaTests(final int ccdaFileIndex,
			String ccdaTypesObjective, boolean shouldHaveSchemaErrors) {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[ccdaFileIndex]), 
						ccdaTypesObjective);
		
		printResultsBasedOnFlags(results);
		
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
				assertFalse("The issue (" + result.getDescription() + ") is an IG Issue but was flagged as a DS4P Issue", result.isDS4PIssue());
				assertFalse("An IG" + msgSuffixSchemaError + " (" + result.getDescription() + ")", result.isSchemaError());
				assertFalse("An IG" + msgSuffixDatatypeSchemaError + " (" + result.getDescription() + ")", result.isDataTypeSchemaError());
			} else if(result.getDescription().startsWith("Mu2consol")) {
				assertTrue("The issue (" + result.getDescription() + ") is an MU Issue but was not flagged as such", result.isMUIssue());
				assertFalse("The issue (" + result.getDescription() + ") is an MU Issue but was flagged as an IG Issue", result.isIGIssue());
				assertFalse("The issue (" + result.getDescription() + ") is an MU Issue but was flagged as a DS4P Issue", result.isDS4PIssue());
				assertFalse("An Mu2" + msgSuffixSchemaError + " (" + result.getDescription() + ")", result.isSchemaError());
				assertFalse("An Mu2" + msgSuffixDatatypeSchemaError + " (" + result.getDescription() + ")", result.isDataTypeSchemaError());				
			} else if(result.getDescription().startsWith("CONTENTPROFILE")) {
				assertTrue("The issue (" + result.getDescription() + ") is a DS4P Issue but was not flagged as such", result.isDS4PIssue());
				assertFalse("The issue (" + result.getDescription() + ") is a DS4P Issue but was flagged as an IG Issue", result.isIGIssue());
				assertFalse("The issue (" + result.getDescription() + ") is an DS4P Issue but was flagged as an MU Issue", result.isMUIssue());
				assertFalse("A DS4P" + msgSuffixSchemaError + " (" + result.getDescription() + ")", result.isSchemaError());
				assertFalse("A DS4P" + msgSuffixDatatypeSchemaError + " (" + result.getDescription() + ")", result.isDataTypeSchemaError());				
			}
		}
		
		return results;
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
	
	private String handleServiceErrorTest(ValidationResultsDto results) {
		return handleServiceErrorTest(results, true);
	}
	
	private static String handleServiceErrorTest(ValidationResultsDto results, boolean expectException) {
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
	
	@Test
	public void ds4pResultsAreRemovedAfterSwitchAndBackTest() {
		handlePackageSwitchAndBackTestChoice(
				CCDATypes.NON_SPECIFIC_DS4P,
				convertCCDAFileToString(CCDA_FILES[DS4P_FROM_MDHT]));
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
	
	//Ignoring test due to temporary allowance of invalid objectives
	@Ignore
	@Test
	public void invalidValidationObjectiveSentTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				"INVALID VALIDATION OBJECTIVE", HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR);
		final String msg = handleServiceErrorTest(results);
		final String match = "invalid";
		assertTrue("The service error returned did not contain: " + match, msg.contains(match));
	}
	
	//Temporary test introduced due to temporary allowance of invalid objectives
	@Test
	public void invalidValidationObjectiveSentConvertToDefaultTest() {
		List<RefCCDAValidationResult> results = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
				convertCCDAFileToString(CCDA_FILES[HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR]), 
				"INVALID VALIDATION OBJECTIVE"));
		printResults(results, false, false, false);
		assertTrue(results != null && !results.isEmpty());
	}	
	
	@Test
	public void emptyStringValidationObjectiveSentTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				"", HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR);
		final String msg = handleServiceErrorTest(results);
		final String match = "empty";
		assertTrue("The service error returned did not contain: " + match, msg.contains(match));
	}
	
	@Test
	public void allPossibleValidValidationObjectivesExceptDS4PSentTest() {
		for (String objective : ValidationObjectives.ALL_UNIQUE_EXCEPT_DS4P) {
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
	
	@Test
	public void testDocumentTypeIdentificationCCDAndObjectiveSentMetaDataUsingServiceNonVocab() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				CCDATypes.NON_SPECIFIC_CCDA, HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR);
		
		final String docTypeExpected = UsrhSubType.CONTINUITY_OF_CARE_DOCUMENT.getName();  
		final String docTypeSet = results.getResultsMetaData().getCcdaDocumentType();
		final String objectiveExpected = CCDATypes.NON_SPECIFIC_CCDA;
		final String objectiveSet = results.getResultsMetaData().getObjectiveProvided();
		
		testDocumentTypeIdentificationAndObjectiveSentMetaDataUsingServiceNonVocab(results.getResultsMetaData(), 
				docTypeExpected, docTypeSet, objectiveExpected, objectiveSet);
	}
	
	@Test
	public void testDocumentTypeIdentificationCCDR2AndObjectiveSentMetaDataUsingServiceNonVocab() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				CCDATypes.NON_SPECIFIC_CCDA, CCD_R21);
		
		final String docTypeExpected = UsrhSubType.CONTINUITY_OF_CARE_DOCUMENT.getName();  
		final String docTypeSet = results.getResultsMetaData().getCcdaDocumentType();
		final String objectiveExpected = CCDATypes.NON_SPECIFIC_CCDA;
		final String objectiveSet = results.getResultsMetaData().getObjectiveProvided();
		
		testDocumentTypeIdentificationAndObjectiveSentMetaDataUsingServiceNonVocab(results.getResultsMetaData(), 
				docTypeExpected, docTypeSet, objectiveExpected, objectiveSet);
	}
	
	private static void testDocumentTypeIdentificationAndObjectiveSentMetaDataUsingServiceNonVocab(ValidationResultsMetaData resultsMetaData, 
			final String docTypeExpected, final String docTypeSet, final String objectiveExpected, final String objectiveSet) {
		
		assertTrue("The document type should be '" + docTypeExpected + "' but it is '" + docTypeSet + "' instead",
				docTypeSet.equals(docTypeExpected));
		System.out.println("docTypeExpected");System.out.println(docTypeExpected);
		System.out.println("docTypeSet");System.out.println(docTypeSet);
		
		assertTrue("The given validation objective should be '" + objectiveExpected + "' but it is '" + objectiveSet + "' instead",
				objectiveSet.equals(objectiveExpected));
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
	
	private static void printResultsBasedOnFlags(List<RefCCDAValidationResult> results) {
		if(SHOW_ERRORS_ONLY) {
			printResults(getMDHTErrorsFromResults(results));
		} else {
			printResults(results);
		}		
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
			boolean showSchema, boolean showType, boolean showIgOrMuOrDS4PType) {
		if (LOG_RESULTS_TO_CONSOLE) {
			println("Description : " + result.getDescription());
			if(showType) {
				println("Type : " + result.getType());
			}
			if(showSchema) {
				println("result.isSchemaError() : " + result.isSchemaError());
				println("result.isDataTypeSchemaError() : " + result.isDataTypeSchemaError());
			}
			if(showIgOrMuOrDS4PType) {
				println("result.isIGIssue() : " + result.isIGIssue());
				println("result.isMUIssue() : " + result.isMUIssue());
				println("result.isDS4PIssue() : " + result.isDS4PIssue());
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
