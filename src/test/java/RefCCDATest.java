import org.junit.Test;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.CCDAIssueStates;
import org.sitenv.referenceccda.validators.schema.ReferenceCCDAValidator;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RefCCDATest {

	private static final boolean SHOW_ERRORS_ONLY = true;
	private static final int LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX = 2;
	private static final int HAS_SCHEMA_ERROR_INDEX = 1;

	// it is fine to add documents to the end of this if desired but do not
	// alter indexes 0, 1, or 2
	private static final URL[] CCDA_FILES = {
			RefCCDATest.class.getResource("/Sample.xml"),
			RefCCDATest.class.getResource("/Sample_addSchemaErrors.xml"),
			RefCCDATest.class.getResource("/Sample.xml") };

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
				CCDAIssueStates.hasSchemaError());
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
				CCDAIssueStates.hasSchemaError());
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

			System.out
					.println(System.lineSeparator()
							+ "CCDAIssueStates.hasSchemaError(): "
							+ CCDAIssueStates.hasSchemaError()
							+ System.lineSeparator());
			if (curCCDAFileIndex == 0
					|| curCCDAFileIndex == LAST_SCHEMA_TEST_AND_NO_SCHEMA_ERROR_INDEX) {
				assertFalse(
						"The document does not have schema error yet the flag is set to true",
						CCDAIssueStates.hasSchemaError());
			} else {
				assertTrue(
						"The document has a schema error yet the flag is set to false",
						CCDAIssueStates.hasSchemaError());
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

	private static String convertCCDAFileToString(URL ccdaFileURL) {
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

	private static ArrayList<RefCCDAValidationResult> validateDocumentAndReturnResults(
			String ccdaFileAsString) {
		ReferenceCCDAValidator referenceCCDAValidator = new ReferenceCCDAValidator();
		ArrayList<RefCCDAValidationResult> results = null;
		try {
			results = referenceCCDAValidator.validateFile("Test", "Test",
					ccdaFileAsString);
		} catch (SAXException e) {
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

}
