package org.sitenv.referenceccda.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.dto.ValidationResultsMetaData;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.content.ReferenceContentValidator;
import org.sitenv.referenceccda.validators.schema.ReferenceCCDAValidator;
import org.sitenv.referenceccda.validators.vocabulary.VocabularyCCDAValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

@Service
public class ReferenceCCDAValidationService {
    private ReferenceCCDAValidator referenceCCDAValidator;
    private VocabularyCCDAValidator vocabularyCCDAValidator;
    private ReferenceContentValidator goldMatchingValidator;
    
    private static final String ERROR_GENERAL_PREFIX = "The service has encountered ";
    private static final String ERROR_PARSING_PREFIX = ERROR_GENERAL_PREFIX + "an error parsing the document. ";
    private static final String ERROR_FOLLOWING_ERROR_POSTFIX = "the following error: ";
    private static final String ERROR_IO_EXCEPTION = ERROR_GENERAL_PREFIX + "the following input/output error: ";
	private static final String ERROR_CLASS_CAST_EXCEPTION = ERROR_PARSING_PREFIX
			+ "Please verify the document is valid against schema and "
			+ "contains a v3 namespace definition: ";
	private static final String ERROR_SAX_PARSE_EXCEPTION = ERROR_PARSING_PREFIX
			+ "Please verify the document does not contain in-line XSL styling and/or address " + ERROR_FOLLOWING_ERROR_POSTFIX;
	private static final String ERROR_GENERIC_EXCEPTION = ERROR_GENERAL_PREFIX + ERROR_FOLLOWING_ERROR_POSTFIX;

    @Autowired
    public ReferenceCCDAValidationService(ReferenceCCDAValidator referenceCCDAValidator, VocabularyCCDAValidator vocabularyCCDAValidator, 
    		ReferenceContentValidator goldValidator) {
        this.referenceCCDAValidator = referenceCCDAValidator;
        this.vocabularyCCDAValidator = vocabularyCCDAValidator;
        this.goldMatchingValidator = goldValidator;
    }

    public ValidationResultsDto validateCCDA(String validationObjective, String referenceFileName, MultipartFile ccdaFile) {
        ValidationResultsDto resultsDto = new ValidationResultsDto();
        ValidationResultsMetaData resultsMetaData = new ValidationResultsMetaData();
        List<RefCCDAValidationResult> validatorResults = new ArrayList<>();
        try {
            validatorResults = runValidators(validationObjective, referenceFileName, ccdaFile);
            resultsMetaData = buildValidationMedata(validatorResults, validationObjective);
            resultsMetaData.setCcdaFileName(ccdaFile.getName());
            resultsMetaData.setCcdaFileContents(new String(ccdaFile.getBytes()));
        } catch (IOException ioE) {
        	processValidateCCDAException(resultsMetaData, 
        			ERROR_IO_EXCEPTION + ioE.getMessage(), validationObjective);
        } catch (SAXException saxE) {
        	processValidateCCDAException(resultsMetaData, 
        			ERROR_SAX_PARSE_EXCEPTION + saxE.getMessage(), validationObjective);
		} catch (ClassCastException cce) {
			processValidateCCDAException(resultsMetaData, 
					ERROR_CLASS_CAST_EXCEPTION + cce.getMessage(), validationObjective);
		} catch (Exception catchAllE) {
			processValidateCCDAException(resultsMetaData, 
					ERROR_GENERIC_EXCEPTION + catchAllE.getMessage(), validationObjective);
        }
        resultsDto.setResultsMetaData(resultsMetaData);
        resultsDto.setCcdaValidationResults(validatorResults);
        return resultsDto;
    }
    
	private static void processValidateCCDAException(
			ValidationResultsMetaData resultsMetaData,
			String serviceErrorMessage, String validationObjective) {
		resultsMetaData.setServiceError(true);
		resultsMetaData.setServiceErrorMessage(serviceErrorMessage);
		resultsMetaData.setCcdaDocumentType(validationObjective);
	}

    private List<RefCCDAValidationResult> runValidators(String validationObjective, String referenceFileName,
                                                        MultipartFile ccdaFile) throws SAXException, Exception {
        List<RefCCDAValidationResult> validatorResults = new ArrayList<>();
        InputStream ccdaFileInputStream = null;
        try {
            ccdaFileInputStream = ccdaFile.getInputStream();
            String ccdaFileContents = IOUtils.toString(new BOMInputStream(ccdaFileInputStream));

            List<RefCCDAValidationResult> mdhtResults = doMDHTValidation(validationObjective, referenceFileName, ccdaFileContents);
            if(mdhtResults != null && !mdhtResults.isEmpty())
            	validatorResults.addAll(mdhtResults);
            
            if (!mdhtResultsHaveSchemaError(mdhtResults)) {
                List<RefCCDAValidationResult> vocabResults = DoVocabularyValidation(validationObjective, referenceFileName, ccdaFileContents);
            	if(vocabResults != null && !vocabResults.isEmpty())
                	validatorResults.addAll(vocabResults);

                List<RefCCDAValidationResult> contentResults = doContentValidation(validationObjective, referenceFileName, ccdaFileContents);
            	if(contentResults != null && !contentResults.isEmpty()) {
                	validatorResults.addAll(contentResults);
            	}
            }
        } catch (IOException e) {
            throw new RuntimeException("Error getting CCDA contents from provided file", e);
        }finally {
            closeFileInputStream(ccdaFileInputStream);
        }
        return validatorResults;
    }

	private boolean mdhtResultsHaveSchemaError(List<RefCCDAValidationResult> mdhtResults) {
        for(RefCCDAValidationResult result : mdhtResults){
            if(result.isSchemaError()){
                return true;
            }
        }
		return false;
	}

    private ArrayList<RefCCDAValidationResult> DoVocabularyValidation(String validationObjective, String referenceFileName, String ccdaFileContents) throws SAXException {
        return vocabularyCCDAValidator.validateFile(validationObjective, referenceFileName, ccdaFileContents);
    }

    private List<RefCCDAValidationResult> doMDHTValidation(String validationObjective, String referenceFileName, String ccdaFileContents) throws SAXException, Exception {
        return referenceCCDAValidator.validateFile(validationObjective, referenceFileName, ccdaFileContents);
    }
    
    private List<RefCCDAValidationResult> doContentValidation(String validationObjective, String referenceFileName, String ccdaFileContents) throws SAXException {
    	return goldMatchingValidator.validateFile(validationObjective, referenceFileName, ccdaFileContents);
    }

    private ValidationResultsMetaData buildValidationMedata(List<RefCCDAValidationResult> validatorResults, String ccdaDocType) {
        ValidationResultsMetaData resultsMetaData = new ValidationResultsMetaData();
        for (RefCCDAValidationResult result : validatorResults) {
            resultsMetaData.addCount(result.getType());
        }
        resultsMetaData.setCcdaDocumentType(ccdaDocType);
        return resultsMetaData;
    }

    private void closeFileInputStream(InputStream fileIs) {
        if (fileIs != null) {
            try {
                fileIs.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing CCDA file input stream", e);
            }
        }
    }
}
