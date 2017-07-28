package org.sitenv.referenceccda.validators.vocabulary;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.XPathIndexer;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.CCDAIssueStates;
import org.sitenv.vocabularies.validation.dto.VocabularyValidationResult;
import org.sitenv.vocabularies.validation.services.VocabularyValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class VocabularyCCDAValidator extends BaseCCDAValidator implements CCDAValidator {
	private static Logger logger = Logger.getLogger(VocabularyCCDAValidator.class);
    @Value("${referenceccda.configFile}")
    private String vocabularyXpathExpressionConfiguration;
    private VocabularyValidationService vocabularyValidationService;

    @Autowired
    public VocabularyCCDAValidator(VocabularyValidationService vocabularyValidationService) {
        this.vocabularyValidationService = vocabularyValidationService;
    }

    //------------------------- INTERNAL CODE CHANGE  START --------------------------   
    public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName, String ccdaFile, String severityLevel) throws SAXException, ParserConfigurationException {
	    //------------------------- INTERNAL CODE CHANGE  END --------------------------   
        ArrayList<RefCCDAValidationResult> results = null;
        if (ccdaFile != null) {
            try {
                results = doValidation(ccdaFile, severityLevel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private ArrayList<RefCCDAValidationResult> doValidation(String ccdaFile, String severityLevel) throws IOException, SAXException, ParserConfigurationException {

    	// Build arraylist list with offsets of each line in the original file.
    	List<Integer> lineOffsets = new ArrayList<Integer>();
		String w;
		int z = 0;
		try {
			LineNumberReader r = new LineNumberReader(new StringReader(ccdaFile));
			while ((w = r.readLine()) != null) {
				z += w.getBytes("UTF8").length;
//				logger.info("Line:" + l + " Size:" + w.getBytes("UTF8").length + " z:" + z + " " + w);
				z++; // CR
				z++; // LF
				lineOffsets.add(new Integer(z));
			}
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
		
		List<VocabularyValidationResult> validationResults = vocabularyValidationService.validate(IOUtils.toInputStream(ccdaFile, "UTF-8"), severityLevel);
        ArrayList<RefCCDAValidationResult> results = new ArrayList<>();
        for (VocabularyValidationResult result : validationResults) {
            results.add(createValidationResult(result, lineOffsets));
        }
        return results;
    }
    
    //------------------------- INTERNAL CODE CHANGE  START --------------------------   
    private ArrayList<RefCCDAValidationResult> doValidation(String ccdaFile, XPathIndexer xpathIndexer, String severityLevel) throws IOException, SAXException, ParserConfigurationException {
	    //------------------------- INTERNAL CODE CHANGE  END --------------------------   
        List<VocabularyValidationResult> validationResults = vocabularyValidationService.validate(IOUtils.toInputStream(ccdaFile, "UTF-8"), severityLevel);
        ArrayList<RefCCDAValidationResult> results = new ArrayList<>();
        for (VocabularyValidationResult result : validationResults) {
            results.add(createValidationResult(result, xpathIndexer));
        }
        return results;
    }

    private RefCCDAValidationResult createValidationResult(VocabularyValidationResult result, XPathIndexer xpathIndexer) {
        ValidationResultType type;
        switch(result.getVocabularyValidationResultLevel()){
            case SHALL: type = ValidationResultType.CCDA_VOCAB_CONFORMANCE_ERROR;
                break;
            case SHOULD: type = ValidationResultType.CCDA_VOCAB_CONFORMANCE_WARN;
                break;
            default: type = ValidationResultType.CCDA_VOCAB_CONFORMANCE_INFO;
                break;
        }
        String lineNumber = getLineNumberInXMLUsingXpath(xpathIndexer, result.getNodeValidationResult().getValidatedDocumentXpathExpression());

        return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(result.getMessage(), result.getNodeValidationResult().getValidatedDocumentXpathExpression(), result.getNodeValidationResult().getConfiguredXpathExpression(), type, lineNumber)
                .actualCode(result.getNodeValidationResult().getRequestedCode())
                .actualCodeSystem(result.getNodeValidationResult().getRequestedCodeSystem())
                .actualCodeSystemName(result.getNodeValidationResult().getRequestedCodeSystemName())
                .actualDisplayName(result.getNodeValidationResult().getRequestedDisplayName())
                .build();
    }

    private RefCCDAValidationResult createValidationResult(VocabularyValidationResult result, List<Integer> lineOffsets) {
        ValidationResultType type;
        switch(result.getVocabularyValidationResultLevel()){
            case SHALL: type = ValidationResultType.CCDA_VOCAB_CONFORMANCE_ERROR;
                break;
            case SHOULD: type = ValidationResultType.CCDA_VOCAB_CONFORMANCE_WARN;
                break;
            default: type = ValidationResultType.CCDA_VOCAB_CONFORMANCE_INFO;
                break;
        }
        
        String lineNumber = getLineNumberInXML(result.getNodeValidationResult().getErrorOffset(),lineOffsets);

        return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(result.getMessage(), result.getNodeValidationResult().getValidatedDocumentXpathExpression(), result.getNodeValidationResult().getConfiguredXpathExpression(), type, lineNumber)
                .actualCode(result.getNodeValidationResult().getRequestedCode())
                .actualCodeSystem(result.getNodeValidationResult().getRequestedCodeSystem())
                .actualCodeSystemName(result.getNodeValidationResult().getRequestedCodeSystemName())
                .actualDisplayName(result.getNodeValidationResult().getRequestedDisplayName())
                .ruleID(result.getNodeValidationResult().getRuleID())
                .build();
    }
    
    private String getLineNumberInXML(long offset, List<Integer> lineOffsets) {
    	for (int i = 0; i < lineOffsets.size(); i++) {
			if (lineOffsets.get(i) > offset) {
				//logger.info("getLineNumberInXML Offset:" + offset + " Line:" + (i+1));
				return String.valueOf(i + 1);
			}
		}
		return "0";
    }

    private String getLineNumberInXMLUsingXpath(final XPathIndexer xpathIndexer, String xpath) {
        XPathIndexer.ElementLocationData eld = xpathIndexer.getElementLocationByPath(xpath.toUpperCase());
        String lineNumber = eld != null ? Integer.toString(eld.line) : "Line number not available";
        return lineNumber;
    }
}
