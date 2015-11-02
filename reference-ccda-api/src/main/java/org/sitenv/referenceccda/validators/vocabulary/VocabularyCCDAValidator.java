package org.sitenv.referenceccda.validators.vocabulary;

import org.apache.commons.io.IOUtils;
import org.sitenv.referenceccda.validators.BaseCCDAValidator;
import org.sitenv.referenceccda.validators.CCDAValidator;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.XPathIndexer;
import org.sitenv.referenceccda.validators.enums.ValidationResultType;
import org.sitenv.xml.validators.ccda.CcdaValidatorResult;
import org.sitenv.xml.xpathvalidator.engine.XPathValidationEngine;
import org.sitenv.xml.xpathvalidator.engine.data.XPathValidatorResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class VocabularyCCDAValidator extends BaseCCDAValidator implements CCDAValidator {
    private XPathValidationEngine engine = null;
    @Value("${referenceccda.configFile}")
    private String vocabularyXpathExpressionConfiguration;

    public ArrayList<RefCCDAValidationResult> validateFile(String validationObjective, String referenceFileName, String ccdaFile) {
        ArrayList<RefCCDAValidationResult> results = null;
        if (ccdaFile != null) {
            final XPathIndexer xpathIndexer = new XPathIndexer();
            initializeVocabularyValidationEngine();
            trackXPathsInXML(xpathIndexer, ccdaFile);
            try {
                results = doValidation(ccdaFile, xpathIndexer);
            } catch (IOException | SAXException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    private ArrayList<RefCCDAValidationResult> doValidation(String ccdaFile, XPathIndexer xpathIndexer) throws IOException, SAXException {
        List<XPathValidatorResult> validationResults = engine.validate(IOUtils.toInputStream(ccdaFile, "UTF-8"));
        ArrayList<RefCCDAValidationResult> results = new ArrayList<>();
        for (XPathValidatorResult result : validationResults) {
           results.add(createValidationResult(result, xpathIndexer));
        }
        return results;
    }

    private void initializeVocabularyValidationEngine() {
        if (engine == null) {
            engine = new XPathValidationEngine();
            engine.initialize(vocabularyXpathExpressionConfiguration);
        }
    }

    private RefCCDAValidationResult createValidationResult(XPathValidatorResult result, XPathIndexer xpathIndexer) {
        if (result instanceof CcdaValidatorResult){
            CcdaValidatorResult convertedResult = (CcdaValidatorResult)result;
            convertedResult.getRequestedCode();
            convertedResult.getRequestedCodeSystem();
            convertedResult.getRequestedCodeSystemName();
            convertedResult.getRequestedDisplayName();
            convertedResult.getExpectedValues();
            String resultMessage;
            ValidationResultType type;
            if(result.hasError()){
                resultMessage = result.getErrorMessage();
                type = ValidationResultType.CCDA_VOCAB_CONFORMANCE_ERROR;
            }else if(result.hasWarning()){
                resultMessage = result.getWarningMessage();
                type = ValidationResultType.CCDA_VOCAB_CONFORMANCE_WARN;
            }else{
                resultMessage = result.getInfoMessage();
                type = ValidationResultType.CCDA_VOCAB_CONFORMANCE_INFO;
            }
            String lineNumber = getLineNumberInXMLUsingXpath(xpathIndexer, result.getBaseXpathExpression());
            return new RefCCDAValidationResult.RefCCDAValidationResultBuilder(resultMessage, result.getXpathExpression(), type, lineNumber).actualCode(convertedResult.getRequestedCode()).actualCodeSystem(convertedResult.getRequestedCodeSystem()).actualDisplayName(convertedResult.getRequestedDisplayName()).expectedValueSet(Arrays.toString(convertedResult.getExpectedValues().toArray())).build();
        }
        return null;
    }

    private String getLineNumberInXMLUsingXpath(final XPathIndexer xpathIndexer, String xpath) {
        XPathIndexer.ElementLocationData eld = xpathIndexer.getElementLocationByPath(xpath.toUpperCase());
        String lineNumber = eld != null ? Integer.toString(eld.line) : "Line number not available";
        return lineNumber;
    }
}
