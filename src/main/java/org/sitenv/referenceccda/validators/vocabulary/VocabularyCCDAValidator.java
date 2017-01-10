package org.sitenv.referenceccda.validators.vocabulary;

import org.apache.commons.io.IOUtils;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.XPathIndexer;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.referenceccda.validators.schema.MDHTResultDetails;
import org.sitenv.vocabularies.validation.dto.VocabularyValidationResult;
import org.sitenv.vocabularies.validation.services.VocabularyValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class VocabularyCCDAValidator extends BaseCCDAValidator implements CCDAValidator {
    @Value("${referenceccda.configFile}")
    private String vocabularyXpathExpressionConfiguration;
    private VocabularyValidationService vocabularyValidationService;

    @Autowired
    public VocabularyCCDAValidator(VocabularyValidationService vocabularyValidationService) {
        this.vocabularyValidationService = vocabularyValidationService;
    }

    public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName, String ccdaFile) throws SAXException {
        ArrayList<RefCCDAValidationResult> results = null;
        if (ccdaFile != null) {
            final XPathIndexer xpathIndexer = new XPathIndexer();
            trackXPathsInXML(xpathIndexer, ccdaFile);
            try {
                results = doValidation(ccdaFile, xpathIndexer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private ArrayList<RefCCDAValidationResult> doValidation(String ccdaFile, XPathIndexer xpathIndexer) throws IOException, SAXException {
        List<VocabularyValidationResult> validationResults = vocabularyValidationService.validate(IOUtils.toInputStream(ccdaFile, "UTF-8"));
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

        return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(result.getMessage(), result.getNodeValidationResult().getValidatedDocumentXpathExpression(), result.getNodeValidationResult().getConfiguredXpathExpression(), type, lineNumber, 
        		new MDHTResultDetails(false, false, false, false))
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
