package org.sitenv.referenceccda.validators;

import java.util.ArrayList;

import org.xml.sax.SAXException;

/**
 * Created by Brian on 10/20/2015.
 */
public interface CCDAValidator {
    ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName, String ccdaFile, String severityLevel) throws SAXException, Exception;
}
