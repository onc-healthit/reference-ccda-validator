package org.sitenv.referenceccda.validators.vocabulary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.XPathIndexer;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.vocabularies.constants.VocabularyConstants;
import org.sitenv.vocabularies.constants.VocabularyConstants.SeverityLevel;
import org.sitenv.vocabularies.validation.dto.GlobalCodeValidatorResults;
import org.sitenv.vocabularies.validation.dto.VocabularyValidationResult;
import org.sitenv.vocabularies.validation.services.VocabularyValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
@Scope(value = "prototype",proxyMode = ScopedProxyMode.TARGET_CLASS)
public class VocabularyCCDAValidator extends BaseCCDAValidator implements CCDAValidator {
    @Value("${referenceccda.configFile}")
    private String vocabularyXpathExpressionConfiguration;
    private VocabularyValidationService vocabularyValidationService;
    private GlobalCodeValidatorResults globalCodeValidatorResults;
    private static Logger logger = Logger.getLogger(VocabularyCCDAValidator.class);

    @Autowired
    public VocabularyCCDAValidator(VocabularyValidationService vocabularyValidationService) {
        this.vocabularyValidationService = vocabularyValidationService;
        globalCodeValidatorResults = new GlobalCodeValidatorResults();
    }

	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName,
			String ccdaFile) throws SAXException {
		return validateFileImplementation(validationObjective, referenceFileName, ccdaFile,
				VocabularyConstants.Config.DEFAULT, SeverityLevel.INFO);
    }

	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName,
			String ccdaFile, String vocabularyConfig) throws SAXException {
		return validateFileImplementation(validationObjective, referenceFileName, ccdaFile, vocabularyConfig,
				SeverityLevel.INFO);			
	}
	
	public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName,
			String ccdaFile, String vocabularyConfig, SeverityLevel severityLevel) throws SAXException {
		return validateFileImplementation(validationObjective, referenceFileName, ccdaFile, vocabularyConfig,
				severityLevel);			
	}
	
	private ArrayList<RefCCDAValidationResult> validateFileImplementation(String validationObjective, String referenceFileName,
			String ccdaFile, String vocabularyConfig, SeverityLevel severityLevel) throws SAXException {
        ArrayList<RefCCDAValidationResult> results = null;
        if (ccdaFile != null) {
            final XPathIndexer xpathIndexer = new XPathIndexer();
            trackXPathsInXML(xpathIndexer, ccdaFile);
            try {
            	logger.info("15: severityLevel (Enum) in validateFileImplementation for vocab calling doValidation " + severityLevel.name());
                results = doValidation(ccdaFile, xpathIndexer, vocabularyConfig, severityLevel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;		
	}

	private ArrayList<RefCCDAValidationResult> doValidation(String ccdaFile, XPathIndexer xpathIndexer,
			String vocabularyConfig, SeverityLevel severityLevel) throws IOException, SAXException {
		logger.info("16: severityLevel (Enum) in doValidation for vocab calling doValidation before run validate" + severityLevel.name());
		List<VocabularyValidationResult> validationResults = vocabularyValidationService
				.validate(IOUtils.toInputStream(ccdaFile, "UTF-8"), vocabularyConfig, severityLevel);
		globalCodeValidatorResults = vocabularyValidationService.getGlobalCodeValidatorResults();
        ArrayList<RefCCDAValidationResult> results = new ArrayList<>();
        for (VocabularyValidationResult result : validationResults) {
            results.add(createValidationResult(result, xpathIndexer));
        }
        return results;
    }
	
	public GlobalCodeValidatorResults getGlobalCodeValidatorResults() {
		return globalCodeValidatorResults;
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

        return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(result.getMessage(), 
        		result.getNodeValidationResult().getValidatedDocumentXpathExpression(), 
        		result.getNodeValidationResult().getConfiguredXpathExpression(), type, lineNumber)
                .actualCode(result.getNodeValidationResult().getRequestedCode())
                .actualCodeSystem(result.getNodeValidationResult().getRequestedCodeSystem())
                .actualCodeSystemName(result.getNodeValidationResult().getRequestedCodeSystemName())
                .actualDisplayName(result.getNodeValidationResult().getRequestedDisplayName())
                .build();
    }

    private String getLineNumberInXMLUsingXpath(final XPathIndexer xpathIndexer, String xpath) {
        XPathIndexer.ElementLocationData eld = xpathIndexer.getElementLocationByPath(xpath.toUpperCase());
        String lineNumber = eld != null ? Integer.toString(eld.line) : "Line number not available";
        return lineNumber;
    }
}
