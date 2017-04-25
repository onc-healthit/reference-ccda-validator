package org.sitenv.referenceccda.validators;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.sitenv.referenceccda.validators.schema.CCDAIssueStates;
import org.xml.sax.SAXException;

/**
 * Created by Brian on 10/20/2015.
 */
public interface CCDAValidator {
    //------------------------- INTERNAL CODE CHANGE  START --------------------------   
    ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName, String ccdaFile, String severityLevel) throws SAXException, Exception;
    //------------------------- INTERNAL CODE CHANGE  END --------------------------   
}
