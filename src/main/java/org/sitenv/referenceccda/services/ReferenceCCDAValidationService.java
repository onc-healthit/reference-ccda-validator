package org.sitenv.referenceccda.services;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;
import org.sitenv.referenceccda.dto.ValidationResultsDto;
import org.sitenv.referenceccda.dto.ValidationResultsMetaData;
import org.sitenv.referenceccda.validators.RefCCDAValidationResult;
import org.sitenv.referenceccda.validators.content.ReferenceContentValidator;
import org.sitenv.referenceccda.validators.schema.CCDAIssueStates;
import org.sitenv.referenceccda.validators.schema.ReferenceCCDAValidator;
import org.sitenv.referenceccda.validators.vocabulary.VocabularyCCDAValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReferenceCCDAValidationService {
	private static Logger log = Logger.getLogger(ReferenceCCDAValidationService.class.getName());
    private ReferenceCCDAValidator referenceCCDAValidator;
    private VocabularyCCDAValidator vocabularyCCDAValidator;
    private ReferenceContentValidator goldMatchingValidator;

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
        } catch (SAXException | IOException e) {
            resultsMetaData.setServiceError(true);
            resultsMetaData.setServiceErrorMessage(e.getMessage());
            resultsMetaData.setCcdaDocumentType(validationObjective);
        }
        resultsDto.setResultsMetaData(resultsMetaData);
        resultsDto.setCcdaValidationResults(validatorResults);
        return resultsDto;
    }

    private List<RefCCDAValidationResult> runValidators(String validationObjective, String referenceFileName,
                                                        MultipartFile ccdaFile) throws SAXException {
        List<RefCCDAValidationResult> validatorResults = new ArrayList<>();
        try {
            String ccdaFileContents = IOUtils.toString(new BOMInputStream(ccdaFile.getInputStream()));

            List<RefCCDAValidationResult> mdhtResults = doMDHTValidation(validationObjective, referenceFileName, ccdaFileContents);
            if(mdhtResults != null && !mdhtResults.isEmpty())
            	validatorResults.addAll(mdhtResults);
            
            if (shouldRunVocabularyValidation()) {

                List<RefCCDAValidationResult> vocabResults = DoVocabularyValidation(validationObjective, referenceFileName, ccdaFileContents);
            	
            	if(vocabResults != null && !vocabResults.isEmpty())
                	validatorResults.addAll(vocabResults);
                
            }
            
            if(shouldRunContentValidation()) {
            	
            	log.info("Running Content validation");
                List<RefCCDAValidationResult> contentResults = doContentValidation(validationObjective, referenceFileName, ccdaFileContents);
            	
            	if(contentResults != null && !contentResults.isEmpty()) {
            		
            		log.info("Content Results = " + contentResults.size());
                	validatorResults.addAll(contentResults);
            	}
            }
        } catch (IOException e) {
            throw new RuntimeException("Error getting CCDA contents from provided file", e);
        }
        return validatorResults;
    }

	private boolean shouldRunVocabularyValidation() {
		return !CCDAIssueStates.hasSchemaError();
	}
	
	private boolean shouldRunContentValidation() {
		return !CCDAIssueStates.hasSchemaError();
	}

    private ArrayList<RefCCDAValidationResult> DoVocabularyValidation(String validationObjective, String referenceFileName, String ccdaFileContents) throws SAXException {
        return vocabularyCCDAValidator.validateFile(validationObjective, referenceFileName, ccdaFileContents);
    }

    private List<RefCCDAValidationResult> doMDHTValidation(String validationObjective, String referenceFileName, String ccdaFileContents) throws SAXException {
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
