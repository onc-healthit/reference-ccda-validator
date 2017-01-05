import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import org.junit.Test;
import org.sitenv.contentvalidator.service.ContentValidatorService;
import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.services.ReferenceCCDAValidationService;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.content.ReferenceContentValidator;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.ReferenceCCDAValidator;
import org.sitenv.referenceccda.validators.vocabulary.VocabularyCCDAValidator;
import org.sitenv.vocabularies.validation.services.VocabularyValidationService;
import org.springframework.mock.web.MockMultipartFile;
import org.xml.sax.SAXException;

public class RefCCDATest {

	private static final boolean SHOW_ERRORS_ONLY = true;
	private static final int HAS_SCHEMA_ERROR_INDEX = 1;
	private static final int LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX = 2;
	private static final int INVALID_SNIPPET_ONLY_INDEX = 3;
	private static final int NON_CCDA_XML_HTML_FILE_WITH_XML_EXTENSION_INDEX = 4;
	private static final int BLANK_EMPTY_DOCUMENT_INDEX = 5;

	// feel free to add docs to the end but don't alter existing data
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
							"Sample_blank_Empty_Document.xml").toURI() };
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void stringConversionAndResultsSizeTest() {
		String ccdaFileAsString = convertCCDAFileToString(CCDA_FILES[LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX]);
		System.out.println("ccdaFileAsString: " + ccdaFileAsString);
		assertFalse(
				"The C-CDA file String conversion failed as no data was captured",
				ccdaFileAsString.isEmpty());

		ArrayList<RefCCDAValidationResult> results = validateDocumentAndReturnResults(ccdaFileAsString);
		;
		System.out.println("No of Entries = " + results.size());
		assertFalse("No results were returned", results.isEmpty());

		System.out
				.println("***************** No Exceptions were thrown during the test******************"
						+ System.lineSeparator() + System.lineSeparator());
	}

	@Test
	public void hasSchemaErrorTest() {
		ArrayList<RefCCDAValidationResult> results = validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[HAS_SCHEMA_ERROR_INDEX]));
		// global result
		assertTrue(
				"The document has a schema error yet the flag is set to false",
				mdhtResultsHaveSchemaError(results));
		// and for sanity, check the single results as well
		boolean schemaErrorInSingleResultFound = false;
		for (RefCCDAValidationResult result : results)
			if (result.isSchemaError())
				schemaErrorInSingleResultFound = true;
		assertTrue(
				"The document has at least one schema error but no single result flagged it as such",
				schemaErrorInSingleResultFound);
	}

	@Test
	public void doesNotHaveSchemaErrorTest() {
		ArrayList<RefCCDAValidationResult> results = validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX]));
		// global result
		assertFalse(
				"The document does not have schema error yet the flag is set to true",
				mdhtResultsHaveSchemaError(results));
		// and for sanity, check the single results as well
		boolean schemaErrorInSingleResultFound = false;
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
			System.out
					.println("***************** Running multipleDocumentsWithAndWithoutSchemaErrorTest test #"
							+ (curCCDAFileIndex + 1)
							+ " ******************"
							+ System.lineSeparator());

			ArrayList<RefCCDAValidationResult> results = validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[curCCDAFileIndex]));

			System.out.println(System.lineSeparator()
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

			System.out.println("***************** End results for test #"
					+ (curCCDAFileIndex + 1) + " ******************"
					+ System.lineSeparator() + System.lineSeparator());
		}
	}

	@Test
	public void invalidSnippetOnlyValidationResultsTest() {
		ArrayList<RefCCDAValidationResult> results = validateDocumentAndReturnResults(convertCCDAFileToString(CCDA_FILES[INVALID_SNIPPET_ONLY_INDEX]));
		assertTrue(
				"The results should be null because a SAXParseException should have been thrown",
				results == null);
		System.out
				.println("Note: As indicated by the pass, the SAXParseException is not an issue for the document tested.");
	}

	@Test
	public void invalidSnippetOnlyServiceErrorTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				"NonSpecificCCDAR2", INVALID_SNIPPET_ONLY_INDEX);
		handleServiceErrorTest(results);
	}

	@Test
	public void classCastMDHTExceptionThrownServiceErrorTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				"NonSpecificCCDAR2",
				NON_CCDA_XML_HTML_FILE_WITH_XML_EXTENSION_INDEX);
		handleServiceErrorTest(results);
	}

	@Test
	public void altSaxParseMDHTExceptionThrownServiceErrorTest() {
		ValidationResultsDto results = runReferenceCCDAValidationServiceAndReturnResults(
				"NonSpecificCCDAR2", BLANK_EMPTY_DOCUMENT_INDEX);
		handleServiceErrorTest(results);
	}

	private static void handleServiceErrorTest(ValidationResultsDto results) {
		boolean isServiceError = results.getResultsMetaData().isServiceError()
				&& (results.getResultsMetaData().getServiceErrorMessage() != null && !results
						.getResultsMetaData().getServiceErrorMessage()
						.isEmpty());
		assertTrue(
				"The results are supposed to contain a service error since the snippet sent is invalid",
				isServiceError);
		if (isServiceError) {
			System.out.println("Service Error Message: "
					+ results.getResultsMetaData().getServiceErrorMessage());
		}
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
			System.out.println(e.toString());
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private static ValidationResultsDto runReferenceCCDAValidationServiceAndReturnResults(
			String validationObjective, final int XML_FILE_INDEX) {
		MockMultipartFile mockSample = new MockMultipartFile("data", null,
				"text/xml", convertCCDAFileToString(CCDA_FILES[XML_FILE_INDEX])
						.getBytes());
		ReferenceCCDAValidationService referenceCcdaValidationService = new ReferenceCCDAValidationService(
				new ReferenceCCDAValidator(), new VocabularyCCDAValidator(
						new VocabularyValidationService()),
				new ReferenceContentValidator(new ContentValidatorService()));
		return referenceCcdaValidationService.validateCCDA(validationObjective,
				null, mockSample);
	}

	private static ArrayList<RefCCDAValidationResult> validateDocumentAndReturnResults(
			String ccdaFileAsString) {
		ReferenceCCDAValidator referenceCCDAValidator = new ReferenceCCDAValidator();
		ArrayList<RefCCDAValidationResult> results = null;
		try {
			results = referenceCCDAValidator.validateFile("Test", "Test",
					ccdaFileAsString);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}

	private static void printResults(RefCCDAValidationResult result) {
		System.out.println("Description : " + result.getDescription());
		System.out.println("Type : " + result.getType());
		System.out
				.println("result.isSchemaError() : " + result.isSchemaError());
		System.out.println("result.isDataTypeSchemaError() : "
				+ result.isDataTypeSchemaError());
		System.out.println();
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
