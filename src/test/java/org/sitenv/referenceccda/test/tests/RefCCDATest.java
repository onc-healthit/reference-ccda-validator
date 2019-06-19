package org.sitenv.referenceccda.test.tests;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.sitenv.referenceccda.test.other.ReferenceValidationLogger.printResults;
import static org.sitenv.vocabularies.test.other.ValidationLogger.println;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sitenv.contentvalidator.service.ContentValidatorService;
import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.services.ReferenceCCDAValidationService;
import org.sitenv.referenceccda.test.other.ReferenceValidationLogger;
import org.sitenv.referenceccda.test.other.ReferenceValidationTester;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.content.ReferenceContentValidator;
import org.sitenv.referenceccda.validators.enums.UsrhSubType;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.CCDATypes;
import org.sitenv.referenceccda.validators.schema.ReferenceCCDAValidator;
import org.sitenv.referenceccda.validators.schema.ValidationObjectives;
import org.sitenv.referenceccda.validators.vocabulary.VocabularyCCDAValidator;
import org.sitenv.vocabularies.configuration.ConfiguredValidationResultSeverityLevel;
import org.sitenv.vocabularies.constants.VocabularyConstants;
import org.sitenv.vocabularies.constants.VocabularyConstants.SeverityLevel;
import org.sitenv.vocabularies.test.other.ValidationLogger;
import org.sitenv.vocabularies.test.other.ValidationTest;
import org.sitenv.vocabularies.validation.services.VocabularyValidationService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import junit.runner.Version;

public class RefCCDATest extends ReferenceValidationTester implements ValidationTest {

	private static final boolean LOG_RESULTS_TO_CONSOLE = true;
	
	private static final boolean SHOW_ERRORS_ONLY = false;
	private static final boolean LOG_LOG4J = true;
	static {
		if(LOG_LOG4J) {
			BasicConfigurator.configure();
		}
	}
	
	private static final int HAS_SCHEMA_ERROR_INDEX = 1, LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX = 2,
			INVALID_SNIPPET_ONLY_INDEX = 3, NON_CCDA_XML_HTML_FILE_WITH_XML_EXTENSION_INDEX = 4,
			BLANK_EMPTY_DOCUMENT_INDEX = 5, HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR = 6, DS4P_FROM_MDHT = 7,
			DS4P_AMB_1 = 8, DS4P_INP_1 = 9, CCD_R21 = 10, DS4P_WITH_NO_DS4P_DATA_AMB = 11, DS4P_WITH_NO_DS4P_DATA_INP = 12,
			TWO_MEGS=13;

	// Feel free to add docs to the end but don't alter existing data
	// Note: The same sample is referenced twice due to a loop test
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
					RefCCDATest.class.getResource("/170.315_b8_ds4p_inp_sample2_v2.xml").toURI(),
					RefCCDATest.class.getResource("/tempResults_ValidatorBrokeETTGG.xml").toURI()
			};
		} catch (URISyntaxException e) {
			if(LOG_RESULTS_TO_CONSOLE) e.printStackTrace();
		}
	}
	
	@Override
	@Before
	public void initializeLogResultsToConsoleValue() {
		ValidationLogger.logResults = LOG_RESULTS_TO_CONSOLE;
		println("JUnit version: " + Version.id());
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
	@Ignore
	public void ds4pOfficialAmbulatory() {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[DS4P_AMB_1]), 
						ValidationObjectives.Receiver.B8_DS4P_AMB_170_315);
		List<RefCCDAValidationResult> mdhtErrors = getMDHTErrorsFromResults(results);
		printResultsBasedOnFlags(results);
		assertTrue("The Ambulatory DS4P file has errors but it should not have any errors", mdhtErrors.isEmpty());
		
	}	
	
	@Test
	@Ignore
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
	public void documentTypeIdentificationCCDAndObjectiveSentMetaDataUsingServiceNonVocabTest() {
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
	public void documentTypeIdentificationCCDR2AndObjectiveSentMetaDataUsingServiceNonVocabTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				CCDATypes.NON_SPECIFIC_CCDA, CCD_R21);
		
		final String docTypeExpected = UsrhSubType.CONTINUITY_OF_CARE_DOCUMENT.getName();  
		final String docTypeSet = results.getResultsMetaData().getCcdaDocumentType();
		final String objectiveExpected = CCDATypes.NON_SPECIFIC_CCDA;
		final String objectiveSet = results.getResultsMetaData().getObjectiveProvided();
		
		testDocumentTypeIdentificationAndObjectiveSentMetaDataUsingServiceNonVocab(results.getResultsMetaData(), 
				docTypeExpected, docTypeSet, objectiveExpected, objectiveSet);
	}
	
	@Test
	@Ignore
	public void globalCodeValidatorResultsVocabularyValidationConfigurationsCountTest() {
		setupInitParameters(true);
		injectDependencies();
		
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(CCDATypes.NON_SPECIFIC_CCDAR2,
				CCD_R21, new VocabularyCCDAValidator(getVocabularyValidationService()),
				"ccdaReferenceValidatorConfigTest");

		
		final int expectedConfigCount = 5;
		final int configCount = results.getResultsMetaData().getVocabularyValidationConfigurationsCount();
		println("vocabularyValidationConfigurationsCount: " + configCount);
		Assert.assertTrue("VocabularyValidationConfigurationsCount should be more than 0", configCount > 0);
		Assert.assertTrue("VocabularyValidationConfigurationsCount should equal to " + expectedConfigCount + " as per the content of "
				+ "requiredNodeValidatorMissingElementConfig", configCount == expectedConfigCount);
	}
	
	@Test
	public void globalCodeValidatorResultsVocabularyValidationConfigurationsCountProgrammaticTest() {
		createGenericExpressionForProgrammaticConfigAndInjectDependencies();
		
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(CCDATypes.NON_SPECIFIC_CCDAR2,
				CCD_R21, new VocabularyCCDAValidator(getVocabularyValidationService()));
		
		final int expectedConfigCount = 1;
		final int configCount = results.getResultsMetaData().getVocabularyValidationConfigurationsCount();
		println("vocabularyValidationConfigurationsCount: " + configCount);
		Assert.assertTrue("VocabularyValidationConfigurationsCount should be more than 0", configCount > 0);
		Assert.assertTrue("VocabularyValidationConfigurationsCount should equal to " + expectedConfigCount + " as per the content of "
				+ "requiredNodeValidatorMissingElementConfig", configCount == expectedConfigCount);
	}
	
	@Ignore // Does not work with maven install due to some glitch but works otherwise 
	@Test
	public void tempResults_UTF8_BOM_EttGg_GetMDHTErrors_Test() {
			List<RefCCDAValidationResult> results = getMDHTErrorsFromResults(validateDocumentAndReturnResults(
					convertCCDAFileToString(CCDA_FILES[TWO_MEGS]), CCDATypes.NON_SPECIFIC_CCDAR2));
			printResults(results, true, true, true);
	}
	
	@Test
	public void tempResults_UTF8_BOM_EttGg_runReferenceCCDAValidation_Test() {
		createGenericExpressionForProgrammaticConfigAndInjectDependencies();
		
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(CCDATypes.NON_SPECIFIC_CCDAR2,
				TWO_MEGS, new VocabularyCCDAValidator(getVocabularyValidationService()));
		
		printResults(getMDHTErrorsFromResults(results.getCcdaValidationResults()));
		println("Service Error: " + results.getResultsMetaData().getServiceErrorMessage());
	}
	
	@Test
	public void mDHTSeverityLevelTest() {
		createGenericExpressionForProgrammaticConfigAndInjectDependencies();

		// ERRORS ONLY
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(CCDATypes.NON_SPECIFIC_CCDAR2,
				HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR,
				new VocabularyCCDAValidator(getVocabularyValidationService()),
				new ReferenceContentValidator(new ContentValidatorService()), VocabularyConstants.Config.DEFAULT,
				SeverityLevel.ERROR);
		int oldErrorResultCount = results.getCcdaValidationResults().size();
		printResults(results.getCcdaValidationResults());

		for (int i = 0; i < results.getCcdaValidationResults().size(); i++) {
			RefCCDAValidationResult result = results.getCcdaValidationResults().get(i);
			assertTrue(
					"The result is not an error but only errors should be returned since SeverityLevel.ERROR was sent in.",
					result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR);
		}

		// ERRORS AND WARNINGS
		results = runReferenceCCDAValidationServiceAndReturnResults(CCDATypes.NON_SPECIFIC_CCDAR2,
				HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR,
				new VocabularyCCDAValidator(getVocabularyValidationService()),
				new ReferenceContentValidator(new ContentValidatorService()), VocabularyConstants.Config.DEFAULT,
				SeverityLevel.WARNING);
		int newErrorResultCount = getMDHTErrorsFromResults(results.getCcdaValidationResults()).size();
		int oldWarningResultCount = 0;
		printResults(results.getCcdaValidationResults());

		for (int i = 0; i < results.getCcdaValidationResults().size(); i++) {
			RefCCDAValidationResult result = results.getCcdaValidationResults().get(i);
			assertTrue(
					"The result is not an error or a warning but only errors or warnings should be returned since SeverityLevel.WARNING was sent in.",
					result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR
							|| result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_WARN);
			if (result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_WARN) {
				oldWarningResultCount++;
			}
		}
		assertTrue(
				"The error count has somehow changed from when SeverityLevel.ERROR was set vs SeverityLevel.WARNING. "
						+ "The errorResultCount was previously " + oldErrorResultCount + " but is now "
						+ newErrorResultCount,
				oldErrorResultCount == newErrorResultCount);
		assertTrue(
				"There wasn't at least one warning yet the file HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR has warnings "
						+ "and SeverityLevel.WARNING was sent in ",
				oldWarningResultCount > 0);

		// ERRORS AND WARNINGS AND INFO
		results = runReferenceCCDAValidationServiceAndReturnResults(CCDATypes.NON_SPECIFIC_CCDAR2,
				HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR,
				new VocabularyCCDAValidator(getVocabularyValidationService()),
				new ReferenceContentValidator(new ContentValidatorService()), VocabularyConstants.Config.DEFAULT,
				SeverityLevel.INFO);
		int newWarningResultCount = 0;
		int infoResultCount = 0;
		int finalErrorCount = 0;
		printResults(results.getCcdaValidationResults());

		for (int i = 0; i < results.getCcdaValidationResults().size(); i++) {
			RefCCDAValidationResult result = results.getCcdaValidationResults().get(i);
			if (result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_ERROR) {
				finalErrorCount++;
			} else if (result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_WARN) {
				newWarningResultCount++;
			} else if (result.getType() == ValidationResultType.CCDA_MDHT_CONFORMANCE_INFO) {
				infoResultCount++;
			}
		}
		assertTrue(
				"The warning count has somehow changed from when SeverityLevel.WARNING was set vs SeverityLevel.INFO. "
						+ "The warningResultCount was previously " + oldWarningResultCount + " but is now "
						+ newWarningResultCount,
				oldWarningResultCount == newWarningResultCount);
		assertTrue(
				"There wasn't at least one warning yet the file HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR has warnings "
						+ "and SeverityLevel.INFO was sent in ",
				newWarningResultCount > 0);
		assertTrue(
				"There wasn't at least one info yet the file HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR has info "
						+ "and SeverityLevel.INFO was sent in ",
				infoResultCount > 0);
		assertTrue(
				"There wasn't at least one error yet the file HAS_4_POSSIBLE_CONSOL_AND_1_POSSIBLE_MU2_ERROR has errors "
						+ "and SeverityLevel.INFO was sent in ",
				finalErrorCount > 0);
	}
	
	
	private void createGenericExpressionForProgrammaticConfigAndInjectDependencies() {
		String validationMessage = "Test Validation Message";
		// The following expression is not specifically relevant to the test but must be a valid expression
		String configuredXpathExpression = "//v3:observation/v3:templateId[@root='2.16.840.1.113883.10.20.22.4.2' and @extension='2015-08-01']"
				+ "/ancestor::v3:observation[1]/v3:value"
				+ "[@xsi:type='PQ' and not(@nullFlavor) and ancestor::v3:section[not(@nullFlavor)]]";
		programmaticallyConfigureRequiredNodeValidator(new ConfiguredValidationResultSeverityLevel("SHALL"), "@unit",
				validationMessage, configuredXpathExpression);
		injectDependencies();
	}
	
	private static void ds4pTestAgainstDocWithNoDS4PContent(int ccdaFileIndex, String type,
			String validationObjective) {
		ArrayList<RefCCDAValidationResult> results = 
				validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[ccdaFileIndex]), 
						validationObjective);		
		assertTrue("The " + type + 	" DS4P file returned an empty RefCCDAValidationResult object", !results.isEmpty());
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
	
	private static ValidationResultsDto runReferenceCCDAValidationServiceAndReturnResults(
			String validationObjective, final int XML_FILE_INDEX) {
		return runReferenceCCDAValidationServiceAndReturnResults(validationObjective, XML_FILE_INDEX,
				new VocabularyCCDAValidator(new VocabularyValidationService()));
	}

	private static ValidationResultsDto runReferenceCCDAValidationServiceAndReturnResults(
			String validationObjective, final int XML_FILE_INDEX, VocabularyCCDAValidator vocabularyCCDAValidator) {
		return runReferenceCCDAValidationServiceAndReturnResults(validationObjective, XML_FILE_INDEX,
				vocabularyCCDAValidator, new ReferenceContentValidator(new ContentValidatorService()));		
	}
	
	private static ValidationResultsDto runReferenceCCDAValidationServiceAndReturnResults(String validationObjective,
			final int XML_FILE_INDEX, VocabularyCCDAValidator vocabularyCCDAValidator, String vocabularyConfig) {
		return runReferenceCCDAValidationServiceAndReturnResults(validationObjective, XML_FILE_INDEX,
				vocabularyCCDAValidator, new ReferenceContentValidator(new ContentValidatorService()),
				vocabularyConfig);
	}	
	
	private static ValidationResultsDto runReferenceCCDAValidationServiceAndReturnResults(
			String validationObjective, final int XML_FILE_INDEX, VocabularyCCDAValidator vocabularyCCDAValidator, 
			ReferenceContentValidator referenceContentValidator) {	
		return runReferenceCCDAValidationServiceAndReturnResults(validationObjective, XML_FILE_INDEX,
				vocabularyCCDAValidator, referenceContentValidator, null);
	}
	
	private static ValidationResultsDto runReferenceCCDAValidationServiceAndReturnResults(
			String validationObjective, final int XML_FILE_INDEX, VocabularyCCDAValidator vocabularyCCDAValidator, 
			ReferenceContentValidator referenceContentValidator, String vocabularyConfig) {
		return runReferenceCCDAValidationServiceAndReturnResults(validationObjective, XML_FILE_INDEX,
				vocabularyCCDAValidator, referenceContentValidator, null, null); 
	}
	
	private static ValidationResultsDto runReferenceCCDAValidationServiceAndReturnResults(
			String validationObjective, final int XML_FILE_INDEX, VocabularyCCDAValidator vocabularyCCDAValidator, 
			ReferenceContentValidator referenceContentValidator, String vocabularyConfig, SeverityLevel severityLevel) {
		println("vocabularyConfig: " + vocabularyConfig);
		File file = new File(CCDA_FILES[XML_FILE_INDEX]);
		ReferenceCCDAValidationService referenceCcdaValidationService = null;
		MultipartFile mockSample = null;
		try(FileInputStream is = new FileInputStream(file)) {
			mockSample = new MockMultipartFile("ccdaFileActualName", file.getName(),
					"text/xml", is);
			referenceCcdaValidationService = new ReferenceCCDAValidationService(new ReferenceCCDAValidator(),
					vocabularyCCDAValidator, referenceContentValidator);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(referenceCcdaValidationService == null || mockSample == null) {
			Assert.fail("referenceCcdaValidationService == null || mockSample == null");
			throw new NullPointerException("referenceCcdaValidationService is "
					+ (referenceCcdaValidationService == null ? "null" : "not null")
					+ ("mockSample is " + mockSample == null ? "null" : "not null"));
		}

//		return vocabularyConfig == null
//				? referenceCcdaValidationService.validateCCDA(validationObjective, "", mockSample)
//				: referenceCcdaValidationService.validateCCDA(validationObjective, "", mockSample, vocabularyConfig);
		
		if(vocabularyConfig == null && severityLevel == null) {
			return referenceCcdaValidationService.validateCCDA(validationObjective, "", mockSample); 
		} else if(vocabularyConfig != null && severityLevel != null) {
			return referenceCcdaValidationService.validateCCDA(validationObjective, "", mockSample, vocabularyConfig, severityLevel);
		}
		return referenceCcdaValidationService.validateCCDA(validationObjective, "", mockSample, vocabularyConfig);

	}
	
	private static void printResultsBasedOnFlags(List<RefCCDAValidationResult> results) {
		if(SHOW_ERRORS_ONLY) {
			ReferenceValidationLogger.printResults(getMDHTErrorsFromResults(results));
		} else {
			ReferenceValidationLogger.printResults(results);
		}		
	}	

}
