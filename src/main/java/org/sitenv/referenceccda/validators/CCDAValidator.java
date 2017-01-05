package org.sitenv.referenceccda.validators;

import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * Created by Brian on 10/20/2015.
 */
public interface CCDAValidator {
	ArrayList<RefCCDAValidationResult> validateFile(String validationObjective,
			String referenceFileName, String ccdaFile) throws SAXException, Exception;
}
